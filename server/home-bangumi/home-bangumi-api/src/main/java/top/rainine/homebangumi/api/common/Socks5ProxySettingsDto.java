package top.rainine.homebangumi.api.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @author rainine
 * @description socks5类型的网络代理配置dto
 * @date 2024/5/9 18:57:32
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Socks5ProxySettingsDto {
    /**
     * 主机地址
     * */
    @NotEmpty(message = HbCodeEnum.ErrorCode.NETWORK_PROXY_HOST_INVALID)
    private String host;

    /**
     * 端口号
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.NETWORK_PROXY_PORT_INVALID)
    @Min(value = 0, message = HbCodeEnum.ErrorCode.NETWORK_PROXY_PORT_INVALID)
    private Integer port;

    /**
     * socks5代理的用户名
     * 如果为空，表示无需验证
     * */
    private String username;

    /**
     * socks5代理的密码
     * */
    private String password;
}
