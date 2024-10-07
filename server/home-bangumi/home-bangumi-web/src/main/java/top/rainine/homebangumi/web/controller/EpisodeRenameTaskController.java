package top.rainine.homebangumi.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import top.rainine.homebangumi.api.EpisodeRenameTaskApi;
import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;
import top.rainine.homebangumi.core.rename.episode.rename.EpisodeRenameTaskFacadeService;

/**
 * @author rainine
 * @description 剧集重命名任务controller
 * @date 2024/7/18 15:21:58
 */
@RestController
@RequiredArgsConstructor
public class EpisodeRenameTaskController implements EpisodeRenameTaskApi {

    private final EpisodeRenameTaskFacadeService taskFacadeService;

    @Override
    public Result<CreateEpisodeRenameTaskResp> createTask(CreateEpisodeRenameTaskReq req) {
        return Result.createSuccess(taskFacadeService.createTask(req));
    }

    @Override
    public Result<EpisodeRenameTaskDetailResp> getTaskDetail(Long id) {
        return Result.createSuccess(taskFacadeService.getTaskDetail(id));
    }

    @Override
    public Result<EpisodeRenameTaskItemsResp> getTaskItems(Long id) {
        return Result.createSuccess(taskFacadeService.getTaskItems(id));
    }

    @Override
    public Result<EpisodeRenameTaskDetailResp> updateTask(Long id, UpdateEpisodeRenameTaskReq req) {
        return Result.createSuccess(taskFacadeService.updateTask(id, req));
    }

    @Override
    public Result<Void> submitTask(Long id) {
        taskFacadeService.submitTask(id);
        return Result.createSuccess();
    }

    @Override
    public Result<Void> reparseTaskItems(Long id) {
        taskFacadeService.reparseTaskItems(id);
        return Result.createSuccess();
    }

    @Override
    public Result<Void> ignoreTaskItem(Long id, Long itemId) {
        taskFacadeService.ignoreTaskItem(id, itemId);
        return Result.createSuccess();
    }

    @Override
    public Result<Void> manualParseTaskItem(Long id, Long itemId, ManualParseRenameTaskItemReq req) {
        taskFacadeService.manualParseTaskItem(id, itemId, req);
        return Result.createSuccess();
    }

    @Override
    public Result<PagedResp<PagedEpisodeRenameTaskItemDto>> loadPagedTasks(PagedReq<LoadPagedTasksReqConditionDto> req) {
        return Result.createSuccess(taskFacadeService.loadPagedTasks(req));
    }
}
