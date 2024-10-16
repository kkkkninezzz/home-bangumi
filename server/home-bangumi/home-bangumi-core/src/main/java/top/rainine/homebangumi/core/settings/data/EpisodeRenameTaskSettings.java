package top.rainine.homebangumi.core.settings.data;

import lombok.Builder;

/**
 * @param sourceDirPath 源目录路径
 * @param outDirPath 输出路径
 *
 * @authoer rainine
 * @date 2024/10/7 13:13
 * @desc
 */
@Builder
public record EpisodeRenameTaskSettings( String sourceDirPath, String outDirPath) {
}
