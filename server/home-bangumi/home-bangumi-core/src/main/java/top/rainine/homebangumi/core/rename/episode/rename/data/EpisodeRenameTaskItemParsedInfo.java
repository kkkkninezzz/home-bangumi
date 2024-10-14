package top.rainine.homebangumi.core.rename.episode.rename.data;

import lombok.Builder;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskItemStatusEnum;

/**
 *
 * @param episodeNo                 剧集编号
 * @param season                    季
 * @param episodeFileName           种子中获取到的剧集文件名
 * @param episodeFilePath           剧集文件的路径
 * @param renamedEpisodeFileName    重命名后的剧集文件名
 * @param status                    状态，通常为{@link EpisodeRenameTaskItemStatusEnum#PARSED}、
 *                                  {@link EpisodeRenameTaskItemStatusEnum#FILTERED_OUT}
 *                                  {@link EpisodeRenameTaskItemStatusEnum#TITLE_PARSE_FAILED}
 *
 * @author rainine
 * @description 解析出来的信息
 * @date 2024/7/18 16:39:09
 */
@Builder
public record EpisodeRenameTaskItemParsedInfo(int episodeNo,
                                              int season,
                                              String episodeFileName,
                                              String episodeFilePath,
                                              String renamedEpisodeFileName,
                                              EpisodeRenameTaskItemStatusEnum status) {
}
