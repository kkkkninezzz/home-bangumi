package top.rainine.homebangumi.core.rss;

import top.rainine.homebangumi.core.rss.data.RssBangumiParsedInfo;
import top.rainine.homebangumi.def.enums.RssCategoryEnum;

import java.util.List;

/**
 * @authoer rainine
 * @date 2024/3/17 14:05
 * @desc rss链接解析
 */
public interface BangumiRssLinkParser {

    /**
     * 获取支持的rss类型
     * */
    RssCategoryEnum getCategory();

    /**
     * 解析rss数据
     * @param rssLink rss链接
     * @return rss番剧信息，一条rssk链接可能解析出多个番剧
     * */
    List<RssBangumiParsedInfo> parse(String rssLink);
}
