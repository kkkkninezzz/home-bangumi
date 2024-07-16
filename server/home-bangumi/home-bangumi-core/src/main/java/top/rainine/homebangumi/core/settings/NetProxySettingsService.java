package top.rainine.homebangumi.core.settings;

import top.rainine.homebangumi.core.settings.data.NetProxySettings;

/**
 * @authoer rainine
 * @date 2024/3/15 00:46
 * @desc 代理配置的只读服务类
 *
 * 返回的所有配置项都是只读
 */
public interface NetProxySettingsService {

    /**
     * 获取代理配置
     * */
    NetProxySettings getProxySettings();

    /**
     * 更新代理配置
     * */
    void updateProxySettings(NetProxySettings settings);
}
