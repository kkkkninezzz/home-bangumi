package top.rainine.homebangumi.core.settings.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.settings.ScheduledTaskSettingsService;
import top.rainine.homebangumi.core.settings.SystemSettingsService;
import top.rainine.homebangumi.core.settings.data.BaseSystemSetting;
import top.rainine.homebangumi.core.settings.data.ScheduledTaskSettings;
import top.rainine.homebangumi.def.enums.SystemSettingKeyEnum;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author rainine
 * @description
 * @date 2024/5/14 18:18:30
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledTaskSettingsServiceImpl implements ScheduledTaskSettingsService {

    private final SystemSettingsService systemSettingsService;

    private static final List<SystemSettingKeyEnum> KEYS = List.of(
            SystemSettingKeyEnum.CHECK_EPISODE_DOWNLOAD_STATUS_DURATION,
            SystemSettingKeyEnum.PUSH_PARSED_EPISODES_TO_DOWNLOADER_DURATION,
            SystemSettingKeyEnum.RENAME_EPISODES_DURATION,
            SystemSettingKeyEnum.UPDATE_RSS_SUBSCRIPTION_DURATION);

    @Override
    public ScheduledTaskSettings getSettings() {
        Map<SystemSettingKeyEnum, BaseSystemSetting> settingMap = systemSettingsService.loadSettingMap(KEYS);

        return ScheduledTaskSettings
                .builder()
                .checkEpisodeDownloadStatusDuration(ofMinutes(settingMap.get(SystemSettingKeyEnum.CHECK_EPISODE_DOWNLOAD_STATUS_DURATION).getValue()))
                .pushParsedEpisodesToDownloaderDuration(ofMinutes(settingMap.get(SystemSettingKeyEnum.PUSH_PARSED_EPISODES_TO_DOWNLOADER_DURATION).getValue()))
                .renameEpisodesDuration(ofMinutes(settingMap.get(SystemSettingKeyEnum.RENAME_EPISODES_DURATION).getValue()))
                .updateRssSubscriptionDuration(ofMinutes(settingMap.get(SystemSettingKeyEnum.UPDATE_RSS_SUBSCRIPTION_DURATION).getValue()))
                .checkNotFinishedRenameTaskDuration(ofMinutes(settingMap.get(SystemSettingKeyEnum.CHECK_NOT_FINISHED_RENAME_TASK_DURATION).getValue()))
                .build();
    }

    @Override
    public void updateSettings(ScheduledTaskSettings settings) {
        List<BaseSystemSetting> baseSystemSettings = List.of(
                new BaseSystemSetting(SystemSettingKeyEnum.CHECK_EPISODE_DOWNLOAD_STATUS_DURATION, toMinutes(settings.checkEpisodeDownloadStatusDuration())),
                new BaseSystemSetting(SystemSettingKeyEnum.PUSH_PARSED_EPISODES_TO_DOWNLOADER_DURATION, toMinutes(settings.pushParsedEpisodesToDownloaderDuration())),
                new BaseSystemSetting(SystemSettingKeyEnum.RENAME_EPISODES_DURATION, toMinutes(settings.renameEpisodesDuration())),
                new BaseSystemSetting(SystemSettingKeyEnum.UPDATE_RSS_SUBSCRIPTION_DURATION, toMinutes(settings.updateRssSubscriptionDuration()))
        );

        systemSettingsService.saveSettings(baseSystemSettings);
    }

    private Duration ofMinutes(int durationValue) {
        return Duration.ofMinutes(durationValue);
    }

    private int toMinutes(Duration duration) {
        return (int) duration.toMinutes();
    }

    @Override
    public Duration getCheckEpisodeDownloadStatusDuration() {
        return ofMinutes(systemSettingsService.getSetting(SystemSettingKeyEnum.CHECK_EPISODE_DOWNLOAD_STATUS_DURATION).getValue());
    }

    @Override
    public Duration getPushParsedEpisodesToDownloaderDuration() {
        return ofMinutes(systemSettingsService.getSetting(SystemSettingKeyEnum.PUSH_PARSED_EPISODES_TO_DOWNLOADER_DURATION).getValue());
    }

    @Override
    public Duration getRenameEpisodesDuration() {
        return ofMinutes(systemSettingsService.getSetting(SystemSettingKeyEnum.RENAME_EPISODES_DURATION).getValue());
    }

    @Override
    public Duration getUpdateRssSubscriptionDuration() {
        return ofMinutes(systemSettingsService.getSetting(SystemSettingKeyEnum.UPDATE_RSS_SUBSCRIPTION_DURATION).getValue());
    }

    @Override
    public Duration getCheckNotFinishedRenameTaskDuration() {
        return ofMinutes(systemSettingsService.getSetting(SystemSettingKeyEnum.CHECK_NOT_FINISHED_RENAME_TASK_DURATION).getValue());
    }
}
