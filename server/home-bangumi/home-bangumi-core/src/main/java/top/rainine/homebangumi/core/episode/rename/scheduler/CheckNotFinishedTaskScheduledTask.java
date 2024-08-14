package top.rainine.homebangumi.core.episode.rename.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.episode.rename.EpisodeRenameTaskManager;
import top.rainine.homebangumi.core.scheduler.HbScheduledTask;
import top.rainine.homebangumi.core.settings.ScheduledTaskSettingsService;
import top.rainine.homebangumi.def.enums.ScheduledTaskIdEnum;

/**
 * @author rainine
 * @description 检测未完成任务
 * @date 2024/8/14 15:26:58
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CheckNotFinishedTaskScheduledTask implements HbScheduledTask {

    private final EpisodeRenameTaskManager taskManager;

    private final ScheduledTaskSettingsService scheduledTaskSettingsService;

    @Override
    public ScheduledTaskIdEnum getTaskId() {
        return ScheduledTaskIdEnum.CHECK_NOT_FINISHED_RENAME_TASK;
    }

    @Override
    public Runnable task() {
        return () -> {
            log.info("[CheckNotFinishedTaskScheduledTask]execute check not finished rename task");
            taskManager.checkAllNotFinishedTasks();
        };
    }

    @Override
    public Trigger getTrigger() {
        return triggerContext -> {
            PeriodicTrigger trigger = new PeriodicTrigger(scheduledTaskSettingsService.getCheckNotFinishedRenameTaskDuration());
            return trigger.nextExecution(triggerContext);
        };
    }
}
