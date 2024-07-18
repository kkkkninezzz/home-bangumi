package top.rainine.homebangumi.core.episode.rename.data;

import top.rainine.homebangumi.def.enums.EpisodeRenameTaskItemStatusEnum;

/**
 *
 * @param episodeNo                 剧集编号
 * @param season                    季
 * @param episodeFileName           种子中获取到的剧集文件名
 * @param renamedEpisodeFileName    重命名后的剧集文件名
 * @param status                    状态，通常为{@link EpisodeRenameTaskItemStatusEnum#PARSED}、
 *                                  {@link EpisodeRenameTaskItemStatusEnum#FILTERED_OUT}
 *                                  {@link EpisodeRenameTaskItemStatusEnum#PARSE_TITLE_FAILED}
 *
 * @author rainine
 * @description 解析出来的信息
 * @date 2024/7/18 16:39:09
 */
public record EpisodeRenameTaskItemParsedInfo(int episodeNo,
                                              int season,
                                              String episodeFileName,
                                              String renamedEpisodeFileName,
                                              EpisodeRenameTaskItemStatusEnum status) {
}
