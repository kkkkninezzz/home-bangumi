package top.rainine.homebangumi.web.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.rainine.homebangumi.core.settings.NetProxySettingsService;
import top.rainine.homebangumi.core.settings.data.NetProxySettings;

/**
 * @author rainine
 * @description
 * @date 2024/3/15 17:54:36
 */
public class NetProxyServiceTests extends AbstractTests {

    private final NetProxySettingsService netProxySettingsService;

    @Autowired
    public NetProxyServiceTests(NetProxySettingsService netProxySettingsService) {
        this.netProxySettingsService = netProxySettingsService;
    }

    @Test
    public void testGetNetProxyConfig() {
        NetProxySettings proxyConfig = netProxySettingsService.getProxySettings();
        System.out.println(proxyConfig);
    }
}
