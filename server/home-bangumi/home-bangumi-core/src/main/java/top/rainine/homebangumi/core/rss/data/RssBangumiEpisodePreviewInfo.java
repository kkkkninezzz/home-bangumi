package top.rainine.homebangumi.core.rss.data;

import lombok.Builder;
import top.rainine.homebangumi.def.enums.RssBangumiEpisodeStatusEnum;

/**
 * @param episodeNo                 剧集编号
 * @param season                    季
 * @param bangumiTitle              解析出来的番剧名
 * @param rawEpisodeTitle           原始的剧集名
 * @param episodeFileName           种子中获取到的剧集文件名
 * @param renamedEpisodeFileName    重命名后的剧集文件名
 * @param torrentPubDate            种子的发布时间，毫秒时间戳
 * @param torrentLink               种子链接
 * @param torrentStoredPath         种子存储的本地路径
 * @param status                    状态，通常只能为{@link RssBangumiEpisodeStatusEnum#PARSED}、
 *                                  {@link RssBangumiEpisodeStatusEnum#SKIPPED}、
 *                                  {@link RssBangumiEpisodeStatusEnum#FILTERED_OUT}、
 *                                  {@link RssBangumiEpisodeStatusEnum#ERROR}
 *
 * @authoer rainine
 * @date 2024/3/17 23:35
 * @desc 剧集的预览信息
 */
@Builder
public record RssBangumiEpisodePreviewInfo(int episodeNo,
                                           int season,
                                           String bangumiTitle,
                                           String rawEpisodeTitle,
                                           String episodeFileName,
                                           String renamedEpisodeFileName,
                                           Long torrentPubDate,
                                           String torrentLink,
                                           String torrentStoredPath,
                                           RssBangumiEpisodeStatusEnum status) {

}
