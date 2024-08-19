package top.rainine.homebangumi.core.settings.data;

import lombok.Builder;

import java.time.Duration;

/**
 * @param checkEpisodeDownloadStatusDuration 检查番剧下载状态的定时任务周期
 * @param pushParsedEpisodesToDownloaderDuration 推送已经解析好的番剧到下载器的定时任务周期
 * @param renameEpisodesDuration 重命名剧集的定时任务周期
 * @param updateRssSubscriptionDuration 定时更新rss bangumi的定时任务周期
 * @param checkNotFinishedRenameTaskDuration 检查未完成的重命名任务 的定时任务周期
 *
 * @author rainine
 * @description 计划任务配置信息
 * @date 2024/5/14 18:11:16
 */
@Builder
public record ScheduledTaskSettings(Duration checkEpisodeDownloadStatusDuration,
                                    Duration pushParsedEpisodesToDownloaderDuration,
                                    Duration renameEpisodesDuration,
                                    Duration updateRssSubscriptionDuration,
                                    Duration checkNotFinishedRenameTaskDuration) {
}
