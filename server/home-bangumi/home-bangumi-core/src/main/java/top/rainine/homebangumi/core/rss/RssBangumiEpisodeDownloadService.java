package top.rainine.homebangumi.core.rss;

import top.rainine.homebangumi.dao.po.HbRssBangumi;
import top.rainine.homebangumi.dao.po.HbRssBangumiEpisode;
import top.rainine.homebangumi.def.enums.DownloaderCategoryEnum;

/**
 * @authoer rainine
 * @date 2024/4/21 23:09
 * @desc rss bangumi下载的服务类
 */
public interface RssBangumiEpisodeDownloadService {

    /**
     * 将所有符合的番剧数据推送到下载器中
     * */
    void pushAllToDownloader();

    /**
     * 将指定番剧信息推送至下载器中
     * @param rssBangumiId id {@link HbRssBangumi#getId()}
     * */
    void pushToDownloader(Long rssBangumiId);

    /**
     * 从下载器中删除下载数据
     * @param torrentIdentity 种子的唯一标识
     * @param deleteFile 是否要删除文件
     * */
    void deleteFromDownloader(DownloaderCategoryEnum downloaderCategory, String torrentIdentity, boolean deleteFile);

    /**
     * 检查所有的剧集状态
     * */
    void checkAllEpisodes();

    /**
     * 重命名剧集
     * @param episodeId 剧集id {@link HbRssBangumiEpisode#getId()}
     * */
    void renameEpisode(Long episodeId);

    /**
     * 重命名所有符合条件的剧集
     * */
    void renameAllEpisodes();

    /**
     * 重命名完成的剧集
     * */
    boolean renameFinishedEpisodeFileName(Integer downloaderCategory, String torrentIdentity, String oldStoredPath, String newStoredPath);
}
