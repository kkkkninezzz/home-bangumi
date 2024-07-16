package top.rainine.homebangumi.core.settings.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.common.utils.GsonUtils;
import top.rainine.homebangumi.core.settings.EpisodeFilterRulesSettingsService;
import top.rainine.homebangumi.core.settings.SystemSettingsService;
import top.rainine.homebangumi.core.settings.data.BaseSystemSetting;
import top.rainine.homebangumi.core.settings.data.EpisodeFilterRulesSettings;
import top.rainine.homebangumi.def.enums.SystemSettingKeyEnum;

import java.util.List;
import java.util.Map;

/**
 * @author rainine
 * @description 实现类
 * @date 2024/5/14 10:36:08
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EpisodeFilterRulesSettingsServiceImpl implements EpisodeFilterRulesSettingsService {

    private final SystemSettingsService systemSettingsService;

    private static final List<SystemSettingKeyEnum> KEYS = List.of(
            SystemSettingKeyEnum.EPISODE_FILTER_RULES);

    @Override
    public EpisodeFilterRulesSettings getSettings() {
        Map<SystemSettingKeyEnum, BaseSystemSetting> settingMap = systemSettingsService.loadSettingMap(KEYS);
        BaseSystemSetting baseSystemSetting = settingMap.get(SystemSettingKeyEnum.EPISODE_FILTER_RULES);

        List<String> rules = StringUtils.isBlank(baseSystemSetting.getValue()) ? List.of() : GsonUtils.toList(baseSystemSetting.getValue(), String.class);
        return EpisodeFilterRulesSettings
                .builder()
                .rules(rules)
                .build();
    }

    @Override
    public void updateSettings(EpisodeFilterRulesSettings settings) {
        List<String> rules = settings.rules();
        String rulesSettingValue = "";
        if (CollectionUtils.isNotEmpty(rules)) {
            rulesSettingValue = GsonUtils.toJson(rules);
        }

        List<BaseSystemSetting> baseSystemSettings = List.of(
                new BaseSystemSetting(SystemSettingKeyEnum.EPISODE_FILTER_RULES, rulesSettingValue)
        );

        systemSettingsService.saveSettings(baseSystemSettings);
    }
}















