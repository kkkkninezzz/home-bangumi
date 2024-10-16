package top.rainine.homebangumi.core.rss.data;

import lombok.Builder;
import top.rainine.homebangumi.core.common.episoderenamer.EpisodeTitleRenameAdapter;
import top.rainine.homebangumi.def.enums.RssCategoryEnum;

import java.util.List;

/**
 * @param rssCategory           rss分类
 * @param rssLink               rss链接
 * @param season                手动指定的季数，如果小于0，则尝试解析
 * @param episodeOffset         剧集的偏移量，如果设置为1，表示从第2集开始，忽略第二集之前的
 * @param filteredOutRules      过滤掉的规则，将忽略掉符合该规则的剧集信息，根据原始的剧集名进行过滤，而非种子名
 * @param parsedTorrentLinks    已经解析过的种子链接
 * @param episodeTitleRenameAdapter 剧集重命名适配器
 *
 * @author rainine
 * @description rss番剧增量解析配置
 * @date 2024/6/25 18:38:09
 */
@Builder
public record RssBangumiIncrementalParseConfig(RssCategoryEnum rssCategory,
                                               String rssLink,
                                               Integer season,
                                               Integer episodeOffset,
                                               List<String> filteredOutRules,
                                               List<String> parsedTorrentLinks,
                                               EpisodeTitleRenameAdapter episodeTitleRenameAdapter) {
}
