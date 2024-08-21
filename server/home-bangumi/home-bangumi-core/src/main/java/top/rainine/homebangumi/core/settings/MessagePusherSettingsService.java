package top.rainine.homebangumi.core.settings;

import top.rainine.homebangumi.core.settings.data.WecomchanSettings;

/**
 * @author rainine
 * @description 消息推送的设置管理
 * @date 2024/8/20 17:55:11
 */
public interface MessagePusherSettingsService {

    /**
     * 获取wecomchan的配置
     * */
    WecomchanSettings getWecomchanSettings();

    /**
     * 更新设置
     * */
    void updateWecomchanSettings(WecomchanSettings settings);
}
