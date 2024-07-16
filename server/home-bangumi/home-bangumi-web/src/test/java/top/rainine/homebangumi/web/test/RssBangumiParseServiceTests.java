package top.rainine.homebangumi.web.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.rainine.homebangumi.common.utils.GsonUtils;
import top.rainine.homebangumi.core.rss.RssBangumiParseService;
import top.rainine.homebangumi.core.rss.data.RssBangumiPreviewInfo;
import top.rainine.homebangumi.def.enums.RssCategoryEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author rainine
 * @description
 * @date 2024/3/19 12:00:18
 */
public class RssBangumiParseServiceTests extends AbstractTests {

    private final RssBangumiParseService parseService;

    @Autowired
    public RssBangumiParseServiceTests(RssBangumiParseService parseService) {
        this.parseService = parseService;
    }

    @Test
    public void testParse() {
        //String rssLink = "https://mikanime.tv/RSS/Bangumi?bangumiId=3255&subgroupid=370";
        //String rssLink = "https://mikanime.tv/RSS/Bangumi?bangumiId=3240&subgroupid=203";
        String rssLink = "https://mikanime.tv/RSS/Bangumi?bangumiId=2961&subgroupid=382";
        List<String> filteredOutRules = Arrays.asList("繁日双语", "简繁内封", "720p", "720P", "[01-12]");
//        RssBangumiPreviewInfo previewInfo = parseService.parse(RssCategoryEnum.MIKAN, rssLink, 1, 3, filteredOutRules);
//        System.out.println(GsonUtils.GSON.toJson(previewInfo));
    }
}
