package top.rainine.homebangumi.web.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.rainine.homebangumi.core.rss.data.RssBangumiParsedInfo;
import top.rainine.homebangumi.core.rss.mikan.MikanBangumiRssLinkParser;

import java.util.List;

/**
 * @authoer rainine
 * @date 2024/3/17 20:56
 * @desc
 */

public class MikanBangumiRssLinkParserTests extends AbstractTests {

    private final MikanBangumiRssLinkParser mikanRssBangumiParser;


    @Autowired
    public MikanBangumiRssLinkParserTests(MikanBangumiRssLinkParser mikanRssBangumiParser) {
        this.mikanRssBangumiParser = mikanRssBangumiParser;
    }

    @Test
    public void testDownloadRssAndParse() {
        List<RssBangumiParsedInfo> rssBangumiParsedInfoList = mikanRssBangumiParser.parse("https://mikanime.tv/RSS/Bangumi?bangumiId=3255&subgroupid=370");
        System.out.println(rssBangumiParsedInfoList);
    }
}
