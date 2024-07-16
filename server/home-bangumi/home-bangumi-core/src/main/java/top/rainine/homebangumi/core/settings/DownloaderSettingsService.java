package top.rainine.homebangumi.core.settings;

import top.rainine.homebangumi.core.settings.data.QbittorrentDownloaderSettings;

/**
 * @authoer rainine
 * @date 2024/4/26 00:04
 * @desc 下载器配置服务类
 */
public interface DownloaderSettingsService {

    /**
     * 获取qbittorrent下载器配置
     * */
    QbittorrentDownloaderSettings getQbittorrentDownloaderSettings();

    /**
     * 更新qbittorrent的下载器配置
     * */
    void updateQbittorrentDownloaderSettings(QbittorrentDownloaderSettings settings);
}
