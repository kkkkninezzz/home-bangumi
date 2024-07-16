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
 * @description http类型的网络代理配置dto
 * @date 2024/5/9 18:57:32
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class HttpProxySettingsDto {

    /**
     * http代理的host
     * */
    @NotEmpty(message = HbCodeEnum.ErrorCode.NETWORK_PROXY_HOST_INVALID)
    private String host;

    /**
     * http代理的端口
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.NETWORK_PROXY_PORT_INVALID)
    @Min(value = 0, message = HbCodeEnum.ErrorCode.NETWORK_PROXY_PORT_INVALID)
    private Integer port;
}
