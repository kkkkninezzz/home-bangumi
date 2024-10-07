package top.rainine.homebangumi.core.settings.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.settings.RenameTaskSettingsService;
import top.rainine.homebangumi.core.settings.SystemSettingsService;
import top.rainine.homebangumi.core.settings.data.BaseSystemSetting;
import top.rainine.homebangumi.core.settings.data.EpisodeRenameTaskSettings;
import top.rainine.homebangumi.def.enums.SystemSettingKeyEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @authoer rainine
 * @date 2024/10/7 13:15
 * @desc
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RenameTaskSettingsServiceImpl implements RenameTaskSettingsService {

    private final SystemSettingsService systemSettingsService;

    private static final List<SystemSettingKeyEnum> EPISODE_KEYS = Arrays.asList(
            SystemSettingKeyEnum.EPISODE_RENAME_TASK_SOURCE_DIR_PATH,
            SystemSettingKeyEnum.EPISODE_RENAME_TASK_OUT_DIR_PATH);

    @Override
    public EpisodeRenameTaskSettings getEpisodeRenameTaskSettings() {
        Map<SystemSettingKeyEnum, BaseSystemSetting> settingMap = systemSettingsService.loadSettingMap(EPISODE_KEYS);
        return EpisodeRenameTaskSettings
                .builder()
                .sourceDirPath(settingMap.get(SystemSettingKeyEnum.EPISODE_RENAME_TASK_SOURCE_DIR_PATH).getValue())
                .outDirPath(settingMap.get(SystemSettingKeyEnum.EPISODE_RENAME_TASK_OUT_DIR_PATH).getValue())
                .build();
    }

    @Override
    public void updateEpisodeRenameTaskSettings(EpisodeRenameTaskSettings settings) {
        List<BaseSystemSetting> baseSystemSettings = List.of(
                new BaseSystemSetting(SystemSettingKeyEnum.EPISODE_RENAME_TASK_SOURCE_DIR_PATH, settings.sourceDirPath()),
                new BaseSystemSetting(SystemSettingKeyEnum.EPISODE_RENAME_TASK_OUT_DIR_PATH, settings.outDirPath())
        );

        systemSettingsService.saveSettings(baseSystemSettings);
    }
}
