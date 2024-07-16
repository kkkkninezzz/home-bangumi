package top.rainine.homebangumi.core.rss.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.common.utils.GsonUtils;
import top.rainine.homebangumi.core.downloader.EpisodeDownloadAdapter;
import top.rainine.homebangumi.core.rss.data.RssBangumiEpisodePreviewInfo;
import top.rainine.homebangumi.core.rss.data.convertor.RssBangumiEpisodeConvertor;
import top.rainine.homebangumi.dao.po.HbBangumi;
import top.rainine.homebangumi.dao.po.HbRssBangumi;
import top.rainine.homebangumi.dao.po.HbRssBangumiEpisode;
import top.rainine.homebangumi.dao.repository.HbBangumiRepository;
import top.rainine.homebangumi.dao.repository.HbRssBangumiEpisodeRepository;
import top.rainine.homebangumi.dao.repository.HbRssBangumiRepository;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.enums.RssBangumiStatusEnum;
import top.rainine.homebangumi.def.enums.RssHandleMethodEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @authoer rainine
 * @date 2024/4/23 23:55
 * @desc 公共逻辑
 */
@Component
@RequiredArgsConstructor
public class RssBangumiComponent {

    private final HbRssBangumiRepository rssBangumiRepository;

    private final HbBangumiRepository bangumiRepository;

    private final HbRssBangumiEpisodeRepository rssBangumiEpisodeRepository;

    private final RssBangumiEpisodeConvertor rssBangumiEpisodeConvertor;

    /**
     * 根据id获取rss bangumi
     * @param id id
     * @throws HbBizException 如果获取失败，抛出异常
     * */
    public HbRssBangumi getRssBangumiOrThrow(Long id) {
        return rssBangumiRepository.findById(id).orElseThrow(() -> new HbBizException(HbCodeEnum.RSS_BANGUMI_NOT_EXISTS));
    }


    /**
     * 根据id获取rss bangumi
     * @param id id
     * @throws HbBizException 如果获取失败，抛出异常
     * */
    public HbRssBangumi getNotArchivedRssBangumiOrThrow(Long id) {
        HbRssBangumi hbRssBangumi = getRssBangumiOrThrow(id);
        if (RssBangumiStatusEnum.ARCHIVED.equals(hbRssBangumi.getStatus())) {
            throw new HbBizException();
        }
        return hbRssBangumi;
    }

    /**
     * 根据rssBangumiId和id获取episode
     * @param rssBangumiId {@link HbRssBangumi#getId()}
     * @param episodeId 当前id
     * */
    public HbRssBangumiEpisode getRssBangumiEpisodeOrThrow(Long rssBangumiId, Long episodeId) {
        HbRssBangumiEpisode hbRssBangumiEpisode = rssBangumiEpisodeRepository.findById(episodeId).orElseThrow(() -> new HbBizException(HbCodeEnum.RSS_BANGUMI_EPISODE_NOT_EXISTS));
        if (!Objects.equals(rssBangumiId, hbRssBangumiEpisode.getRssBangumiId())) {
            throw new HbBizException(HbCodeEnum.RSS_BANGUMI_EPISODE_NOT_BELONG_TO_BANGUMI);
        }

        return hbRssBangumiEpisode;
    }

    /**
     * 根据d获取episode
     * @param episodeId 当前id
     * */
    public HbRssBangumiEpisode getRssBangumiEpisodeOrThrow(Long episodeId) {
        return rssBangumiEpisodeRepository.findById(episodeId).orElseThrow(() -> new HbBizException(HbCodeEnum.RSS_BANGUMI_EPISODE_NOT_EXISTS));
    }

    /**
     * 获取所有激活的订阅型番剧
     * */
    public List<HbRssBangumi> loadAllActiveSubscribeRssBangumis() {
        return rssBangumiRepository.findAllByStatusAndHandleMethod(RssBangumiStatusEnum.ACTIVE.getStatus(),
                RssHandleMethodEnum.SUBSCRIBE.getMethod());
    }

    /**
     * 根据id获取rss bangumi
     * @param bangumiId id
     * @throws HbBizException 如果获取失败，抛出异常
     * */
    public HbBangumi getBangumiOrThrow(Long bangumiId) {
        return bangumiRepository.findById(bangumiId).orElseThrow(() -> new HbBizException(HbCodeEnum.BANGUMI_NOT_EXISTS));
    }

    /**
     * 根据rssBangumId获取rss bangumi
     * @param rssBangumId id
     * @throws HbBizException 如果获取失败，抛出异常
     * */
    public HbBangumi getBangumiByRssBangumIdOrThrow(Long rssBangumId) {
        HbRssBangumi hbRssBangumi = getRssBangumiOrThrow(rssBangumId);
        return getBangumiOrThrow(hbRssBangumi.getBangumiId());
    }

    /**
     * 根据bangumiId获取数据库中存储的季度信息
     * @return 如果不存在返回null
     * */
    public Integer getSeason(Long bangumiId) {
        return bangumiRepository.findById(bangumiId).map(HbBangumi::getSeason).orElse(null);
    }

    /**
     * 根据bangumiId获取数据库中存储的番剧标题
     * @return 如果不存在返回 unknown
     * */
    public String getBangumiTitle(Long bangumiId) {
        return bangumiRepository.findById(bangumiId).map(HbBangumi::getTitle).orElse("unknown");
    }

    /**
     * 根据rssbangumiId获取番剧标题
     * */
    public String getBangumiTitleByRssBangumiId(Long rssBnagumiId) {
        return rssBangumiRepository.findById(rssBnagumiId).map(rssBangumi -> getBangumiTitle(rssBangumi.getBangumiId())).orElse("unknown");
    }

    /**
     * 将字符串过滤规则转换为列表
     * */
    public List<String> getFilteredOutRules(String filteredOutRulesStr) {
        if (StringUtils.isBlank(filteredOutRulesStr)) {
            return new ArrayList<>();
        }

        return GsonUtils.toList(filteredOutRulesStr, String.class);
    }


    /**
     * 转换为剧集存储对象
     * */
    public HbRssBangumiEpisode toHbRssBangumiEpisode(Long hbRssBangumiId, String bangumiTitle, RssBangumiEpisodePreviewInfo episodePreviewInfo, Path downloadDir,
                                                     Integer downloaderCategory) {
        HbRssBangumiEpisode hbRssBangumiEpisode = rssBangumiEpisodeConvertor.toHbRssBangumiEpisode(episodePreviewInfo);
        hbRssBangumiEpisode.setRssBangumiId(hbRssBangumiId);

        if (StringUtils.isNotBlank(hbRssBangumiEpisode.getEpisodeFileName())) {
            String episodeStoredPath = downloadDir.resolve(bangumiTitle).resolve(hbRssBangumiEpisode.getEpisodeFileName()).toString();
            // 强制转换为unix系统的路径
            hbRssBangumiEpisode.setEpisodeStoredPath(EpisodeDownloadAdapter.getPathInDownloader(episodeStoredPath));
        }

        if (StringUtils.isNotBlank(hbRssBangumiEpisode.getRenamedEpisodeFileName())) {
            String renamedEpisodeStoredPath = downloadDir.resolve(bangumiTitle).resolve(hbRssBangumiEpisode.getRenamedEpisodeFileName()).toString();
            hbRssBangumiEpisode.setRenamedEpisodeStoredPath(EpisodeDownloadAdapter.getPathInDownloader(renamedEpisodeStoredPath));
        }
        hbRssBangumiEpisode.setDownloaderCategory(downloaderCategory);
        return hbRssBangumiEpisode;
    }
}
