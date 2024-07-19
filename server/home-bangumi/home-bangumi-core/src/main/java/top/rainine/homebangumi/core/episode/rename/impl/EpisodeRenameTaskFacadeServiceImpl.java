package top.rainine.homebangumi.core.episode.rename.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;
import top.rainine.homebangumi.common.utils.GsonUtils;
import top.rainine.homebangumi.core.episode.rename.EpisodeRenameTaskFacadeService;
import top.rainine.homebangumi.core.episode.rename.EpisodeRenameTaskItemParser;
import top.rainine.homebangumi.core.episode.rename.data.EpisodeRenameTaskItemParsedInfo;
import top.rainine.homebangumi.core.episode.rename.data.EpisodeRenameTaskItemParserConfig;
import top.rainine.homebangumi.core.episode.rename.data.convertor.EpisodeRenameTaskConvertor;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTask;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTaskItem;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskItemRepository;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskRepository;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskItemStatusEnum;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskStatusEnum;
import top.rainine.homebangumi.def.enums.EpisodeTitleRenameMethodEnum;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private final EpisodeRenameTaskConvertor episodeRenameTaskConvertor;

    @Override
    @Transactional
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

        List<EpisodeRenameTaskItemParsedInfo> itemParsedInfoList = taskItemParser.parse(config);

        HbEpisodeRenameTask episodeRenameTask = episodeRenameTaskConvertor.toHbEpisodeRenameTask(req);
        taskRepository.save(episodeRenameTask);

        Path episodeDirPath = Paths.get(req.getEpisodeDirPath());
        Path renamedOutputDirPath = Paths.get(req.getRenamedOutputDirPath());
        List<HbEpisodeRenameTaskItem> taskItemList = itemParsedInfoList.stream()
                .map(parsedInfo -> toHbEpisodeRenameTaskItem(episodeRenameTask.getId(), episodeDirPath, renamedOutputDirPath, parsedInfo))
                .toList();
        taskItemRepository.saveAll(taskItemList);

        return new CreateEpisodeRenameTaskResp()
                .setTaskId(episodeRenameTask.getId());
    }

    private HbEpisodeRenameTaskItem toHbEpisodeRenameTaskItem(Long taskId,
                                                              Path episodeDirPath, Path renamedOutputDirPath,
                                                              EpisodeRenameTaskItemParsedInfo parsedInfo) {
        HbEpisodeRenameTaskItem taskItem = episodeRenameTaskConvertor.toHbEpisodeRenameTaskItem(taskId, parsedInfo);

        if (StringUtils.isNotBlank(parsedInfo.episodeFileName())) {
            taskItem.setEpisodePath(episodeDirPath.resolve(parsedInfo.episodeFileName()).toString());
        }

        if (StringUtils.isNotBlank(parsedInfo.renamedEpisodeFileName())) {
            taskItem.setRenamedEpisodeOutputPath(renamedOutputDirPath.resolve(parsedInfo.renamedEpisodeFileName()).toString());
        }
        return taskItem;
    }

    /**
     * 获取任务，不存在则抛出异常
     * */
    private HbEpisodeRenameTask getTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new HbBizException(HbCodeEnum.EPISODE_RENAME_TASK_NOT_EXISTS));
    }

    @Override
    public EpisodeRenameTaskDetailResp getTaskDetail(Long id) {
        HbEpisodeRenameTask renameTask = getTaskOrThrow(id);

        return episodeRenameTaskConvertor.toEpisodeRenameTaskDetailResp(renameTask);
    }

    @Override
    public EpisodeRenameTaskItemsResp getTaskItems(Long id) {
        getTaskOrThrow(id);

        List<HbEpisodeRenameTaskItem> hbEpisodeRenameTaskItems = taskItemRepository.findAllByTaskId(id);
        List<EpisodeRenameTaskItemDto> list = hbEpisodeRenameTaskItems
                .stream()
                .sorted((e1, e2) -> {
                    int e1SortValue = calTaskItemSortValue(e1);
                    int e2SortValue = calTaskItemSortValue(e2);
                    return e1SortValue - e2SortValue;
                })
                .map(episodeRenameTaskConvertor::toEpisodeRenameTaskItemDto)
                .toList();

        return new EpisodeRenameTaskItemsResp()
                .setItems(list);
    }

    /**
     * 计算排序值
     * */
    private int calTaskItemSortValue(HbEpisodeRenameTaskItem item) {
        Integer status = item.getStatus();

        if (EpisodeRenameTaskItemStatusEnum.FILTERED_OUT.equals(status)
                || EpisodeRenameTaskItemStatusEnum.IGNORED.equals(status)) {
            return 1000000;
        }

        // 如果是标题解析失败的，那么优先级最高，需要人工参与
        if (EpisodeRenameTaskItemStatusEnum.TITLE_PARSE_FAILED.equals(status)) {
            return -10000;
        }

        // 剩下的按剧集倒序排
        return Objects.nonNull(item.getEpisodeNo()) ? -item.getEpisodeNo(): 0;
    }

    @Override
    public EpisodeRenameTaskDetailResp updateTask(Long id, UpdateEpisodeRenameTaskReq req) {
        HbEpisodeRenameTask renameTask = getAndCheckTaskStatus(id);

        episodeRenameTaskConvertor.updateHbEpisodeRenameTask(renameTask, req);
        taskRepository.save(renameTask);

        return episodeRenameTaskConvertor.toEpisodeRenameTaskDetailResp(renameTask);
    }

    private HbEpisodeRenameTask getAndCheckTaskStatus(Long id) {
        HbEpisodeRenameTask renameTask = getTaskOrThrow(id);
        // 如果不是待处理状态，那么不允许更新
        if (!EpisodeRenameTaskStatusEnum.NONE.equals(renameTask.getTaskStatus())) {
            throw new HbBizException(HbCodeEnum.EPISODE_RENAME_TASK_STATUS_NOT_ALLOW_OPERATE);
        }

        return renameTask;
    }

    @Override
    public void submitTask(Long id) {

    }

    @Override
    @Transactional
    public void reparseTaskItems(Long id) {
        HbEpisodeRenameTask renameTask = getAndCheckTaskStatus(id);

        taskItemRepository.deleteAllByTaskId(id);

        EpisodeRenameTaskItemParserConfig config = EpisodeRenameTaskItemParserConfig
                .builder()
                .season(renameTask.getSeason())
                .episodeDirPath(renameTask.getEpisodeDirPath())
                .filteredOutRules(GsonUtils.toList(renameTask.getFilteredOutRules(), String.class))
                .episodeTitleRenameMethod(EpisodeTitleRenameMethodEnum.of(renameTask.getEpisodeTitleRenameMethod()))
                .customizeRenamedEpisodeTitleFormat(renameTask.getCustomizeRenamedEpisodeTitleFormat())
                .build();

        List<EpisodeRenameTaskItemParsedInfo> itemParsedInfoList = taskItemParser.parse(config);

        Path episodeDirPath = Paths.get(renameTask.getEpisodeDirPath());
        Path renamedOutputDirPath = Paths.get(renameTask.getRenamedOutputDirPath());
        List<HbEpisodeRenameTaskItem> taskItemList = itemParsedInfoList.stream()
                .map(parsedInfo -> toHbEpisodeRenameTaskItem(renameTask.getId(), episodeDirPath, renamedOutputDirPath, parsedInfo))
                .toList();
        taskItemRepository.saveAll(taskItemList);
    }

    private HbEpisodeRenameTaskItem getTaskItemOrThrow(Long taskId, Long itemId) {
        return taskItemRepository.findByTaskIdAndId(taskId, itemId)
                .orElseThrow(() -> new HbBizException(HbCodeEnum.EPISODE_RENAME_TASK_ITEM_NOT_EXISTS));
    }

    @Override
    public void ignoreTaskItem(Long id, Long itemId) {
        getAndCheckTaskStatus(id);
        HbEpisodeRenameTaskItem taskItem = getTaskItemOrThrow(id, itemId);

        taskItem.setStatus(EpisodeRenameTaskItemStatusEnum.IGNORED.getStatus());
        taskItemRepository.save(taskItem);
    }

    @Override
    public void manualParseTaskItem(Long id, Long itemId, ManualParseRenameTaskItemReq req) {

    }

    @Override
    public PagedResp<PagedEpisodeRenameTaskItemDto> loadPagedTasks(PagedReq<LoadPagedTasksReqConditionDto> req) {
        return null;
    }
}
