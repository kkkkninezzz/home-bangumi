package top.rainine.homebangumi.core.settings;

import top.rainine.homebangumi.core.settings.data.ScheduledTaskSettings;

import java.time.Duration;

/**
 * @author rainine
 * @description 计划任务的配置信息
 * @date 2024/5/14 18:10:26
 */
public interface ScheduledTaskSettingsService {

    ScheduledTaskSettings getSettings();

    void updateSettings(ScheduledTaskSettings settings);

    Duration getCheckEpisodeDownloadStatusDuration();

    Duration getPushParsedEpisodesToDownloaderDuration();

    Duration getRenameEpisodesDuration();

    Duration getUpdateRssSubscriptionDuration();

    Duration getCheckNotFinishedRenameTaskDuration();
}
