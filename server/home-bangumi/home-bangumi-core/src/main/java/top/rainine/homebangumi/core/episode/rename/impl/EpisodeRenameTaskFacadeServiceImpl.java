package top.rainine.homebangumi.core.episode.rename.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;
import top.rainine.homebangumi.core.episode.rename.EpisodeRenameTaskFacadeService;
import top.rainine.homebangumi.core.episode.rename.EpisodeRenameTaskItemParser;
import top.rainine.homebangumi.core.episode.rename.data.EpisodeRenameTaskItemParsedInfo;
import top.rainine.homebangumi.core.episode.rename.data.EpisodeRenameTaskItemParserConfig;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskItemRepository;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskRepository;
import top.rainine.homebangumi.def.enums.EpisodeTitleRenameMethodEnum;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author rainine
 * @description 重命名任务实现类
 * @date 2024/7/18 16:15:45
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EpisodeRenameTaskFacadeServiceImpl implements EpisodeRenameTaskFacadeService {

    private final HbEpisodeRenameTaskRepository taskRepository;

    private final HbEpisodeRenameTaskItemRepository taskItemRepository;

    private final EpisodeRenameTaskItemParser taskItemParser;

    @Override
    public CreateEpisodeRenameTaskResp createTask(CreateEpisodeRenameTaskReq req) {

        EpisodeTitleRenameMethodEnum episodeTitleRenameMethod = EpisodeTitleRenameMethodEnum.of(req.getEpisodeTitleRenameMethod());
        if (EpisodeTitleRenameMethodEnum.CUSTOMIZED_TITLE.equals(episodeTitleRenameMethod)
                && StringUtils.isEmpty(req.getCustomizeRenamedEpisodeTitleFormat())) {
            throw new HbBizException(HbCodeEnum.CUSTOMIZE_RENAMED_EPISODE_TITLE_FORMAT_INVALID);
        }

        List<String> filteredOutRules = Optional.ofNullable(req.getFilteredOutRules()).orElseGet(ArrayList::new);

        EpisodeRenameTaskItemParserConfig config = EpisodeRenameTaskItemParserConfig
                .builder()
                .season(req.getSeason())
                .episodeDirPath(req.getEpisodeDirPath())
                .filteredOutRules(filteredOutRules)
                .episodeTitleRenameMethod(episodeTitleRenameMethod)
                .customizeRenamedEpisodeTitleFormat(req.getCustomizeRenamedEpisodeTitleFormat())
                .build();

        List<EpisodeRenameTaskItemParsedInfo> taskItemParsedInfoList = taskItemParser.parse(config);

        // TODO 存入数据库中

        return null;
    }

    @Override
    public EpisodeRenameTaskDetailResp getTaskDetail(Long id) {
        return null;
    }

    @Override
    public EpisodeRenameTaskItemsResp getTaskItems(Long id) {
        return null;
    }

    @Override
    public EpisodeRenameTaskDetailResp updateTask(Long id, UpdateEpisodeRenameTaskReq req) {
        return null;
    }

    @Override
    public void submitTask(Long id) {

    }

    @Override
    public void reparseTaskItems(Long id) {

    }

    @Override
    public void ignoreTaskItem(Long id, Long itemId) {

    }

    @Override
    public void manualParseTaskItem(Long id, Long itemId, ManualParseRenameTaskItemReq req) {

    }

    @Override
    public PagedResp<PagedEpisodeRenameTaskItemDto> loadPagedTasks(PagedReq<LoadPagedTasksReqConditionDto> req) {
        return null;
    }
}
