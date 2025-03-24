package top.rainine.homebangumi.core.rss.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.downloader.EpisodeDownloadAdapter;
import top.rainine.homebangumi.core.downloader.EpisodeDownloaderManager;
import top.rainine.homebangumi.core.downloader.data.TorrentDownloadStatusInfo;
import top.rainine.homebangumi.core.downloader.data.TorrentFileRenameResultInfo;
import top.rainine.homebangumi.core.event.HbEventBus;
import top.rainine.homebangumi.core.event.data.EpisodeDownloadFailedEvent;
import top.rainine.homebangumi.core.event.data.EpisodeDownloadFinishedEvent;
import top.rainine.homebangumi.core.event.data.EpisodeRenameFailedEvent;
import top.rainine.homebangumi.core.event.data.EpisodeRenameFinishedEvent;
import top.rainine.homebangumi.core.rss.RssBangumiEpisodeDownloadService;
import top.rainine.homebangumi.dao.po.HbRssBangumi;
import top.rainine.homebangumi.dao.po.HbRssBangumiEpisode;
import top.rainine.homebangumi.dao.repository.HbRssBangumiEpisodeRepository;
import top.rainine.homebangumi.def.enums.DownloaderCategoryEnum;
import top.rainine.homebangumi.def.enums.RssBangumiEpisodeStatusEnum;
import top.rainine.homebangumi.def.enums.TorrentFileRenameResultEnum;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @authoer rainine
 * @date 2024/4/21 23:20
 * @desc 处理下载的服务类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RssBangumiEpisodeDownloadServiceImpl implements RssBangumiEpisodeDownloadService {

    private final HbRssBangumiEpisodeRepository episodeRepository;

    private final EpisodeDownloaderManager episodeDownloaderManager;

    private final RssBangumiComponent rssBangumiComponent;

    private final HbEventBus hbEventBus;

    private final NotFoundInDownloaderCounter notFoundInDownloaderCounter;

    @Override
    @Async
    public void pushAllToDownloader() {
        List<HbRssBangumi> bangumiList = rssBangumiComponent.loadAllActiveSubscribeRssBangumis();

        bangumiList.forEach(rssBangumi -> pushToDownloader(rssBangumi.getId()));
    }

    @Override
    public void pushToDownloader(Long rssBangumiId) {
        List<HbRssBangumiEpisode> episodes = episodeRepository.findAllByRssBangumiIdAndStatus(rssBangumiId, RssBangumiEpisodeStatusEnum.PARSED.getStatus());
        if (CollectionUtils.isEmpty(episodes)) {
            return;
        }

        List<HbRssBangumiEpisode> pushSuccessfulEpisodes = episodes.stream()
                .map(episode -> {
                    EpisodeDownloadAdapter episodeDownloadAdapter = getEpisodeDownloadAdapter(episode.getDownloaderCategory());
                    if (!episodeDownloadAdapter.auth()) {
                        return null;
                    }
                    String torrentIdentity = episodeDownloadAdapter.addTorrent(episode.getTorrentStoredPath(), episode.getEpisodeStoredPath(), false);
                    if (StringUtils.isBlank(torrentIdentity)) {
                        return null;
                    }
                    episode.setTorrentIdentity(torrentIdentity);
                    episode.setStatus(RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOADING.getStatus());
                    return episode;
                }).filter(Objects::nonNull)
                .toList();

        if (!pushSuccessfulEpisodes.isEmpty()) {
            episodeRepository.saveAll(pushSuccessfulEpisodes);
        }
        log.info("[RssBangumiEpisodeDownloadService]push episodes to downloader, rss bangumi id: {}, count: {}", rssBangumiId, pushSuccessfulEpisodes.size());
    }

    private EpisodeDownloadAdapter getEpisodeDownloadAdapter(Integer downloaderCategory) {
        return episodeDownloaderManager.getDownloaderAdapter(DownloaderCategoryEnum.of(downloaderCategory));
    }

    @Override
    public void deleteFromDownloader(DownloaderCategoryEnum downloaderCategory, String torrentIdentity, boolean deleteFile) {
        EpisodeDownloadAdapter episodeDownloadAdapter = episodeDownloaderManager.getDownloaderAdapter(downloaderCategory);
        // 如果下载器验证失败，那么不进行删除
        if (!episodeDownloadAdapter.auth()) {
            return;
        }

        episodeDownloadAdapter.removeTorrent(torrentIdentity, deleteFile);
    }

    @Override
    @Async
    public void checkAllEpisodes() {
        List<HbRssBangumiEpisode> episodes = episodeRepository.findAllByStatus(RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOADING.getStatus());
        if (CollectionUtils.isEmpty(episodes)) {
            return;
        }

        List<HbRssBangumiEpisode> downloadStatusChangedEpisodes = episodes.stream()
                .map(episode -> {
                    EpisodeDownloadAdapter episodeDownloadAdapter = getEpisodeDownloadAdapter(episode.getDownloaderCategory());
                    // 如果下载器验证失败，那么不进行检查
                    if (!episodeDownloadAdapter.auth()) {
                        return null;
                    }

                    TorrentDownloadStatusInfo torrentDownloadStatus = episodeDownloadAdapter.getTorrentDownloadStatus(episode.getTorrentIdentity());
                    switch (torrentDownloadStatus.status()) {
                        case FINISHED -> episode.setStatus(RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOAD_FINISHED.getStatus());
                        case NOT_FOUND -> {
                            int count = notFoundInDownloaderCounter.getCount(episode.getId());
                            // 如果3次内都还是NOT_FOUND，则进行重试
                            if (count < 3) {
                                notFoundInDownloaderCounter.inc(episode.getId());
                                return null;
                            } else {
                                episode.setStatus(RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOAD_FAILED.getStatus());
                                episode.setErrorMessage("not found episode torrent in downloader");
                            }
                        }
                        case FAILED ->  episode.setStatus(RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOAD_FAILED.getStatus());
                        case null, default -> {
                            return null;
                        }
                    }
                    return episode;
                }).filter(Objects::nonNull)
                .toList();

        // 从NOT_FOUND的缓存计数器中，移除
        episodes.forEach(episode -> notFoundInDownloaderCounter.remove(episode.getId()));

        List<HbRssBangumiEpisode> finishedEpisodes = episodes.stream()
                .filter(episode -> RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOAD_FINISHED.equals(episode.getStatus()))
                .toList();

        List<HbRssBangumiEpisode> failedEpisodes = episodes.stream()
                .filter(episode -> RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOAD_FAILED.equals(episode.getStatus()))
                .toList();
        if (!downloadStatusChangedEpisodes.isEmpty()) {
            episodeRepository.saveAll(downloadStatusChangedEpisodes);
        }

        log.info("[RssBangumiEpisodeDownloadService]check episodes download result end, finished count: {}, failed count: {}",
                finishedEpisodes.size(), failedEpisodes.size());

        // 发布下载完成事件
        finishedEpisodes.forEach(episode -> hbEventBus.publishEvent(new EpisodeDownloadFinishedEvent(episode.getId())));

        // 发布下载失败事件
        failedEpisodes.forEach(episode -> hbEventBus.publishEvent(new EpisodeDownloadFailedEvent(episode.getId())));
    }

    @Override
    public void renameEpisode(Long episodeId) {
        Optional<HbRssBangumiEpisode> episodeOptional = episodeRepository.findById(episodeId);
        if (episodeOptional.isEmpty()) {
            return;
        }

        HbRssBangumiEpisode episode = episodeOptional.get();
        renameDownloadFinishedEpisode(episode);
    }

    private void renameDownloadFinishedEpisode(HbRssBangumiEpisode episode) {
        if (!RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOAD_FINISHED.equals(episode.getStatus())) {
            return;
        }

        // 如果没有重命名后的存储路径，那么直接认为重命名失败
        if (StringUtils.isEmpty(episode.getRenamedEpisodeStoredPath())) {
            episode.setStatus(RssBangumiEpisodeStatusEnum.RENAME_FAILED.getStatus());
            episodeRepository.save(episode);
            hbEventBus.publishEvent(new EpisodeRenameFailedEvent(episode.getId()));
            return;
        }

        EpisodeDownloadAdapter episodeDownloadAdapter = getEpisodeDownloadAdapter(episode.getDownloaderCategory());
        // 如果下载器验证失败，那么不进行重命名
        if (!episodeDownloadAdapter.auth()) {
            return;
        }

        TorrentFileRenameResultInfo torrentFileRenameResultInfo = episodeDownloadAdapter.renameFile(episode.getTorrentIdentity(), episode.getEpisodeStoredPath(), episode.getRenamedEpisodeStoredPath());

        switch (torrentFileRenameResultInfo.result()) {
            case SUCCESS -> episode.setStatus(RssBangumiEpisodeStatusEnum.RENAME_FINISHED.getStatus());
            case FAILED -> {
                episode.setStatus(RssBangumiEpisodeStatusEnum.RENAME_FAILED.getStatus());
                episode.setErrorMessage(torrentFileRenameResultInfo.errorMessage());
            }
            case null, default -> {
                return;
            }
        }
        episodeRepository.save(episode);

        // 发布重命名结束事件
        if (Objects.equals(TorrentFileRenameResultEnum.SUCCESS, torrentFileRenameResultInfo.result())) {
            hbEventBus.publishEvent(new EpisodeRenameFinishedEvent(episode.getId()));
        } else if (Objects.equals(TorrentFileRenameResultEnum.FAILED, torrentFileRenameResultInfo.result())) {
            hbEventBus.publishEvent(new EpisodeRenameFailedEvent(episode.getId()));
        }
    }

    @Override
    public boolean renameFinishedEpisodeFileName(Integer downloaderCategory, String torrentIdentity, String oldStoredPath, String newStoredPath) {

        EpisodeDownloadAdapter episodeDownloadAdapter = getEpisodeDownloadAdapter(downloaderCategory);
        // 如果下载器验证失败，那么不进行重命名
        if (!episodeDownloadAdapter.auth()) {
            return false;
        }

        TorrentFileRenameResultInfo torrentFileRenameResultInfo = episodeDownloadAdapter.renameFile(torrentIdentity, oldStoredPath, newStoredPath);
        return Objects.equals(TorrentFileRenameResultEnum.SUCCESS, torrentFileRenameResultInfo.result());
    }
    @Override
    @Async
    public void renameAllEpisodes() {
        List<HbRssBangumiEpisode> episodes = episodeRepository.findAllByStatus(RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOAD_FINISHED.getStatus());
        if (CollectionUtils.isEmpty(episodes)) {
            return;
        }

        episodes.forEach(this::renameDownloadFinishedEpisode);
        long finishedEpisodesCount = episodes.stream()
                .filter(episode -> RssBangumiEpisodeStatusEnum.RENAME_FINISHED.equals(episode.getStatus()))
                .count();

        long failedEpisodesCount = episodes.stream()
                .filter(episode -> RssBangumiEpisodeStatusEnum.RENAME_FAILED.equals(episode.getStatus()))
                .count();

        log.info("[RssBangumiEpisodeDownloadService]rename episodes end, total count: {}, finished count: {}, failed count: {}",
                episodes.size(), finishedEpisodesCount, failedEpisodesCount);
    }
}






































