package top.rainine.homebangumi.api.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @author rainine
 * @description 密码登录请求
 * @date 2024/3/21 18:55:13
 */
@Getter
@Setter
@ToString
public class LoginByPasswordReq {
    /**
     * 账号
     * */
    @NotBlank(message = HbCodeEnum.ErrorCode.ACCOUNT_NOT_EMPTY)
    private String username;

    /**
     * 密码
     * */
    @NotBlank(message = HbCodeEnum.ErrorCode.PASSWORD_NOT_EMPTY)
    private String password;
}
