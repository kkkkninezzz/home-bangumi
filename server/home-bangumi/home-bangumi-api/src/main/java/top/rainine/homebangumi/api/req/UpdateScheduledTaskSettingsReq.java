package top.rainine.homebangumi.api.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @author rainine
 * @description
 * @date 2024/5/15 18:28:18
 */
@Getter
@Setter
@ToString
public class UpdateScheduledTaskSettingsReq {

    /**
     * 检查番剧下载状态的定时任务周期，单位分钟
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.CHECK_EPISODE_DOWNLOAD_STATUS_DURATION_INVALID)
    @Min(value = 1, message = HbCodeEnum.ErrorCode.CHECK_EPISODE_DOWNLOAD_STATUS_DURATION_INVALID)
    private Integer checkEpisodeDownloadStatusDuration;

    /**
     * 推送已经解析好的番剧到下载器的定时任务周期，单位分钟
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.PUSH_PARSED_EPISODES_TO_DOWNLOADER_DURATION_INVALID)
    @Min(value = 1, message = HbCodeEnum.ErrorCode.PUSH_PARSED_EPISODES_TO_DOWNLOADER_DURATION_INVALID)
    private Integer pushParsedEpisodesToDownloaderDuration;

    /**
     * 重命名剧集的定时任务周期，单位分钟
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.RENAME_EPISODES_DURATION_INVALID)
    @Min(value = 1, message = HbCodeEnum.ErrorCode.RENAME_EPISODES_DURATION_INVALID)
    private Integer renameEpisodesDuration;

    /**
     * 定时更新rss bangumi的定时任务周期，单位分钟
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.UPDATE_RSS_SUBSCRIPTION_DURATION_INVALID)
    @Min(value = 1, message = HbCodeEnum.ErrorCode.UPDATE_RSS_SUBSCRIPTION_DURATION_INVALID)
    private Integer updateRssSubscriptionDuration;
}
