package top.rainine.homebangumi.core.episode.rename.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.episode.rename.EpisodeRenameTaskItemParser;
import top.rainine.homebangumi.core.episode.rename.data.EpisodeRenameTaskItemParsedInfo;
import top.rainine.homebangumi.core.episode.rename.data.EpisodeRenameTaskItemParserConfig;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author rainine
 * @description 解析实现类
 * @date 2024/7/18 18:34:32
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EpisodeRenameTaskItemParserImpl implements EpisodeRenameTaskItemParser {
    @Override
    public List<EpisodeRenameTaskItemParsedInfo> parse(EpisodeRenameTaskItemParserConfig config) {

        try {
            return doParse(config);
        } catch (Exception e) {
            log.error("[EpisodeRenameTaskItemParserImpl]parse episode rename task item failed, config: {}", config, e);
            // TODO 新增错误码
            throw new HbBizException();
        }


    }

    private List<EpisodeRenameTaskItemParsedInfo> doParse(EpisodeRenameTaskItemParserConfig config) {
        Path episodeDirPath = Paths.get(config.episodeDirPath());
        Files.walk()

        return null;
    }
}
