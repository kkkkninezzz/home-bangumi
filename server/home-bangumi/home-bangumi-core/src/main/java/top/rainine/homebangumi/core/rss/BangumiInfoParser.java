package top.rainine.homebangumi.core.rss;

import top.rainine.homebangumi.core.rss.data.BangumiParedInfo;
import top.rainine.homebangumi.core.rss.data.RssBangumiParsedInfo;
import top.rainine.homebangumi.def.enums.RssCategoryEnum;

import java.nio.file.Path;
import java.util.List;

/**
 * @author rainine
 * @description 番剧信息解析器
 * @date 2024/3/19 17:38:09
 */
public interface BangumiInfoParser {
    /**
     * 获取支持的rss类型
     * */
    RssCategoryEnum getCategory();

    /**
     * 解析rss数据
     * @param rssLink rss链接
     * @param posterStoredDirPath 番剧海报的存储路径
     * @return 番剧信息
     * */
    BangumiParedInfo parse(String rssLink, Path posterStoredDirPath);
}
