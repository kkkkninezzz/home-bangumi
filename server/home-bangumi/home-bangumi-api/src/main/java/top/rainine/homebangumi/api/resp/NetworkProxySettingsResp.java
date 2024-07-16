package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.rainine.homebangumi.api.common.HttpProxySettingsDto;
import top.rainine.homebangumi.api.common.Socks5ProxySettingsDto;
import top.rainine.homebangumi.def.enums.NetProxyTypeEnum;

/**
 * @author rainine
 * @description 网络代理配置返回
 * @date 2024/5/9 18:17:23
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class NetworkProxySettingsResp {

    /**
     * 是否开启网络代理
     * */
    private Boolean enable;

    /**
     * 网络代理类型
     * @see NetProxyTypeEnum#getType()
     * */
    private Integer networkProxyType;

    /**
     * http类型的网络代理，当且仅当{@link NetworkProxySettingsResp#networkProxyType} == {@link NetProxyTypeEnum#HTTP}
     * */
    private HttpProxySettingsDto httpProxySettings;

    /**
     * socks5类型的网络代理，当且仅当{@link NetworkProxySettingsResp#networkProxyType} == {@link NetProxyTypeEnum#SOCKS5}
     * */
    private Socks5ProxySettingsDto socks5ProxySettings;
}
