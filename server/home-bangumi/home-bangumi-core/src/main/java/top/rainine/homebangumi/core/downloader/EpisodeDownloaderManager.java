package top.rainine.homebangumi.core.downloader;

import top.rainine.homebangumi.def.enums.DownloaderCategoryEnum;

/**
 * @authoer rainine
 * @date 2024/5/10 23:35
 * @desc 下载器管理
 */
public interface EpisodeDownloaderManager {

    /**
     * 根据{@link DownloaderCategoryEnum}获取对应的下载适配器
     * @throws top.rainine.homebangumi.def.exception.HbBizException 如果没有对应的类别的下载器，那么抛出异常
     * */
    EpisodeDownloadAdapter getDownloaderAdapter(DownloaderCategoryEnum downloaderCategory);
}
