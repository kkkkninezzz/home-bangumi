package top.rainine.homebangumi.core.settings.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.event.HbEventBus;
import top.rainine.homebangumi.core.event.data.WecomchanConfigModifiedEvent;
import top.rainine.homebangumi.core.settings.MessagePusherSettingsService;
import top.rainine.homebangumi.core.settings.SystemSettingsService;
import top.rainine.homebangumi.core.settings.data.BaseSystemSetting;
import top.rainine.homebangumi.core.settings.data.WecomchanSettings;
import top.rainine.homebangumi.def.enums.SystemSettingKeyEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author rainine
 * @description 消息推送的设置管理
 * @date 2024/8/20 18:08:23
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessagePusherSettingsServiceImpl implements MessagePusherSettingsService {

    private final SystemSettingsService systemSettingsService;

    private final HbEventBus eventBus;

    private static final List<SystemSettingKeyEnum> WECOMCHAN_KEYS = Arrays.asList(
            SystemSettingKeyEnum.MESSAGE_PUSHER_WECOMCHAN_ENABLE,
            SystemSettingKeyEnum.MESSAGE_PUSHER_WECOMCHAN_URL,
            SystemSettingKeyEnum.MESSAGE_PUSHER_WECOMCHAN_SEND_KEY);

    @Override
    public WecomchanSettings getWecomchanSettings() {
        Map<SystemSettingKeyEnum, BaseSystemSetting> settingMap = systemSettingsService.loadSettingMap(WECOMCHAN_KEYS);
        return WecomchanSettings
                .builder()
                .enable(settingMap.get(SystemSettingKeyEnum.MESSAGE_PUSHER_WECOMCHAN_ENABLE).getValue())
                .url(settingMap.get(SystemSettingKeyEnum.MESSAGE_PUSHER_WECOMCHAN_URL).getValue())
                .sendKey(settingMap.get(SystemSettingKeyEnum.MESSAGE_PUSHER_WECOMCHAN_SEND_KEY).getValue())
                .build();
    }

    @Override
    public void updateWecomchanSettings(WecomchanSettings settings) {
        List<BaseSystemSetting> baseSystemSettings = List.of(
                new BaseSystemSetting(SystemSettingKeyEnum.MESSAGE_PUSHER_WECOMCHAN_ENABLE, settings.enable()),
                new BaseSystemSetting(SystemSettingKeyEnum.MESSAGE_PUSHER_WECOMCHAN_URL, settings.url()),
                new BaseSystemSetting(SystemSettingKeyEnum.MESSAGE_PUSHER_WECOMCHAN_SEND_KEY, settings.sendKey())
        );

        systemSettingsService.saveSettings(baseSystemSettings);
        eventBus.publishEvent(WecomchanConfigModifiedEvent.DEFAULT);
    }
}



