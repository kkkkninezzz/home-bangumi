package top.rainine.homebangumi.core.rss.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.rainine.homebangumi.api.common.BangumiInfoDto;
import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;
import top.rainine.homebangumi.common.utils.GsonUtils;
import top.rainine.homebangumi.core.event.data.EpisodeFinishedEvent;
import top.rainine.homebangumi.core.rss.data.RssBangumiParseConfig;
import top.rainine.homebangumi.core.settings.DownloaderSettingsService;
import top.rainine.homebangumi.core.settings.data.QbittorrentDownloaderSettings;
import top.rainine.homebangumi.core.downloader.EpisodeDownloadAdapter;
import top.rainine.homebangumi.core.event.HbEventBus;
import top.rainine.homebangumi.core.event.data.EpisodeDownloadFinishedEvent;
import top.rainine.homebangumi.core.event.data.RssSubscriptionUpdatedEvent;
import top.rainine.homebangumi.core.rss.RssBangumiEpisodeDownloadService;
import top.rainine.homebangumi.core.rss.RssBangumiParseService;
import top.rainine.homebangumi.core.rss.RssBangumiService;
import top.rainine.homebangumi.core.rss.RssSubscriptionUpdateService;
import top.rainine.homebangumi.core.rss.data.RssBangumiPreviewInfo;
import top.rainine.homebangumi.core.rss.data.convertor.RssBangumiConvertor;
import top.rainine.homebangumi.core.rss.data.convertor.RssBangumiEpisodeConvertor;
import top.rainine.homebangumi.core.utils.HbAdvisor;
import top.rainine.homebangumi.core.utils.PageRequestUtils;
import top.rainine.homebangumi.dao.po.*;
import top.rainine.homebangumi.dao.po.QHbBangumi;
import top.rainine.homebangumi.dao.po.QHbRssBangumi;
import top.rainine.homebangumi.dao.repository.HbBangumiRepository;
import top.rainine.homebangumi.dao.repository.HbRssBangumiEpisodeRepository;
import top.rainine.homebangumi.dao.repository.HbRssBangumiRepository;
import top.rainine.homebangumi.dao.utils.JpaUtils;
import top.rainine.homebangumi.def.enums.*;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author rainine
 * @description
 * @date 2024/4/8 18:29:44
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RssBangumiServiceImpl implements RssBangumiService, HbAdvisor<RssBangumiServiceImpl> {

    private final HbRssBangumiRepository hbRssBangumiRepository;

    private final HbBangumiRepository hbBangumiRepository;

    private final HbRssBangumiEpisodeRepository hbRssBangumiEpisodeRepository;

    private final RssBangumiParseService rssBangumiParseService;

    private final RssBangumiComponent rssBangumiComponent;

    private final DownloaderSettingsService downloaderSettingsService;

    private final RssBangumiEpisodeDownloadService episodeDownloadService;

    private final JPAQueryFactory jpaQueryFactory;

    private final RssSubscriptionUpdateService rssSubscriptionUpdateService;

    private final HbEventBus hbEventBus;

    private final RssBangumiStatisticComponent statisticComponent;

    private final RssBangumiEpisodeConvertor rssBangumiEpisodeConvertor;

    private final RssBangumiConvertor rssBangumiConvertor;

    @Override
    public PreviewRssBangumiResp previewAndCreateRssBangumi(PreviewRssBangumiReq req) {
        hbRssBangumiRepository.findByRssLink(req.getRssLink())
                .ifPresent(_ -> {
                    throw new HbBizException(HbCodeEnum.RSS_LINK_EXISTS);
                });

        RssCategoryEnum rssCategory = RssCategoryEnum.of(req.getRssCategory());
        List<String> filteredOutRules = Optional.ofNullable(req.getFilterRules()).orElseGet(ArrayList::new);
        EpisodeTitleRenameMethodEnum episodeTitleRenameMethod = EpisodeTitleRenameMethodEnum.of(req.getEpisodeTitleRenameMethod());
        if (EpisodeTitleRenameMethodEnum.CUSTOMIZED_TITLE.equals(episodeTitleRenameMethod) && StringUtils.isEmpty(req.getCustomizeRenamedEpisodeTitleFormat())) {
            throw new HbBizException(HbCodeEnum.CUSTOMIZE_RENAMED_EPISODE_TITLE_FORMAT_INVALID);
        }

        RssBangumiParseConfig parseConfig = RssBangumiParseConfig
                .builder()
                .rssCategory(rssCategory)
                .rssLink(req.getRssLink())
                .season(null)
                .episodeOffset(req.getEpisodeOffset())
                .filteredOutRules(filteredOutRules)
                .episodeTitleRenameMethod(episodeTitleRenameMethod)
                .customizeRenamedEpisodeTitleFormat(req.getCustomizeRenamedEpisodeTitleFormat())
                .build();

        RssBangumiPreviewInfo previewInfo = rssBangumiParseService.parse(parseConfig);

        Long rssBangumiId = self().createRssBangumi(req, previewInfo);
        return PreviewRssBangumiResp
                .builder()
                .id(rssBangumiId)
                .build();
    }

    @Transactional
    public Long createRssBangumi(PreviewRssBangumiReq req, RssBangumiPreviewInfo previewInfo) {
        HbBangumi hbBangumi = rssBangumiConvertor.toHbBangumi(previewInfo);
        hbBangumiRepository.save(hbBangumi);

        HbRssBangumi hbRssBangumi = rssBangumiConvertor.toHbRssBangumi(req);
        hbRssBangumi.setBangumiId(hbBangumi.getId());
        hbRssBangumiRepository.save(hbRssBangumi);

        QbittorrentDownloaderSettings qbittorrentDownloaderSettings = downloaderSettingsService.getQbittorrentDownloaderSettings();
        Path downloadDir = Paths.get(qbittorrentDownloaderSettings.downloadDir());
        List<HbRssBangumiEpisode> episodeList = previewInfo.episodes()
                .stream()
                .map(episodePreviewInfo -> rssBangumiComponent.toHbRssBangumiEpisode(hbRssBangumi.getId(), hbBangumi.getTitle(), episodePreviewInfo,
                        downloadDir, req.getDownloaderCategory())).toList();
        hbRssBangumiEpisodeRepository.saveAll(episodeList);

        return hbRssBangumi.getId();
    }

    /**
     * 根据id获取rss bangumi
     * @param id id
     * @throws HbBizException 如果获取失败，抛出异常
     * */
    private HbRssBangumi getRssBangumiOrThrow(Long id) {
        return rssBangumiComponent.getRssBangumiOrThrow(id);
    }

    /**
     * 根据id获取rss bangumi
     * @param id id
     * @throws HbBizException 如果获取失败，抛出异常
     * */
    private HbRssBangumi getNotArchivedRssBangumiOrThrow(Long id) {
        return rssBangumiComponent.getNotArchivedRssBangumiOrThrow(id);
    }

    @Override
    public RssBangumiDetailResp getRssBangumiDetail(Long id) {
        HbRssBangumi hbRssBangumi = getRssBangumiOrThrow(id);
        BangumiInfoDto bangumiInfo = hbBangumiRepository.findById(hbRssBangumi.getBangumiId())
                .map(rssBangumiConvertor::toBangumiInfoDto)
                .orElse(null);
        return rssBangumiConvertor.toRssBangumiDetailResp(hbRssBangumi, bangumiInfo);

    }

    /**
     * 计算番剧的排序值
     * */
    private int calEpisodeSortValue(HbRssBangumiEpisode episode) {
        Integer status = episode.getStatus();
        if (RssBangumiEpisodeStatusEnum.SKIPPED.equals(status)) {
            return 1000000 - Optional.ofNullable(episode.getEpisodeNo()).orElse(0);
        }

        if (RssBangumiEpisodeStatusEnum.FILTERED_OUT.equals(status)
                || RssBangumiEpisodeStatusEnum.IGNORED.equals(status)) {
            return 1000000;
        }

        // 如果是标题解析失败的，那么优先级最高，需要人工参与
        if (RssBangumiEpisodeStatusEnum.TITLE_PARSE_FAILED.equals(status)) {
            return -10000;
        }

        // 剩下的按剧集倒序排
        return Objects.nonNull(episode.getEpisodeNo()) ? -episode.getEpisodeNo(): 0;
    }

    @Override
    public RssBangumiEpisodesResp getRssBangumiEpisodes(Long id) {
        getRssBangumiOrThrow(id);
        List<RssBangumiEpisodeDto> episodes = hbRssBangumiEpisodeRepository.findAllByRssBangumiId(id)
                .stream()
                .sorted((e1, e2) -> {
                    int e1SortValue = calEpisodeSortValue(e1);
                    int e2SortValue = calEpisodeSortValue(e2);
                    return e1SortValue - e2SortValue;
                })
                .map(rssBangumiEpisodeConvertor::toRssBangumiEpisodeDto)
                .toList();

        return RssBangumiEpisodesResp
                .builder()
                .episodes(episodes)
                .build();
    }

    @Override
    @Transactional
    public RssBangumiDetailResp updateRssBangumi(Long id, RssBangumiUpdateReq req) {
        HbRssBangumi hbRssBangumi = getNotArchivedRssBangumiOrThrow(id);
        rssBangumiConvertor.updateRssBangumi(hbRssBangumi, req);
        hbRssBangumiRepository.save(hbRssBangumi);

        HbBangumi hbBangumi = hbBangumiRepository.findById(hbRssBangumi.getBangumiId())
                .orElse(null);
        if (Objects.nonNull(hbBangumi)) {
            rssBangumiConvertor.updateBangumi(hbBangumi, req.getBangumiInfo());
            hbBangumiRepository.save(hbBangumi);
        }


        BangumiInfoDto bangumiInfo = Optional.ofNullable(hbBangumi)
                .map(rssBangumiConvertor::toBangumiInfoDto)
                .orElse(null);
        return rssBangumiConvertor.toRssBangumiDetailResp(hbRssBangumi, bangumiInfo);
    }

    @Override
    public synchronized Long reparseRssBangumiEpisodes(Long id) {
        HbRssBangumi hbRssBangumi = getNotArchivedRssBangumiOrThrow(id);
        self().beforeReparseRssBangumiEpisodes(hbRssBangumi);

        HbBangumi hbBangumi = hbBangumiRepository.findById(hbRssBangumi.getBangumiId())
                .orElse(null);
        Integer season = null;
        if (Objects.nonNull(hbBangumi)) {
            season = hbBangumi.getSeason();
        }

        RssBangumiParseConfig parseConfig = RssBangumiParseConfig
                .builder()
                .rssCategory(RssCategoryEnum.of(hbRssBangumi.getRssCategory()))
                .rssLink(hbRssBangumi.getRssLink())
                .season(season)
                .episodeOffset(hbRssBangumi.getEpisodeOffset())
                .filteredOutRules(GsonUtils.toList(hbRssBangumi.getFilterRules(), String.class))
                .episodeTitleRenameMethod(EpisodeTitleRenameMethodEnum.of(hbRssBangumi.getEpisodeTitleRenameMethod()))
                .customizeRenamedEpisodeTitleFormat(hbRssBangumi.getCustomizeRenamedEpisodeTitleFormat())
                .build();
        RssBangumiPreviewInfo previewInfo = rssBangumiParseService.parse(parseConfig);

        self().afterReparseRssBangumiEpisodes(hbRssBangumi, hbBangumi, previewInfo);
        return id;
    }

    /**
     * 在重新解析之前
     * */
    @Transactional
    public void beforeReparseRssBangumiEpisodes(HbRssBangumi hbRssBangumi) {
        // 如果处于active状态，那么更新为inactive
        if (RssBangumiStatusEnum.ACTIVE.equals(hbRssBangumi.getStatus())) {
            hbRssBangumi.setStatus(RssBangumiStatusEnum.INACTIVE.getStatus());
            hbRssBangumiRepository.save(hbRssBangumi);
        }

        // 删除所有的剧集信息
        // 重新解析时，默认删除所有下载好的文件
        deleteAllRssBangumiEpisodesAndNotCheckRssBangumi(hbRssBangumi.getId(), true);
    }

    /**
     * 在重新解析之后，进行更新
     * */
    @Transactional
    public void afterReparseRssBangumiEpisodes(HbRssBangumi hbRssBangumi, HbBangumi hbBangumi, RssBangumiPreviewInfo previewInfo) {
        // 如果之前没有解析出rssBangumi, 那么插入
        if (Objects.isNull(hbBangumi)) {
            hbBangumi = rssBangumiConvertor.toHbBangumi(previewInfo);
            hbBangumiRepository.save(hbBangumi);

            hbRssBangumi.setBangumiId(hbBangumi.getId());
            hbRssBangumiRepository.save(hbRssBangumi);
        }

        final String bangumiTitle = hbBangumi.getTitle();
        QbittorrentDownloaderSettings qbittorrentDownloaderSettings = downloaderSettingsService.getQbittorrentDownloaderSettings();
        Path downloadDir = Paths.get(qbittorrentDownloaderSettings.downloadDir());
        List<HbRssBangumiEpisode> episodeList = previewInfo.episodes()
                .stream()
                .map(episodePreviewInfo -> rssBangumiComponent.toHbRssBangumiEpisode(hbRssBangumi.getId(), bangumiTitle, episodePreviewInfo, downloadDir,
                        hbRssBangumi.getDownloaderCategory())).toList();
        hbRssBangumiEpisodeRepository.saveAll(episodeList);
    }

    @Override
    @Transactional
    public void deleteAllRssBangumiEpisodes(Long id, boolean deleteFile) {
        getNotArchivedRssBangumiOrThrow(id);
        deleteAllRssBangumiEpisodesAndNotCheckRssBangumi(id, deleteFile);
    }

    private void deleteAllRssBangumiEpisodesAndNotCheckRssBangumi(Long id, boolean deleteFile) {
        List<HbRssBangumiEpisode> episodes = hbRssBangumiEpisodeRepository.findAllByRssBangumiId(id);
        if (CollectionUtils.isEmpty(episodes)) {
            return;
        }

        Path torrentDir = episodes.stream()
                .filter(episode -> StringUtils.isNotBlank(episode.getTorrentStoredPath()))
                .findFirst()
                .map(episode -> {
                    Path path = Paths.get(episode.getTorrentStoredPath());
                    return path.getParent();
                })
                .orElse(null);

        episodes.forEach(episode -> deleteRssBangumiEpisode(episode, deleteFile));

        if (Objects.nonNull(torrentDir)) {
            try {
                // 删除父目录
                Files.deleteIfExists(torrentDir);
            } catch (Exception e) {
                log.warn("[RssBangumiService]delete bangumi torrent dir failed, torrent dir path: {}", torrentDir, e);
            }
        }

    }

    @Override
    @Transactional
    public void deleteEpisode(Long id, Long episodeId, boolean deleteFile) {
        getNotArchivedRssBangumiOrThrow(id);
        HbRssBangumiEpisode hbRssBangumiEpisode = rssBangumiComponent.getRssBangumiEpisodeOrThrow(id, episodeId);
        deleteRssBangumiEpisode(hbRssBangumiEpisode, deleteFile);
    }

    private void deleteRssBangumiEpisode(HbRssBangumiEpisode episode, boolean deleteFile) {
        deleteFromDownloader(episode, deleteFile);
        deleteTorrent(episode);
        hbRssBangumiEpisodeRepository.deleteById(episode.getId());
    }

    /**
     * 删除种子文件
     * */
    private void deleteTorrent(HbRssBangumiEpisode episode) {
        if (StringUtils.isBlank(episode.getTorrentStoredPath())) {
            return;
        }

        try {
            Path path = Paths.get(episode.getTorrentStoredPath());
            Files.deleteIfExists(path);
        } catch (Exception e) {
            log.warn("[RssBangumiService]delete torrent failed, torrent path: {}", episode.getTorrentStoredPath(), e);
        }

    }

    private void deleteFromDownloader(HbRssBangumiEpisode episode, boolean deleteFile) {
        RssBangumiEpisodeStatusEnum status = RssBangumiEpisodeStatusEnum.of(episode.getStatus());

        // 删除下载器中的记录
        switch (status) {
            case EPISODE_DOWNLOADING:
            case EPISODE_DOWNLOAD_FAILED:
            case EPISODE_DOWNLOAD_FINISHED:
            case RENAMING:
            case RENAME_FAILED:
            case RENAME_FINISHED:
            case FINISHED:
                episodeDownloadService.deleteFromDownloader(DownloaderCategoryEnum.of(episode.getDownloaderCategory()), episode.getTorrentIdentity(), deleteFile);
        }
    }

    @Override
    public void onEpisodeRenameFinished(Long episodeId) {
        Optional<HbRssBangumiEpisode> episodeOptional = hbRssBangumiEpisodeRepository.findById(episodeId);
        if (episodeOptional.isEmpty()) {
            return;
        }

        HbRssBangumiEpisode episode = episodeOptional.get();
        if (!RssBangumiEpisodeStatusEnum.RENAME_FINISHED.equals(episode.getStatus())) {
            return;
        }

        episode.setStatus(RssBangumiEpisodeStatusEnum.FINISHED.getStatus());
        hbRssBangumiEpisodeRepository.save(episode);

        hbEventBus.publishEvent(new EpisodeFinishedEvent(episodeId));
    }

    @Override
    public PagedResp<PagedRssbBangumiItemDto> loadPagedRssBangumis(PagedReq<LoadPagedRssbangumisReqConditionDto> req) {
        QHbRssBangumi qHbRssBangumi = QHbRssBangumi.hbRssBangumi;

        JPAQuery<HbRssBangumi> query = jpaQueryFactory.select(qHbRssBangumi)
                .from(qHbRssBangumi);

        LoadPagedRssbangumisReqConditionDto condition = req.getCondition();


        BooleanExpression where = null;
        // 如果同时查询番剧标题和链接名，这里需要用or
        if (StringUtils.isNotBlank(condition.getBangumiTitle()) && StringUtils.isNotBlank(condition.getRssName())) {
            QHbBangumi qHbBangumi = QHbBangumi.hbBangumi;
            query.leftJoin(qHbBangumi)
                    .on(qHbRssBangumi.bangumiId.eq(qHbBangumi.id));
            where = JpaUtils.and(where, qHbBangumi.title.like(STR."%\{condition.getBangumiTitle()}%")
                    .or(qHbRssBangumi.rssName.like(STR."%\{condition.getBangumiTitle()}%")));
        } else {
            // 如果需要查询标题，那么进行连表
            if (StringUtils.isNotBlank(condition.getBangumiTitle())) {
                QHbBangumi qHbBangumi = QHbBangumi.hbBangumi;
                query.leftJoin(qHbBangumi)
                        .on(qHbRssBangumi.bangumiId.eq(qHbBangumi.id))
                        .where();
                where = JpaUtils.and(where, qHbBangumi.title.like(STR."%\{condition.getBangumiTitle()}%"));
            }
            else if (StringUtils.isNotBlank(condition.getRssName())) {
                where = JpaUtils.and(where, qHbRssBangumi.rssName.like(STR."%\{condition.getBangumiTitle()}%"));
            }
        }


        if (Objects.nonNull(condition.getRssCategory())) {
            where = JpaUtils.and(where, qHbRssBangumi.rssCategory.eq(condition.getRssCategory()));
        }

        if (Objects.nonNull(condition.getHandleMethod())) {
            where = JpaUtils.and(where, qHbRssBangumi.handleMethod.eq(condition.getHandleMethod()));
        }

        // 如果指定了状态，那么返回指定状态的番剧信息
        // 如果没有指定，那么只返回INACTIVE、ACTIVE状态的
        if (Objects.nonNull(condition.getStatus())) {
            where = JpaUtils.and(where, qHbRssBangumi.status.eq(condition.getStatus()));
        } else {
            where = JpaUtils.and(where, qHbRssBangumi.status.in(RssBangumiStatusEnum.INACTIVE.getStatus(), RssBangumiStatusEnum.ACTIVE.getStatus()));
        }

        if (Objects.nonNull(where)) {
            query.where(where);
        }

        Pageable pageable = PageRequestUtils.toPageable(req);
        query.orderBy(qHbRssBangumi.createdTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        QueryResults<HbRssBangumi> results = query.fetchResults();
        List<PagedRssbBangumiItemDto> pagedRssbBangumiItemDtoList = results.getResults().stream().map(this::buildPagedRssbBangumiItemDto).toList();

        return PagedResp.<PagedRssbBangumiItemDto>create()
                .setCurrent(req.getCurrent())
                .setPageSize(req.getPageSize())
                .setTotal(results.getTotal())
                .setList(pagedRssbBangumiItemDtoList);
    }

    private PagedRssbBangumiItemDto buildPagedRssbBangumiItemDto(HbRssBangumi rssBangumi) {
        BangumiInfoDto bangumiInfo = hbBangumiRepository.findById(rssBangumi.getBangumiId())
                .map(rssBangumiConvertor::toBangumiInfoDto)
                .orElse(null);

        return rssBangumiConvertor.toPagedRssbBangumiItemDto(rssBangumi, bangumiInfo);
    }

    @Override
    @Transactional
    public void activeRssBangumi(Long id) {
        HbRssBangumi hbRssBangumi = getNotArchivedRssBangumiOrThrow(id);
        // 如果处于active状态，那么直接返回
        if (RssBangumiStatusEnum.ACTIVE.equals(hbRssBangumi.getStatus())) {
            return;
        }
        hbRssBangumi.setStatus(RssBangumiStatusEnum.ACTIVE.getStatus());
        hbRssBangumiRepository.save(hbRssBangumi);
        episodeDownloadService.pushToDownloader(id);
    }

    @Override
    public void inactiveRssBangumi(Long id) {
        HbRssBangumi hbRssBangumi = getNotArchivedRssBangumiOrThrow(id);
        // 如果处于inactive状态，那么直接返回
        if (RssBangumiStatusEnum.INACTIVE.equals(hbRssBangumi.getStatus())) {
            return;
        }
        hbRssBangumi.setStatus(RssBangumiStatusEnum.INACTIVE.getStatus());
        hbRssBangumiRepository.save(hbRssBangumi);
    }

    @Override
    @Transactional
    public void deleteRssBangumi(Long id, boolean deleteFile) {
        HbRssBangumi hbRssBangumi = getRssBangumiOrThrow(id);
        if (Objects.nonNull(hbRssBangumi.getBangumiId())) {
            deleteBangumi(hbRssBangumi.getBangumiId());
        }
        deleteAllRssBangumiEpisodesAndNotCheckRssBangumi(hbRssBangumi.getId(), deleteFile);
        hbRssBangumiRepository.deleteById(id);
    }

    /**
     * 删除bangumi信息
     * */
    private void deleteBangumi(Long bangumiId) {
        HbBangumi hbBangumi = hbBangumiRepository.findById(bangumiId).orElse(null);
        if (Objects.isNull(hbBangumi)) {
            return;
        }
        if (StringUtils.isBlank(hbBangumi.getPosterStoredPath())) {
            return;
        }

        try {
            Path path = Paths.get(hbBangumi.getPosterStoredPath());
            Files.deleteIfExists(path);
            // 同时删除父目录
            Files.deleteIfExists(path.getParent());
        } catch (Exception e) {
            log.warn("[RssBangumiService]delete bangumi poster failed, poster path: {}", hbBangumi.getPosterStoredPath(), e);
        }
        hbBangumiRepository.deleteById(bangumiId);
    }

    @Override
    @Transactional
    public void archiveRssBangumi(Long id, boolean deleteFile) {
        HbRssBangumi hbRssBangumi = getNotArchivedRssBangumiOrThrow(id);
        hbRssBangumiEpisodeRepository.findAllByRssBangumiId(id)
                .forEach(episode -> {
                    deleteFromDownloader(episode, deleteFile);
                    deleteTorrent(episode);
                    episode.setStatus(RssBangumiEpisodeStatusEnum.ARCHIVED.getStatus());
                    hbRssBangumiEpisodeRepository.save(episode);
                });
        hbRssBangumi.setStatus(RssBangumiStatusEnum.ARCHIVED.getStatus());
        hbRssBangumiRepository.save(hbRssBangumi);
    }

    @Override
    public Long incrementalParseBangumiEpisodes(Long id) {
        getNotArchivedRssBangumiOrThrow(id);
        rssSubscriptionUpdateService.updateRssSubscription(id);
        return id;
    }

    @Override
    public void pushToDownloader(Long id) {
        getNotArchivedRssBangumiOrThrow(id);
        episodeDownloadService.pushToDownloader(id);
    }

    @Override
    @Transactional
    public void renameEpisodeFileName(Long id, Long episodeId, RenameEpisodeFileNameReq req) {
        getNotArchivedRssBangumiOrThrow(id);
        HbRssBangumiEpisode rssBangumiEpisode = rssBangumiComponent.getRssBangumiEpisodeOrThrow(id, episodeId);

        Integer status = rssBangumiEpisode.getStatus();
        // 下载中或者下载完成后，允许手动重命名
        if (!RssBangumiEpisodeStatusEnum.FINISHED.equals(status)
            && !RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOADING.equals(status)) {
            throw new HbBizException(HbCodeEnum.RSS_BANGUMI_EPISODE_STATUS_NOT_ALLOW_RENAME);
        }

        String oldRenamedEpisodeStoredPath = rssBangumiEpisode.getRenamedEpisodeStoredPath();

        Path newRenamedEpisodeStoredPath = Paths.get(oldRenamedEpisodeStoredPath).getParent().resolve(req.getNewFileName());
        rssBangumiEpisode.setRenamedEpisodeFileName(req.getNewFileName());
        // 强制转换为unix系统的路径
        rssBangumiEpisode.setRenamedEpisodeStoredPath(EpisodeDownloadAdapter.getPathInDownloader(newRenamedEpisodeStoredPath.toString()));

        // 如果下载完成，那么同步进行文件重命名
        if (RssBangumiEpisodeStatusEnum.FINISHED.equals(status)) {
            boolean renameResult = episodeDownloadService.renameFinishedEpisodeFileName(rssBangumiEpisode.getDownloaderCategory(), rssBangumiEpisode.getTorrentIdentity(),
                    oldRenamedEpisodeStoredPath, rssBangumiEpisode.getRenamedEpisodeStoredPath());
            if (!renameResult) {
                throw new HbBizException(HbCodeEnum.RSS_BANGUMI_EPISODE_RENAME_FAILED);
            }
        }


        hbRssBangumiEpisodeRepository.save(rssBangumiEpisode);
    }

    @Override
    @Transactional
    public void ignoreEpisode(Long id, Long episodeId, boolean deleteFile) {
        getNotArchivedRssBangumiOrThrow(id);
        HbRssBangumiEpisode rssBangumiEpisode = rssBangumiComponent.getRssBangumiEpisodeOrThrow(id, episodeId);
        Integer status = rssBangumiEpisode.getStatus();
        if (RssBangumiEpisodeStatusEnum.IGNORED.equals(status)) {
            return;
        }

        // 忽略剧集时，尝试从下载器中移除、删除种子文件
        deleteFromDownloader(rssBangumiEpisode, deleteFile);
        deleteTorrent(rssBangumiEpisode);
        rssBangumiEpisode.setStatus(RssBangumiEpisodeStatusEnum.IGNORED.getStatus());
        
        hbRssBangumiEpisodeRepository.save(rssBangumiEpisode);
    }

    @Override
    public void manualParseEpisode(Long id, Long episodeId, ManualParseEpisodeReq req) {
        HbRssBangumi rssBangumi = getNotArchivedRssBangumiOrThrow(id);
        HbRssBangumiEpisode rssBangumiEpisode = rssBangumiComponent.getRssBangumiEpisodeOrThrow(id, episodeId);

        Integer status = rssBangumiEpisode.getStatus();
        if (!RssBangumiEpisodeStatusEnum.PARSED.equals(status)
                && !RssBangumiEpisodeStatusEnum.TITLE_PARSE_FAILED.equals(status)
                && !RssBangumiEpisodeStatusEnum.RENAME_FAILED.equals(status)) {
            throw new HbBizException(HbCodeEnum.RSS_BANGUMI_EPISODE_STATUS_NOT_ALLOW_MANUAL_PARSE);
        }


        Integer episodeNo = req.getEpisodeNo();
        rssBangumiEpisode.setEpisodeNo(episodeNo);

        String renamedEpisodeStoredPath = rssBangumiEpisode.getRenamedEpisodeStoredPath();
        if (StringUtils.isEmpty(renamedEpisodeStoredPath)) {
            renamedEpisodeStoredPath = rssBangumiEpisode.getEpisodeStoredPath();
        }

        Path newRenamedEpisodeStoredPath = Paths.get(renamedEpisodeStoredPath).getParent().resolve(req.getRenamedEpisodeFileName());
        rssBangumiEpisode.setRenamedEpisodeFileName(req.getRenamedEpisodeFileName());
        // 强制转换为unix系统的路径
        rssBangumiEpisode.setRenamedEpisodeStoredPath(EpisodeDownloadAdapter.getPathInDownloader(newRenamedEpisodeStoredPath.toString()));

        if (RssBangumiEpisodeStatusEnum.TITLE_PARSE_FAILED.equals(status)) {
            rssBangumiEpisode.setStatus(RssBangumiEpisodeStatusEnum.PARSED.getStatus());
            rssBangumiEpisode.setErrorMessage("");
        } else if (RssBangumiEpisodeStatusEnum.RENAME_FAILED.equals(status)) {
            rssBangumiEpisode.setStatus(RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOAD_FINISHED.getStatus());
            rssBangumiEpisode.setErrorMessage("");
        }

        hbRssBangumiEpisodeRepository.save(rssBangumiEpisode);

        if (RssBangumiEpisodeStatusEnum.RENAME_FAILED.equals(status)) {

            // 如果之前是重命名失败的状态，那么发布一次下载成功事件，去尝试重命名
            hbEventBus.publishEvent(new EpisodeDownloadFinishedEvent(episodeId));
        } else if (RssBangumiEpisodeStatusEnum.TITLE_PARSE_FAILED.equals(status)) {
            // 如果是标题解析失败状态，且rss链接处于激活状态，那么触发一次下载
            if (RssBangumiStatusEnum.ACTIVE.equals(rssBangumi.getStatus())) {
                RssSubscriptionUpdatedEvent updatedEvent = new RssSubscriptionUpdatedEvent(rssBangumi.getId());
                hbEventBus.publishEvent(updatedEvent);
            }
        }
    }


    @Override
    public RssBangumiStatisticOnHomeResp statisticOnHome() {
        return statisticComponent.statisticOnHome();
    }

    @Override
    public UpdatedBangumisResp getUpdatedBangumisForWeek() {
        return statisticComponent.getUpdatedBangumisForWeek();
    }
}































