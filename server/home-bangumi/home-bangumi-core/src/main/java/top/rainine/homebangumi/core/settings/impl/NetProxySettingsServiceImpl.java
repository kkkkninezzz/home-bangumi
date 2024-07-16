package top.rainine.homebangumi.core.settings.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.event.HbEventBus;
import top.rainine.homebangumi.core.event.data.NetProxySettingsModifiedEvent;
import top.rainine.homebangumi.core.settings.NetProxySettingsService;
import top.rainine.homebangumi.core.settings.SystemSettingsService;
import top.rainine.homebangumi.core.settings.data.BaseSystemSetting;
import top.rainine.homebangumi.core.settings.data.HttpProxySettings;
import top.rainine.homebangumi.core.settings.data.NetProxySettings;
import top.rainine.homebangumi.core.settings.data.Socks5ProxySettings;
import top.rainine.homebangumi.def.enums.NetProxyTypeEnum;
import top.rainine.homebangumi.def.enums.SystemSettingKeyEnum;

import java.util.*;

/**
 * @author rainine
 * @description 网络代理配置的只读服务类
 * @date 2024/3/15 11:45:58
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NetProxySettingsServiceImpl implements NetProxySettingsService {

    private final SystemSettingsService systemSettingsService;

    private final HbEventBus eventBus;

    private static final List<SystemSettingKeyEnum> BASE_KEYS = Arrays.asList(
            SystemSettingKeyEnum.NET_PROXY_ENABLED,
            SystemSettingKeyEnum.NET_PROXY_TYPE);

    private static final List<SystemSettingKeyEnum> HTTP_KEYS = Arrays.asList(
            SystemSettingKeyEnum.HTTP_NET_PROXY_HOST,
            SystemSettingKeyEnum.HTTP_NET_PROXY_PORT);

    private static final List<SystemSettingKeyEnum> SOCKS_KEYS = Arrays.asList(
            SystemSettingKeyEnum.SOCKS5_NET_PROXY_HOST,
            SystemSettingKeyEnum.SOCKS5_NET_PROXY_PORT,
            SystemSettingKeyEnum.SOCKS5_NET_PROXY_USERNAME,
            SystemSettingKeyEnum.SOCKS5_NET_PROXY_PASSWORD);

    @Override
    public NetProxySettings getProxySettings() {
        Map<SystemSettingKeyEnum, BaseSystemSetting> settingMap = systemSettingsService.loadSettingMap(BASE_KEYS);

        NetProxySettings.NetProxySettingsBuilder builder = NetProxySettings.builder();
        boolean enable = settingMap.get(SystemSettingKeyEnum.NET_PROXY_ENABLED).getValue();
        builder.enable(enable)
                .proxyType(NetProxyTypeEnum.NONE);

        if (enable) {
            Integer type = settingMap.get(SystemSettingKeyEnum.NET_PROXY_TYPE).getValue();
            NetProxyTypeEnum netProxyTypeEnum = NetProxyTypeEnum.of(type);
            builder.proxyType(netProxyTypeEnum);

            switch (netProxyTypeEnum) {
                case HTTP -> builder.httpProxySettings(loadHttpProxySettings());
                case SOCKS5 -> builder.socks5ProxySettings(loadSocks5ProxySettings());
            }
        }

        return builder.build();
    }

    /**
     * 获取http代理配置
     * */
    private HttpProxySettings loadHttpProxySettings() {
        Map<SystemSettingKeyEnum, BaseSystemSetting> settingMap = systemSettingsService.loadSettingMap(HTTP_KEYS);

        String host = settingMap.get(SystemSettingKeyEnum.HTTP_NET_PROXY_HOST).getValue();
        int port = settingMap.get(SystemSettingKeyEnum.HTTP_NET_PROXY_PORT).getValue();

        return HttpProxySettings.builder()
                .host(host)
                .port(port)
                .build();
    }

    /**
     * 获取socks5代理配置
     * */
    private Socks5ProxySettings loadSocks5ProxySettings() {
        Map<SystemSettingKeyEnum, BaseSystemSetting> settingMap = systemSettingsService.loadSettingMap(SOCKS_KEYS);

        String host = settingMap.get(SystemSettingKeyEnum.SOCKS5_NET_PROXY_HOST).getValue();
        int port = settingMap.get(SystemSettingKeyEnum.SOCKS5_NET_PROXY_PORT).getValue();
        String username = settingMap.get(SystemSettingKeyEnum.SOCKS5_NET_PROXY_USERNAME).getValue();
        String password = settingMap.get(SystemSettingKeyEnum.SOCKS5_NET_PROXY_PASSWORD).getValue();

        return Socks5ProxySettings.builder()
                .host(host)
                .port(port)
                .username(username)
                .password(password)
                .build();
    }

    @Override
    public void updateProxySettings(NetProxySettings settings) {
        List<BaseSystemSetting> baseSystemSettings = new ArrayList<>();
        baseSystemSettings.add(new BaseSystemSetting(SystemSettingKeyEnum.NET_PROXY_ENABLED, settings.enable()));
        baseSystemSettings.add(new BaseSystemSetting(SystemSettingKeyEnum.NET_PROXY_TYPE, settings.proxyType().getType()));

        List<BaseSystemSetting> httpBaseSettings = Optional.ofNullable(settings.httpProxySettings())
                .map(httpProxySettings -> List.of(
                        new BaseSystemSetting(SystemSettingKeyEnum.HTTP_NET_PROXY_HOST, httpProxySettings.host()),
                        new BaseSystemSetting(SystemSettingKeyEnum.HTTP_NET_PROXY_PORT, httpProxySettings.port())
                )).orElseGet(List::of);
        baseSystemSettings.addAll(httpBaseSettings);

        List<BaseSystemSetting> socks5BaseSettings = Optional.ofNullable(settings.socks5ProxySettings())
                .map(socks5ProxySettings -> List.of(
                        new BaseSystemSetting(SystemSettingKeyEnum.SOCKS5_NET_PROXY_HOST, socks5ProxySettings.host()),
                        new BaseSystemSetting(SystemSettingKeyEnum.SOCKS5_NET_PROXY_PORT, socks5ProxySettings.port()),
                        new BaseSystemSetting(SystemSettingKeyEnum.SOCKS5_NET_PROXY_USERNAME, socks5ProxySettings.username()),
                        new BaseSystemSetting(SystemSettingKeyEnum.SOCKS5_NET_PROXY_PASSWORD, socks5ProxySettings.password())
                )).orElseGet(List::of);
        baseSystemSettings.addAll(socks5BaseSettings);

        systemSettingsService.saveSettings(baseSystemSettings);

        // 发布更新事件
        eventBus.publishEvent(NetProxySettingsModifiedEvent.DEFAULT);
    }
}

























