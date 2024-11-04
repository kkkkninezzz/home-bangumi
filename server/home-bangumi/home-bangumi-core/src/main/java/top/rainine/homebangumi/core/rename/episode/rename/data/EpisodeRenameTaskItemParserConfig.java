package top.rainine.homebangumi.core.rename.episode.rename.data;

import lombok.Builder;
import top.rainine.homebangumi.def.enums.EpisodeTitleRenameMethodEnum;

import java.util.List;

/**
 * @param season 指定的季度
 * @param episodeNoOffset  剧集的偏移量
 * @param episodeDirPath 剧集目录路径
 * @param episodeDirPathMaxDepth 剧集目录路径
 * @param filteredOutRules 过滤掉的规则，将忽略掉符合该规则的剧集信息，根据文件名进行过滤
 * @param skippedEpisodeNo 跳过的剧集号
 * @param episodeTitleRenameMethod 剧集重命名方式
 * @param customizeRenamedEpisodeTitleFormat 自定义重命名后的剧集标题格式，{@link EpisodeRenameTaskItemParserConfig#episodeTitleRenameMethod} == {@link EpisodeTitleRenameMethodEnum#CUSTOMIZED_TITLE}时不允许为空
 *
 * @author rainine
 * @description 解析配置
 * @date 2024/7/18 16:25:35
 */

@Builder
public record EpisodeRenameTaskItemParserConfig(Integer season,
                                                Integer episodeNoOffset,
                                                String episodeDirPath,
                                                Integer episodeDirPathMaxDepth,
                                                Integer skippedEpisodeNo,
                                                List<String> filteredOutRules,
                                                EpisodeTitleRenameMethodEnum episodeTitleRenameMethod,
                                                String customizeRenamedEpisodeTitleFormat) {
}
