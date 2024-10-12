package top.rainine.homebangumi.core.rename.episode.rename.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.common.utils.HbFileNameUtils;
import top.rainine.homebangumi.core.rename.episode.rename.EpisodeRenameTaskItemParser;
import top.rainine.homebangumi.core.rename.episode.rename.data.EpisodeRenameTaskItemParsedInfo;
import top.rainine.homebangumi.core.rename.episode.rename.data.EpisodeRenameTaskItemParserConfig;
import top.rainine.homebangumi.core.common.episoderenamer.EpisodeTitleRenameAdapter;
import top.rainine.homebangumi.core.common.episoderenamer.EpisodeTitleRenameAdapterFactory;
import top.rainine.homebangumi.core.common.titleparser.EpisodeTitleParser;
import top.rainine.homebangumi.core.common.titleparser.data.EpisodeTitleInfo;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskItemStatusEnum;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author rainine
 * @description 解析实现类
 * @date 2024/7/18 18:34:32
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EpisodeRenameTaskItemParserImpl implements EpisodeRenameTaskItemParser {

    private final EpisodeTitleParser episodeTitleParser;

    private final EpisodeTitleRenameAdapterFactory episodeTitleRenameAdapterFactory;

    @Override
    public List<EpisodeRenameTaskItemParsedInfo> parse(EpisodeRenameTaskItemParserConfig config) {
        try {
            return doParse(config);
        } catch (IOException e) {
            log.error("[EpisodeRenameTaskItemParserImpl]parse episode rename task item failed, config: {}", config, e);
            throw new HbBizException(HbCodeEnum.PARSE_EPISODE_RENAME_TASK_ITEM_FAILED);
        }


    }

    private List<EpisodeRenameTaskItemParsedInfo> doParse(EpisodeRenameTaskItemParserConfig config) throws IOException {
        // 标题重命名适配器
        EpisodeTitleRenameAdapter episodeTitleRenameAdapter = episodeTitleRenameAdapterFactory.newRenameAdapter(config.episodeTitleRenameMethod(),
                "",
                config.customizeRenamedEpisodeTitleFormat());

        Path episodeDirPath = Paths.get(config.episodeDirPath());
        // 如果不是目录
        if (!Files.isDirectory(episodeDirPath)) {
            throw new HbBizException();
        }

        List<EpisodeRenameTaskItemParsedInfo> parsedInfoList;
        try (Stream<Path> paths = Files.walk(episodeDirPath, config.episodeDirPathMaxDepth())) {
            parsedInfoList = paths
                    .filter(path -> !Files.isDirectory(path))
                    .map(path -> parseItem(config, path, episodeTitleRenameAdapter))
                    .toList();
        }

        return parsedInfoList;
    }

    private EpisodeRenameTaskItemParsedInfo parseItem(EpisodeRenameTaskItemParserConfig config, Path episodeFilePath, EpisodeTitleRenameAdapter episodeTitleRenameAdapter) {
        String episodeFileName = episodeFilePath.getFileName().toString();

        EpisodeRenameTaskItemParsedInfo.EpisodeRenameTaskItemParsedInfoBuilder builder = EpisodeRenameTaskItemParsedInfo
                .builder()
                .episodeFileName(episodeFileName);
        if (isFilteredOut(episodeFileName, config.filteredOutRules())) {
            builder.status(EpisodeRenameTaskItemStatusEnum.FILTERED_OUT);
            return builder.build();
        }

        EpisodeTitleInfo episodeTitleInfo;
        try {
            episodeTitleInfo = episodeTitleParser.parseTitle(episodeFileName, config.season());
        } catch (Exception e) {
            log.error("[EpisodeRenameTaskItemParser]parse title failed, episodeFilePath: {}, episodeFileName: {}",
                    episodeFilePath, episodeFileName);
            return builder
                    .status(EpisodeRenameTaskItemStatusEnum.TITLE_PARSE_FAILED)
                    .build();
        }

        String renamedTitle = episodeTitleRenameAdapter.renameTitle(episodeFileName, episodeTitleInfo);
        String fileExtension = FilenameUtils.getExtension(episodeFileName);
        String renamedTitleFileName;
        if (StringUtils.isNotBlank(fileExtension)) {
            renamedTitleFileName = STR."\{renamedTitle}.\{FilenameUtils.getExtension(episodeFileName)}";
        } else {
            renamedTitleFileName = renamedTitle;
        }

        // 对名字进行过滤
        renamedTitleFileName = HbFileNameUtils.filterIllegalChars(renamedTitleFileName);

        return builder.episodeNo(episodeTitleInfo.episode())
                .season(episodeTitleInfo.season())
                .renamedEpisodeFileName(renamedTitleFileName)
                .status(EpisodeRenameTaskItemStatusEnum.PARSED)
                .build();
    }

    /**
     * 是否被过滤掉
     * */
    private boolean isFilteredOut(String episodeFileName, List<String> rules) {
        return rules.stream().anyMatch(episodeFileName::contains);
    }
}





















