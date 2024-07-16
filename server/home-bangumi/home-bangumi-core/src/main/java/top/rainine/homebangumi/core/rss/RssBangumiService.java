package top.rainine.homebangumi.core.rss;

import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;
import top.rainine.homebangumi.dao.po.HbRssBangumiEpisode;

/**
 * @author rainine
 * @description rss bangumi对外的服务类
 * @date 2024/4/8 18:22:58
 */
public interface RssBangumiService {

    /**
     * 预览并创建rss bangumi
     * */
    PreviewRssBangumiResp previewAndCreateRssBangumi(PreviewRssBangumiReq req);

    /**
     * 获取rss bangumi的详情
     * */
    RssBangumiDetailResp getRssBangumiDetail(Long id);

    /**
     * 获取rss bangumi的剧集信息
     * */
    RssBangumiEpisodesResp getRssBangumiEpisodes(Long id);

    /**
     * 更新rss bangumi
     * */
    RssBangumiDetailResp updateRssBangumi(Long id, RssBangumiUpdateReq req);

    /**
     * 重新解析剧集信息
     * @return 成功后，返回rssBnagumi id
     * */
    Long reparseRssBangumiEpisodes(Long id);

    /**
     * 增量解析剧集信息
     * @return 成功后，返回rssBnagumi id
     * */
    Long incrementalParseBangumiEpisodes(Long id);

    /**
     * 删除所有的rssBangumi剧集信息
     * */
    void deleteAllRssBangumiEpisodes(Long id, boolean deleteFile);

    /**
     * 删除剧集信息
     * @param id rssBangumi id
     * @param episodeId 剧集id
     * */
    void deleteEpisode(Long id, Long episodeId, boolean deleteFile);

    /**
     * 当剧集重命名结束
     * @param episodeId 剧集id {@link HbRssBangumiEpisode#getId()}
     * */
    void onEpisodeRenameFinished(Long episodeId);

    /**
     * 获取分页的数据
     * */
    PagedResp<PagedRssbBangumiItemDto> loadPagedRssBangumis(PagedReq<LoadPagedRssbangumisReqConditionDto> req);

    /**
     * 启用rssbangumi
     * */
    void activeRssBangumi(Long id);

    /**
     * 禁用rssbangumi
     * */
    void inactiveRssBangumi(Long id);

    /**
     * 删除rssbangumi
     * */
    void deleteRssBangumi(Long id, boolean deleteFile);

    /**
     * 归档rssbangumi
     * */
    void archiveRssBangumi(Long id, boolean deleteFile);

    /**
     * 将当前番剧符合条件的数据都推送到下载器中
     * */
    void pushToDownloader(Long id);

    /**
     * 更新剧集的刮削标题
     * */
    void renameEpisodeFileName(Long id, Long episodeId, RenameEpisodeFileNameReq req);

    /**
     * 忽略剧集
     * */
    void ignoreEpisode(Long id, Long episodeId, boolean deleteFile);

    /**
     * 手动解析剧集
     * */
    void manualParseEpisode(Long id, Long episodeId, ManualParseEpisodeReq req);

    /**
     * 在首页的统计数据
     * */
    RssBangumiStatisticOnHomeResp statisticOnHome();

    /**
     * 获取一周内有更新的番剧
     * */
    UpdatedBangumisResp getUpdatedBangumisForWeek();
}
