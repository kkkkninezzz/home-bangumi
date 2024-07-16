package top.rainine.homebangumi.api.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.api.annotation.valid.ValidEnumValue;
import top.rainine.homebangumi.api.common.HttpProxySettingsDto;
import top.rainine.homebangumi.api.common.Socks5ProxySettingsDto;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.enums.NetProxyTypeEnum;

/**
 * @author rainine
 * @description 更新网络代理配置请求
 * @date 2024/5/9 18:54:40
 */
@Getter
@Setter
@ToString
public class UpdateNetworkProxySettingsReq {
    /**
     * 是否开启网络代理
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.NETWORK_PROXY_ENABLE_INVALID)
    private Boolean enable;

    /**
     * 网络代理类型
     * @see NetProxyTypeEnum#getType()
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.NETWORK_PROXY_TYPE_INVALID)
    @ValidEnumValue(enumClass = NetProxyTypeEnum.class, message = HbCodeEnum.ErrorCode.NETWORK_PROXY_TYPE_INVALID)
    private Integer networkProxyType;

    /**
     * http类型的网络代理，当且仅当{@link UpdateNetworkProxySettingsReq#networkProxyType} == {@link NetProxyTypeEnum#HTTP}
     * */
    @Valid
    private HttpProxySettingsDto httpProxySettings;

    /**
     * socks5类型的网络代理，当且仅当{@link UpdateNetworkProxySettingsReq#networkProxyType} == {@link NetProxyTypeEnum#SOCKS5}
     * */
    @Valid
    private Socks5ProxySettingsDto socks5ProxySettings;
}
