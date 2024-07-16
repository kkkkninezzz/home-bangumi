package top.rainine.homebangumi.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import top.rainine.homebangumi.api.ScheduledTaskApi;
import top.rainine.homebangumi.api.req.RunOnceScheduledTaskReq;
import top.rainine.homebangumi.api.resp.Result;
import top.rainine.homebangumi.core.scheduler.ScheduledTaskService;
import top.rainine.homebangumi.def.enums.ScheduledTaskIdEnum;

/**
 * @author rainine
 * @description
 * @date 2024/5/17 18:48:41
 */
@RestController
@RequiredArgsConstructor
public class ScheduledTaskController implements ScheduledTaskApi {

    private final ScheduledTaskService scheduledTaskService;

    @Override
    public Result<Void> runOnceTask(RunOnceScheduledTaskReq req) {
        scheduledTaskService.runOnceTask(ScheduledTaskIdEnum.of(req.getTaskId()));
        return Result.createSuccess();
    }
}
