package top.rainine.homebangumi.core.settings.data;

import lombok.Builder;

/**
 * @param baseUrl  下载器的基础url
 * @param username 用户名
 * @param password 密码
 * @param downloadDir 在下载器的目录
 *
 * @authoer rainine
 * @date 2024/4/26 00:05
 * @desc qbittorrent下载配置
 */
@Builder
public record QbittorrentDownloaderSettings(String baseUrl, String username, String password, String downloadDir) {

}
