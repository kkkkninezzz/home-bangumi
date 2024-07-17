package top.rainine.homebangumi.api;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;

/**
 * @author rainine
 * @description 剧集重命名任务api
 * @date 2024/7/17 14:59:45
 */
@RequestMapping("/api/v1/episode/rename/task")
public interface EpisodeRenameTaskApi {

    /**
     * 创建重命名任务
     * */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<CreateEpisodeRenameTaskResp> createTask(@Valid @RequestBody CreateEpisodeRenameTaskReq req);

    /**
     * 获取任务详情
     * */
    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<EpisodeRenameTaskDetailResp> getTaskDetail(@PathVariable("id") Long id);

    /**
     * 获取任务项
     * */
    @GetMapping(value = "/detail/{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<EpisodeRenameTaskItemsResp> getTaskItems(@PathVariable("id") Long id);

    /**
     * 更新任务
     * */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<EpisodeRenameTaskDetailResp> updateTask(@PathVariable("id") Long id, @Valid @RequestBody UpdateEpisodeRenameTaskReq req);

    /**
     * 提交任务
     * */
    @PostMapping(value = "/{id}/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Long> submitTask(@PathVariable("id") Long id);

    /**
     * 重新解析任务项
     * 注意: 该接口将删除所有已经解析出的任务项，并重新进行解析
     * */
    @PostMapping(value = "/{id}/items/reparse", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Long> reparseTaskItems(@PathVariable("id") Long id);

    /**
     * 忽略任务项
     * */
    @PutMapping(value = "/{id}/item/{itemId}/ignore", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> ignoreTaskItem(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId);

    /**
     * 手动解析任务项
     * */
    @PutMapping(value = "/{id}/item/{itemId}/manual-parse", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> manualParseTaskItem(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId, @RequestBody @Valid ManualParseRenameTaskItemReq req);

    /**
     * 获取分页的任务列表
     * */
    @PostMapping(value = "/paged/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<PagedResp<PagedEpisodeRenameTaskItemDto>> loadPagedTasks(@Valid @RequestBody PagedReq<LoadPagedTasksReqConditionDto> req);
}
