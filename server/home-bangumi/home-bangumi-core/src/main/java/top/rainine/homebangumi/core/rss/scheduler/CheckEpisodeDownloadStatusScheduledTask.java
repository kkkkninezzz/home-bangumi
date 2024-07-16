package top.rainine.homebangumi.core.rss.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.rss.RssBangumiEpisodeDownloadService;
import top.rainine.homebangumi.core.scheduler.HbScheduledTask;
import top.rainine.homebangumi.core.settings.ScheduledTaskSettingsService;
import top.rainine.homebangumi.core.settings.data.ScheduledTaskSettings;
import top.rainine.homebangumi.def.enums.ScheduledTaskIdEnum;

import java.time.Duration;

/**
 * @author rainine
 * @description 检查番剧下载状态的定时任务
 * @date 2024/4/29 17:46:48
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CheckEpisodeDownloadStatusScheduledTask implements HbScheduledTask {

    private final RssBangumiEpisodeDownloadService downloadService;

    private final ScheduledTaskSettingsService scheduledTaskSettingsService;

    @Override
    public ScheduledTaskIdEnum getTaskId() {
        return ScheduledTaskIdEnum.CHECK_EPISODE_DOWNLOAD;
    }

    @Override
    public Runnable task() {
        return () -> {
            log.info("[CheckEpisodeDownloadStatusScheduledTask]execute check episode download result task");
            downloadService.checkAllEpisodes();
        };
    }

    @Override
    public Trigger getTrigger() {
        return triggerContext -> {
            PeriodicTrigger trigger = new PeriodicTrigger(scheduledTaskSettingsService.getCheckEpisodeDownloadStatusDuration());
            return trigger.nextExecution(triggerContext);
        };
    }
}
