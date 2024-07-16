package top.rainine.homebangumi.core.scheduler.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import top.rainine.homebangumi.core.scheduler.HbScheduledTask;
import top.rainine.homebangumi.core.scheduler.ScheduledTaskService;

import java.util.Map;

/**
 * @author rainine
 * @description 定时任务执行器
 * @date 2024/4/22 18:47:58
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class HbScheduledConfigurer implements SchedulingConfigurer {

    private final ApplicationContext applicationContext;

    private final ScheduledTaskService scheduledTaskService;

    @Override
    public void configureTasks(@NotNull ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler scheduler = getThreadPoolTaskScheduler();
        scheduler.initialize();
        taskRegistrar.setScheduler(scheduler);

        initTasks(taskRegistrar);
    }

    private void initTasks(ScheduledTaskRegistrar taskRegistrar) {
        Map<String, HbScheduledTask> beanMap = applicationContext.getBeansOfType(HbScheduledTask.class);
        if (MapUtils.isEmpty(beanMap)) {
            log.info("[ScheduledTaskManager]not found scheduled tasks");
            return;
        }
        log.info("[ScheduledTaskManager]found scheduled tasks, count: {}", beanMap.size());
        beanMap.values().forEach(task -> {
            taskRegistrar.addTriggerTask(task.task(), task.getTrigger());
            scheduledTaskService.addTask(task);
        });
    }

    public ThreadPoolTaskScheduler getThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1); // 设置线程池大小
        scheduler.setThreadNamePrefix("hb-scheduled-task-"); // 设置线程名称前缀
        scheduler.setAwaitTerminationSeconds(60); // 设置终止等待时间
        // 设置处理拒绝执行的任务异常
        scheduler.setRejectedExecutionHandler((r, _) -> log.error("[HbScheduledConfigure]Task rejected, task: {}", r));
        // 处理定时任务执行过程中抛出的未捕获异常
        scheduler.setErrorHandler(e -> log.error("[HbScheduledConfigure]Error in scheduled task", e));
        return scheduler;
    }

}
