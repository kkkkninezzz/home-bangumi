package top.rainine.homebangumi.core.settings.data;

import lombok.Builder;
import top.rainine.homebangumi.def.enums.NetProxyTypeEnum;

/**
 * @param enable            代理是否启用
 * @param proxyType         网络代理的类型
 * @param httpProxySettings   http类型的代理配置
 * @param socks5ProxySettings socks5的代理配置
 * @authoer rainine
 * @date 2024/3/15 00:47
 * @desc 代理配置
 */
@Builder
public record NetProxySettings(boolean enable,
                               NetProxyTypeEnum proxyType,
                               HttpProxySettings httpProxySettings,
                               Socks5ProxySettings socks5ProxySettings) {

}
