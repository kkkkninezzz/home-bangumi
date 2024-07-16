package top.rainine.homebangumi.web.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.rainine.homebangumi.core.downloader.qbittorrent.QbittorrentEpisodeDownloadAdapter;

/**
 * @authoer rainine
 * @date 2024/4/28 00:18
 * @desc
 */
@Slf4j
public class QbittorrentEpisodeDownloadAdapterTests extends AbstractTests {

    private final QbittorrentEpisodeDownloadAdapter qbittorrentEpisodeDownloadAdapter;

    @Autowired
    public QbittorrentEpisodeDownloadAdapterTests(QbittorrentEpisodeDownloadAdapter qbittorrentEpisodeDownloadAdapter) {
        this.qbittorrentEpisodeDownloadAdapter = qbittorrentEpisodeDownloadAdapter;
    }

    /**
     * 登录接口
     * */
    @Test
    public void testAuth() {
        log.info("[QbittorrentEpisodeDownloadAdapterTests]auth: {}", qbittorrentEpisodeDownloadAdapter.auth());
        //log.info("[QbittorrentEpisodeDownloadAdapterTests]isCookieValid: {}", qbittorrentEpisodeDownloadAdapter.isCookieValid());
    }
}
