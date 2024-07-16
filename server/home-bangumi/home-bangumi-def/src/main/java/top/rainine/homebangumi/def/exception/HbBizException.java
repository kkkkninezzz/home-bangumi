package top.rainine.homebangumi.def.exception;

import lombok.Getter;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @author rainine
 * @description hb定义的业务异常
 * @date 2024/3/15 16:14:47
 */
@Getter
public class HbBizException extends RuntimeException {

    private String code;

    public HbBizException() {
    }

    public HbBizException(HbCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.code = codeEnum.getCode();
    }
}
