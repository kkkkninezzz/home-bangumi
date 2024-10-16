package top.rainine.homebangumi.core.rss.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.rss.RssBangumiEpisodeDownloadService;
import top.rainine.homebangumi.core.common.scheduler.HbScheduledTask;
import top.rainine.homebangumi.core.settings.ScheduledTaskSettingsService;
import top.rainine.homebangumi.def.enums.ScheduledTaskIdEnum;

/**
 * @authoer rainine
 * @date 2024/4/24 23:35
 * @desc 推送已经解析好的番剧到下载器的定时任务
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PushParsedEpisodesToDownloaderScheduledTask implements HbScheduledTask {

    private final RssBangumiEpisodeDownloadService episodeDownloadService;

    private final ScheduledTaskSettingsService scheduledTaskSettingsService;

    @Override
    public ScheduledTaskIdEnum getTaskId() {
        return ScheduledTaskIdEnum.PUSH_PARSED_EPISODES_TO_DOWNLOADER;
    }

    @Override
    public Runnable task() {
        return () -> {
            log.info("[PushParsedEpisodesToDownloaderScheduledTask]execute task");
            episodeDownloadService.pushAllToDownloader();
        };
    }

    @Override
    public Trigger getTrigger() {
        return triggerContext -> {
            PeriodicTrigger trigger = new PeriodicTrigger(scheduledTaskSettingsService.getPushParsedEpisodesToDownloaderDuration());
            return trigger.nextExecution(triggerContext);
        };
    }
}
