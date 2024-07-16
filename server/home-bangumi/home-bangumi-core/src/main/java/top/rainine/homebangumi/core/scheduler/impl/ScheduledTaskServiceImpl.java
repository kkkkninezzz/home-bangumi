package top.rainine.homebangumi.core.scheduler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.scheduler.HbScheduledTask;
import top.rainine.homebangumi.core.scheduler.ScheduledTaskService;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.enums.ScheduledTaskIdEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author rainine
 * @description 定时任务管理器
 * @date 2024/4/22 17:52:59
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    private final ConcurrentMap<ScheduledTaskIdEnum, HbScheduledTask> taskMap = new ConcurrentHashMap<>();

    @Override
    public void addTask(HbScheduledTask task) {
        taskMap.put(task.getTaskId(), task);
    }

    @Override
    public void runOnceTask(ScheduledTaskIdEnum taskId) {
        HbScheduledTask hbScheduledTask = Optional.ofNullable(taskMap.get(taskId))
                .orElseThrow(() -> {
                    log.error("[ScheduledTaskService]not supported scheduled task id: {}", taskId);
                    return new HbBizException(HbCodeEnum.NOT_SUPPORTED_SCHEDULED_TASK_ID);
                });

        hbScheduledTask.task().run();
    }

}


















