package top.rainine.homebangumi.core.downloader;

import org.apache.commons.io.FilenameUtils;
import top.rainine.homebangumi.core.downloader.data.TorrentDownloadStatusInfo;
import top.rainine.homebangumi.core.downloader.data.TorrentFileRenameResultInfo;
import top.rainine.homebangumi.def.enums.DownloaderCategoryEnum;

/**
 * @author rainine
 * @description 番剧下载适配器
 * @date 2024/3/14 17:46:36
 */
public interface EpisodeDownloadAdapter {

    /**
     * 获取支持的类型
     * */
    DownloaderCategoryEnum getCategory();

    /**
     * 发起与下载器的验证
     * @return 验证成功返回true
     * */
    boolean auth();

    /**
     * 添加一个种子到下载器中
     * @param torrentPath 种子的文件路径
     * @param savePath 下载后文件的保存路径
     * @param paused 添加种子后， 是否处于暂停状态
     * @return 如果成功添加，返回种子在下载器中的唯一标识。否则返回null
     * */
    String addTorrent(String torrentPath, String savePath, boolean paused);

    /**
     * 从下载器中移除种子
     * @param torrentIdentity 种子在下载器中定义的唯一标识
     * @param deleteFiles 是否要删除已经下载的文件
     * @return 移除成功返回true
     * */
    boolean removeTorrent(String torrentIdentity, boolean deleteFiles);

    /**
     * 获取种子的下载状态
     * @param torrentIdentity 种子在下载器中定义的唯一标识
     * */
    TorrentDownloadStatusInfo getTorrentDownloadStatus(String torrentIdentity);

    /**
     * 重命名种子下载后的文件
     * @param torrentIdentity 种子在下载器中定义的唯一标识
     * @param oldPath 文件所在的旧路径
     * @param newPath 重命名后的新路径
     * @return 重命名结果
     * */
    TorrentFileRenameResultInfo renameFile(String torrentIdentity, String oldPath, String newPath);

    /**
     * 获取在downloader中的路径，主要是兼容为hb在windows中时向linux容器中的qb下载路径问题
     * */
    static String getPathInDownloader(String path) {
        return FilenameUtils.separatorsToUnix(path);
    }
}
















