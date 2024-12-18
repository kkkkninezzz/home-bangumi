package top.rainine.homebangumi.core.rename.episode.rename.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;
import top.rainine.homebangumi.common.utils.GsonUtils;
import top.rainine.homebangumi.common.utils.HbFileNameUtils;
import top.rainine.homebangumi.core.rename.episode.rename.EpisodeRenameTaskFacadeService;
import top.rainine.homebangumi.core.rename.episode.rename.EpisodeRenameTaskItemParser;
import top.rainine.homebangumi.core.rename.episode.rename.data.convertor.EpisodeRenameTaskConvertor;
import top.rainine.homebangumi.core.rename.episode.rename.EpisodeRenameTaskManager;
import top.rainine.homebangumi.core.rename.episode.rename.data.EpisodeRenameTaskItemParsedInfo;
import top.rainine.homebangumi.core.rename.episode.rename.data.EpisodeRenameTaskItemParserConfig;
import top.rainine.homebangumi.core.common.utils.PageRequestUtils;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTask;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTaskItem;
import top.rainine.homebangumi.dao.po.QHbEpisodeRenameTask;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskItemRepository;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskRepository;
import top.rainine.homebangumi.dao.utils.JpaUtils;
import top.rainine.homebangumi.def.enums.*;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    private final JPAQueryFactory jpaQueryFactory;

    private final EpisodeRenameTaskManager episodeRenameTaskManager;

    private final EpisodeRenameTaskComponent taskComponent;

    @Override
    @Transactional
    public CreateEpisodeRenameTaskResp createTask(CreateEpisodeRenameTaskReq req) {
        if (!HbFileNameUtils.isValidFileName(req.getEpisodeDirPath())
                || !HbFileNameUtils.isDir(req.getEpisodeDirPath())) {
            throw new HbBizException(HbCodeEnum.EPISODE_DIR_PATH_INVALID);
        }

        if (!HbFileNameUtils.isValidFileName(req.getRenamedOutputDirPath())
         || !HbFileNameUtils.isDir(req.getRenamedOutputDirPath())) {
            throw new HbBizException(HbCodeEnum.RENAMED_EPISODE_OUTPUT_DIR_PATH_INVALID);
        }

        EpisodeTitleRenameMethodEnum episodeTitleRenameMethod = EpisodeTitleRenameMethodEnum.of(req.getEpisodeTitleRenameMethod());
        if (EpisodeTitleRenameMethodEnum.CUSTOMIZED_TITLE.equals(episodeTitleRenameMethod)
                && StringUtils.isEmpty(req.getCustomizeRenamedEpisodeTitleFormat())) {
            throw new HbBizException(HbCodeEnum.CUSTOMIZE_RENAMED_EPISODE_TITLE_FORMAT_INVALID);
        }

        HbEpisodeRenameTask episodeRenameTask = episodeRenameTaskConvertor.toHbEpisodeRenameTask(req);

        EpisodeRenameTaskItemParserConfig config = episodeRenameTaskConvertor.toEpisodeRenameTaskItemParserConfig(episodeRenameTask);
        List<EpisodeRenameTaskItemParsedInfo> itemParsedInfoList = taskItemParser.parse(config);

        taskRepository.save(episodeRenameTask);

        Path renamedOutputDirPath = Paths.get(req.getRenamedOutputDirPath());
        List<HbEpisodeRenameTaskItem> taskItemList = itemParsedInfoList.stream()
                .map(parsedInfo -> toHbEpisodeRenameTaskItem(episodeRenameTask.getId(), renamedOutputDirPath, parsedInfo))
                .toList();
        taskItemRepository.saveAll(taskItemList);

        return new CreateEpisodeRenameTaskResp()
                .setTaskId(episodeRenameTask.getId());
    }

    private HbEpisodeRenameTaskItem toHbEpisodeRenameTaskItem(Long taskId, Path renamedOutputDirPath,
                                                              EpisodeRenameTaskItemParsedInfo parsedInfo) {
        HbEpisodeRenameTaskItem taskItem = episodeRenameTaskConvertor.toHbEpisodeRenameTaskItem(taskId, parsedInfo);

        if (StringUtils.isNotBlank(parsedInfo.episodeFilePath())) {
            taskItem.setEpisodePath(parsedInfo.episodeFilePath());
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
        return taskComponent.getTaskOrThrow(id);
    }

    @Override
    public EpisodeRenameTaskDetailResp getTaskDetail(Long id) {
        HbEpisodeRenameTask renameTask = getTaskOrThrow(id);

        return episodeRenameTaskConvertor.toEpisodeRenameTaskDetailResp(renameTask);
    }

    @Override
    public EpisodeRenameTaskItemsResp getTaskItems(Long id) {
        HbEpisodeRenameTask renameTask = getTaskOrThrow(id);

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
                .setTaskId(renameTask.getId())
                .setTaskStatus(renameTask.getTaskStatus())
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
        if (!HbFileNameUtils.isValidFileName(req.getEpisodeDirPath())
                || !HbFileNameUtils.isDir(req.getEpisodeDirPath())) {
            throw new HbBizException(HbCodeEnum.EPISODE_DIR_PATH_INVALID);
        }

        if (!HbFileNameUtils.isValidFileName(req.getRenamedOutputDirPath())
                || !HbFileNameUtils.isDir(req.getRenamedOutputDirPath())) {
            throw new HbBizException(HbCodeEnum.RENAMED_EPISODE_OUTPUT_DIR_PATH_INVALID);
        }


        EpisodeTitleRenameMethodEnum episodeTitleRenameMethod = EpisodeTitleRenameMethodEnum.of(req.getEpisodeTitleRenameMethod());
        if (EpisodeTitleRenameMethodEnum.CUSTOMIZED_TITLE.equals(episodeTitleRenameMethod)
                && StringUtils.isEmpty(req.getCustomizeRenamedEpisodeTitleFormat())) {
            throw new HbBizException(HbCodeEnum.CUSTOMIZE_RENAMED_EPISODE_TITLE_FORMAT_INVALID);
        }

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
        getAndCheckTaskStatus(id);
        episodeRenameTaskManager.submitTask(id);
    }

    @Override
    @Transactional
    public void reparseTaskItems(Long id) {
        HbEpisodeRenameTask renameTask = getAndCheckTaskStatus(id);

        taskItemRepository.deleteAllByTaskId(id);

        EpisodeRenameTaskItemParserConfig config = episodeRenameTaskConvertor.toEpisodeRenameTaskItemParserConfig(renameTask);
        List<EpisodeRenameTaskItemParsedInfo> itemParsedInfoList = taskItemParser.parse(config);

        Path renamedOutputDirPath = Paths.get(renameTask.getRenamedOutputDirPath());
        List<HbEpisodeRenameTaskItem> taskItemList = itemParsedInfoList.stream()
                .map(parsedInfo -> toHbEpisodeRenameTaskItem(renameTask.getId(), renamedOutputDirPath, parsedInfo))
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
        HbEpisodeRenameTask task = getAndCheckTaskStatus(id);
        HbEpisodeRenameTaskItem taskItem = getTaskItemOrThrow(id, itemId);
        if (!EpisodeRenameTaskItemStatusEnum.PARSED.equals(taskItem.getStatus())
                && !EpisodeRenameTaskItemStatusEnum.TITLE_PARSE_FAILED.equals(taskItem.getStatus())) {
            throw new HbBizException(HbCodeEnum.EPISODE_RENAME_TASK_ITEM_STATUS_NOT_ALLOW_OPERATE);
        }

        taskItem.setEpisodeNo(req.getEpisodeNo());
        taskItem.setRenamedEpisodeFileName(req.getRenamedEpisodeFileName());

        Path newRenamedEpisodeStoredPath = Paths.get(task.getRenamedOutputDirPath()).resolve(req.getRenamedEpisodeFileName());
        taskItem.setRenamedEpisodeOutputPath(newRenamedEpisodeStoredPath.toString());

        if (EpisodeRenameTaskItemStatusEnum.TITLE_PARSE_FAILED.equals(taskItem.getStatus())) {
            taskItem.setStatus(EpisodeRenameTaskItemStatusEnum.PARSED.getStatus());
            taskItem.setErrorMessage("");
        }

        taskItemRepository.save(taskItem);
    }

    @Override
    public PagedResp<PagedEpisodeRenameTaskItemDto> loadPagedTasks(PagedReq<LoadPagedTasksReqConditionDto> req) {
        QHbEpisodeRenameTask qHbEpisodeRenameTask = QHbEpisodeRenameTask.hbEpisodeRenameTask;

        JPAQuery<HbEpisodeRenameTask> query = jpaQueryFactory.select(qHbEpisodeRenameTask)
                .from(qHbEpisodeRenameTask);

        LoadPagedTasksReqConditionDto condition = req.getCondition();
        BooleanExpression where = null;
        if (StringUtils.isNotBlank(condition.getTaskName())) {
            where = JpaUtils.and(where, qHbEpisodeRenameTask.taskName.like(STR."%\{condition.getTaskName()}%"));
        }

        if (Objects.nonNull(condition.getTaskStatus())) {
            where = JpaUtils.and(where, qHbEpisodeRenameTask.taskStatus.eq(condition.getTaskStatus()));
        }

        if (Objects.nonNull(where)) {
            query.where(where);
        }

        Pageable pageable = PageRequestUtils.toPageable(req);
        query.orderBy(qHbEpisodeRenameTask.createdTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        QueryResults<HbEpisodeRenameTask> results = query.fetchResults();
        List<PagedEpisodeRenameTaskItemDto> list = results.getResults()
                .stream()
                .map(this::buildPagedEpisodeRenameTaskItemDto)
                .toList();

        return PagedResp.<PagedEpisodeRenameTaskItemDto>create()
                .setCurrent(req.getCurrent())
                .setPageSize(req.getPageSize())
                .setTotal(results.getTotal())
                .setList(list);
    }



    private PagedEpisodeRenameTaskItemDto buildPagedEpisodeRenameTaskItemDto(HbEpisodeRenameTask task) {
        long totalCount = taskComponent.countTotalOfTaskItems(task.getId());

        long pendingCount = 0;
        long successCount = 0;
        long failedCount = 0;
        if (EpisodeRenameTaskStatusEnum.PENDING.equals(task.getTaskStatus())) {
            pendingCount = taskComponent.countPendingOfTaskItems(task.getId());
        } else if (EpisodeRenameTaskStatusEnum.PROCESSING.equals(task.getTaskStatus())
                || EpisodeRenameTaskStatusEnum.FINISHED.equals(task.getTaskStatus())) {
            pendingCount = taskComponent.countPendingOfTaskItems(task.getId());
            successCount = taskComponent.countSuccessOfTaskItems(task.getId());
            failedCount = taskComponent.countFailedOfTaskItems(task.getId());
        }

        return episodeRenameTaskConvertor.toPagedEpisodeRenameTaskItemDto(task, totalCount, pendingCount, successCount, failedCount);
    }
}















