package top.rainine.homebangumi.core.rss.data;

import lombok.*;
import top.rainine.homebangumi.def.enums.RssCategoryEnum;

import java.util.List;

/**
 * @param rssCategory        rss分类
 * @param title              番剧标题
 * @param episodes           剧集列表
 * @author rainine
 * @description 解析rss后得到的数据
 * @date 2024/3/14 18:54:39
 */
@Builder
public record RssBangumiParsedInfo(RssCategoryEnum rssCategory,
                                   String title,
                                   List<RssBangumiEpisodeParsedInfo> episodes) {

}
