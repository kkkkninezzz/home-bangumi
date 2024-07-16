package top.rainine.homebangumi.core.settings;

import top.rainine.homebangumi.core.settings.data.BaseSystemSetting;
import top.rainine.homebangumi.def.enums.SystemSettingKeyEnum;

import java.util.List;
import java.util.Map;

/**
 * @authoer rainine
 * @date 2024/3/15 00:45
 * @desc 系统配置的服务类
 */
public interface SystemSettingsService {

    /**
     * 获取配置信息
     * @param key 配置key
     * @return 返回配置信息，如果未设置过，将返回默认值
     * */
    BaseSystemSetting getSetting(SystemSettingKeyEnum key);

    /**
     * 获取多个配置信息
     * @param keys 配置key列表
     * @return 返回配置信息列表，如果对应的key未设置过，将返回默认值
     * */
    List<BaseSystemSetting> loadSettings(List<SystemSettingKeyEnum> keys);

    /**
     * 获取多个配置信息map
     * @param keys 配置key列表
     * @return 返回配置信息列表，如果对应的key未设置过，将返回默认值.
     * */
    Map<SystemSettingKeyEnum, BaseSystemSetting> loadSettingMap(List<SystemSettingKeyEnum> keys);

    /**
     * 保存配置
     * */
    void saveSetting(BaseSystemSetting setting);

    /**
     * 保存配置
     * */
    void saveSettings(Map<SystemSettingKeyEnum, BaseSystemSetting> settingMap);


    /**
     * 保存配置
     * */
    void saveSettings(List<BaseSystemSetting> settings);
}
