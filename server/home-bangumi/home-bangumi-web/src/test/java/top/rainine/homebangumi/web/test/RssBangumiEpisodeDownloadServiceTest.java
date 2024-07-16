package top.rainine.homebangumi.web.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.rainine.homebangumi.core.rss.RssBangumiEpisodeDownloadService;

/**
 * @authoer rainine
 * @date 2024/4/28 22:44
 * @desc
 */
public class RssBangumiEpisodeDownloadServiceTest extends AbstractTests {

    private final RssBangumiEpisodeDownloadService downloadService;

    @Autowired
    public RssBangumiEpisodeDownloadServiceTest(RssBangumiEpisodeDownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @Test
    public void testPush() {
        downloadService.pushToDownloader(1L);
    }
}
