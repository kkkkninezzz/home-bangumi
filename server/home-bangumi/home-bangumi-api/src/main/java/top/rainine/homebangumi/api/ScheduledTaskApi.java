package top.rainine.homebangumi.api;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import top.rainine.homebangumi.api.req.RunOnceScheduledTaskReq;
import top.rainine.homebangumi.api.resp.Result;

/**
 * @author rainine
 * @description 定时任务api
 * @date 2024/5/17 18:21:26
 */
@RequestMapping("/api/v1/scheduled-task")
public interface ScheduledTaskApi {
    /**
     * 立即执行一次指定的定时任务
     * */
    @PostMapping(value = "/run-once", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> runOnceTask(@Valid @RequestBody RunOnceScheduledTaskReq req);
}
