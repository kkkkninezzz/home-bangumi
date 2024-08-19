package top.rainine.homebangumi.core.episode.rename;

import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;

/**
 * @author rainine
 * @description 重命名任务
 * @date 2024/7/18 15:29:50
 */
public interface EpisodeRenameTaskFacadeService {

    /**
     * 创建一个任务
     * */
    CreateEpisodeRenameTaskResp createTask(CreateEpisodeRenameTaskReq req);

    /**
     * 获取任务详情
     * */
    EpisodeRenameTaskDetailResp getTaskDetail(Long id);

    /**
     * 获取任务项
     * */
    EpisodeRenameTaskItemsResp getTaskItems(Long id);

    /**
     * 更新任务信息
     * */
    EpisodeRenameTaskDetailResp updateTask(Long id, UpdateEpisodeRenameTaskReq req);

    /**
     * 提交任务到执行器中
     * */
    void submitTask(Long id);

    /**
     * 重新解析任务项
     * */
    void reparseTaskItems(Long id);

    /**
     * 忽略任务项
     * */
    void ignoreTaskItem(Long id, Long itemId);

    /**
     * 手动解析任务项
     * */
    void manualParseTaskItem(Long id, Long itemId, ManualParseRenameTaskItemReq req);

    /**
     * 获取分页的任务列表
     * */
    PagedResp<PagedEpisodeRenameTaskItemDto> loadPagedTasks(PagedReq<LoadPagedTasksReqConditionDto> req);
}
