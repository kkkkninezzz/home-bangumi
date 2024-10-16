package top.rainine.homebangumi.core.rename.episode.rename;

import top.rainine.homebangumi.core.rename.episode.rename.data.EpisodeRenameTaskItemParsedInfo;
import top.rainine.homebangumi.core.rename.episode.rename.data.EpisodeRenameTaskItemParserConfig;

import java.util.List;

/**
 * @author rainine
 * @description 解析生成任务项
 * @date 2024/7/18 16:24:16
 */
public interface EpisodeRenameTaskItemParser {

    /**
     * 按规则解析
     * */
    List<EpisodeRenameTaskItemParsedInfo> parse(EpisodeRenameTaskItemParserConfig config);
}
