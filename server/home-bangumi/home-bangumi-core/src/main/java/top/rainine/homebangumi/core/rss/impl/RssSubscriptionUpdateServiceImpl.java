package top.rainine.homebangumi.core.rss.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.event.data.EpisodeTitleParseFailedEvent;
import top.rainine.homebangumi.core.event.data.EpisodeTorrentDownloadFailedEvent;
import top.rainine.homebangumi.core.rss.data.RssBangumiIncrementalParseConfig;
import top.rainine.homebangumi.core.rss.episoderenamer.EpisodeTitleRenameAdapter;
import top.rainine.homebangumi.core.rss.episoderenamer.EpisodeTitleRenameAdapterFactory;
import top.rainine.homebangumi.core.settings.DownloaderSettingsService;
import top.rainine.homebangumi.core.settings.data.QbittorrentDownloaderSettings;
import top.rainine.homebangumi.core.event.HbEventBus;
import top.rainine.homebangumi.core.event.data.RssSubscriptionUpdatedEvent;
import top.rainine.homebangumi.core.rss.RssBangumiParseService;
import top.rainine.homebangumi.core.rss.RssSubscriptionUpdateService;
import top.rainine.homebangumi.core.rss.data.RssBangumiEpisodePreviewInfo;
import top.rainine.homebangumi.dao.po.HbBangumi;
import top.rainine.homebangumi.dao.po.HbRssBangumi;
import top.rainine.homebangumi.dao.po.HbRssBangumiEpisode;
import top.rainine.homebangumi.dao.repository.HbRssBangumiEpisodeRepository;
import top.rainine.homebangumi.def.enums.*;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * @authoer rainine
 * @date 2024/4/23 00:34
 * @desc
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RssSubscriptionUpdateServiceImpl implements RssSubscriptionUpdateService {

    private final RssBangumiComponent rssBangumiComponent;

    private final HbRssBangumiEpisodeRepository rssBangumiEpisodeRepository;

    private final RssBangumiParseService rssBangumiParseService;

    private final HbEventBus eventBus;

    private final DownloaderSettingsService downloaderSettingsService;

    private final EpisodeTitleRenameAdapterFactory episodeTitleRenameAdapterFactory;

    @Override
    @Async
    public void updateAllRssSubscriptions() {
        List<HbRssBangumi> bangumiList = rssBangumiComponent.loadAllActiveSubscribeRssBangumis();
        bangumiList.forEach(rssBangumi -> {
            try {
                updateRssSubscription(rssBangumi);
            } catch (HbBizException e) {
                log.warn("[RssSubscriptionUpdateService]update rss subscription failed, rssBangumiId: {}",
                        rssBangumi.getId(), e);
            } catch (Exception e) {
                log.error("[RssSubscriptionUpdateService]update rss subscription error, rssBangumiId: {}",
                        rssBangumi.getId(), e);
            }

        });
    }

    @Override
    public void updateRssSubscription(Long rssBangumiId) {
        HbRssBangumi rssBangumi = rssBangumiComponent.getRssBangumiOrThrow(rssBangumiId);
        updateRssSubscription(rssBangumi);
    }

    private void updateRssSubscription(HbRssBangumi rssBangumi) {
        List<HbRssBangumiEpisode> curEpisodes = rssBangumiEpisodeRepository.findAllByRssBangumiId(rssBangumi.getId());
        List<String> torrentLinks = curEpisodes.stream().map(HbRssBangumiEpisode::getTorrentLink).toList();

        HbBangumi bangumi = rssBangumiComponent.getBangumiOrThrow(rssBangumi.getBangumiId());

        EpisodeTitleRenameAdapter episodeTitleRenameAdapter = episodeTitleRenameAdapterFactory.newRenameAdapter(EpisodeTitleRenameMethodEnum.of(rssBangumi.getEpisodeTitleRenameMethod()),
                bangumi.getTitle(), rssBangumi.getCustomizeRenamedEpisodeTitleFormat());

        RssBangumiIncrementalParseConfig parseConfig = RssBangumiIncrementalParseConfig
                .builder()
                .rssCategory(RssCategoryEnum.of(rssBangumi.getRssCategory()))
                .rssLink(rssBangumi.getRssLink())
                .season(bangumi.getSeason())
                .episodeOffset(rssBangumi.getEpisodeOffset())
                .filteredOutRules(rssBangumiComponent.getFilteredOutRules(rssBangumi.getFilterRules()))
                .parsedTorrentLinks(torrentLinks)
                .episodeTitleRenameAdapter(episodeTitleRenameAdapter)
                .build();

        List<RssBangumiEpisodePreviewInfo> episodePreviewInfos = rssBangumiParseService.incrementalParse(parseConfig);

        if (CollectionUtils.isEmpty(episodePreviewInfos)) {
            return;
        }

        String bangumiTitle = rssBangumiComponent.getBangumiTitle(rssBangumi.getBangumiId());

        QbittorrentDownloaderSettings qbittorrentDownloaderSettings = downloaderSettingsService.getQbittorrentDownloaderSettings();
        Path qbDownloadDir = Paths.get(qbittorrentDownloaderSettings.downloadDir());
        List<HbRssBangumiEpisode> newEpisodeList = episodePreviewInfos
                .stream()
                .map(episodePreviewInfo -> {
                    Path downloadDir;
                    if (Objects.equals(DownloaderCategoryEnum.QBITTORRENT.getCategory(), rssBangumi.getDownloaderCategory())) {
                        downloadDir = qbDownloadDir;
                    } else {
                        throw new IllegalArgumentException("downloader not supported, downloader: " + rssBangumi.getDownloaderCategory());
                    }

                    return rssBangumiComponent.toHbRssBangumiEpisode(rssBangumi.getId(), bangumiTitle, episodePreviewInfo,
                            downloadDir, rssBangumi.getDownloaderCategory());
                })
                .toList();
        rssBangumiEpisodeRepository.saveAll(newEpisodeList);
        log.info("[RssSubscriptionUpdateService]rss subscription update success, rss name: {}, new episode count: {}", rssBangumi.getRssName(), newEpisodeList.size());

        // 更新成功后，抛出有更新的事件
        if (RssBangumiStatusEnum.ACTIVE.equals(rssBangumi.getStatus())) {
            RssSubscriptionUpdatedEvent updatedEvent = new RssSubscriptionUpdatedEvent(rssBangumi.getId());
            eventBus.publishEvent(updatedEvent);
        }

        newEpisodeList.forEach(episode -> {
            // 抛出标题解析失败的事件
            if (Objects.equals(RssBangumiEpisodeStatusEnum.TITLE_PARSE_FAILED.getStatus(), episode.getStatus())) {
                eventBus.publishEvent(new EpisodeTitleParseFailedEvent(episode.getId()));
            } else if (Objects.equals(RssBangumiEpisodeStatusEnum.TORRENT_DOWNLOAD_FAILED.getStatus(), episode.getStatus())) {
                eventBus.publishEvent(new EpisodeTorrentDownloadFailedEvent(episode.getId()));
            }
        });
    }
}
























