package top.rainine.homebangumi.core.settings.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.event.HbEventBus;
import top.rainine.homebangumi.core.event.data.QbittorrentDownloaderConfigModifiedEvent;
import top.rainine.homebangumi.core.settings.DownloaderSettingsService;
import top.rainine.homebangumi.core.settings.SystemSettingsService;
import top.rainine.homebangumi.core.settings.data.BaseSystemSetting;
import top.rainine.homebangumi.core.settings.data.QbittorrentDownloaderSettings;
import top.rainine.homebangumi.def.enums.SystemSettingKeyEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @authoer rainine
 * @date 2024/4/28 00:13
 * @desc
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DownloaderSettingsServiceImpl implements DownloaderSettingsService {

    private final SystemSettingsService systemSettingsService;

    private final HbEventBus eventBus;

    private static final List<SystemSettingKeyEnum> QBITTORRENT_KEYS = Arrays.asList(
            SystemSettingKeyEnum.QBITTORRENT_BASE_URL,
            SystemSettingKeyEnum.QBITTORRENT_USERNAME,
            SystemSettingKeyEnum.QBITTORRENT_PASSWORD,
            SystemSettingKeyEnum.QBITTORRENT_DOWNLOAD_DIR);

    @Override
    public QbittorrentDownloaderSettings getQbittorrentDownloaderSettings() {
        Map<SystemSettingKeyEnum, BaseSystemSetting> settingMap = systemSettingsService.loadSettingMap(QBITTORRENT_KEYS);
        return QbittorrentDownloaderSettings
                .builder()
                .baseUrl(settingMap.get(SystemSettingKeyEnum.QBITTORRENT_BASE_URL).getValue())
                .username(settingMap.get(SystemSettingKeyEnum.QBITTORRENT_USERNAME).getValue())
                .password(settingMap.get(SystemSettingKeyEnum.QBITTORRENT_PASSWORD).getValue())
                .downloadDir(settingMap.get(SystemSettingKeyEnum.QBITTORRENT_DOWNLOAD_DIR).getValue())
                .build();
    }

    @Override
    public void updateQbittorrentDownloaderSettings(QbittorrentDownloaderSettings settings) {
        List<BaseSystemSetting> baseSystemSettings = List.of(
                new BaseSystemSetting(SystemSettingKeyEnum.QBITTORRENT_BASE_URL, settings.baseUrl()),
                new BaseSystemSetting(SystemSettingKeyEnum.QBITTORRENT_USERNAME, settings.username()),
                new BaseSystemSetting(SystemSettingKeyEnum.QBITTORRENT_PASSWORD, settings.password()),
                new BaseSystemSetting(SystemSettingKeyEnum.QBITTORRENT_DOWNLOAD_DIR, settings.downloadDir())
        );

        systemSettingsService.saveSettings(baseSystemSettings);
        eventBus.publishEvent(QbittorrentDownloaderConfigModifiedEvent.DEFAULT);
    }
}
