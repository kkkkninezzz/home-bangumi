package top.rainine.homebangumi.core.rss.data;

import lombok.Builder;

/**
 * @param rawEpisodeTitle 原始的剧集名
 * @param torrentPubDate  种子的发布时间
 *                        毫秒时间戳
 * @param torrentLink     种子链接
 * @authoer rainine
 * @date 2024/3/17 21:11
 * @desc 番剧解析出来的剧集信息
 */
@Builder
public record RssBangumiEpisodeParsedInfo(String rawEpisodeTitle,
                                          Long torrentPubDate,
                                          String torrentLink) {

}
