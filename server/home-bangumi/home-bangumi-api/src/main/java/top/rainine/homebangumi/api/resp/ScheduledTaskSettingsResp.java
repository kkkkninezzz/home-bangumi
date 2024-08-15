package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author rainine
 * @description
 * @date 2024/5/15 18:24:38
 */
@Getter
@Setter
@ToString
public class ScheduledTaskSettingsResp {

    /**
     * 检查番剧下载状态的定时任务周期，单位分钟
     * */
    private Integer checkEpisodeDownloadStatusDuration;

    /**
     * 推送已经解析好的番剧到下载器的定时任务周期，单位分钟
     * */
    private Integer pushParsedEpisodesToDownloaderDuration;

    /**
     * 重命名剧集的定时任务周期，单位分钟
     * */
    private Integer renameEpisodesDuration;

    /**
     * 定时更新rss bangumi的定时任务周期，单位分钟
     * */
    private Integer updateRssSubscriptionDuration;

    /**
     * 检查未完成的重命名任务 的定时任务周期，单位分钟
     * */
    private Integer checkNotFinishedRenameTaskDuration;
}
