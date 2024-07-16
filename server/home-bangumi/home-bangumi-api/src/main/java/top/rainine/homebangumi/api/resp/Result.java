package top.rainine.homebangumi.api.resp;

import lombok.Builder;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

/**
 * @author rainine
 * @description 返回结果
 * @date 2024/3/21 18:40:45
 */
@Builder
public record Result<T>(String code, String msg, T data) {

    public boolean isSuccess() {
        return HbCodeEnum.SUCCESS.getCode().equals(code);
    }

    /**
     * 创建返回对象
     */
    public static <T> Result<T> create(String code, String msg, T data) {
        return Result.<T>builder()
                .code(code)
                .msg(msg)
                .data(data)
                .build();
    }


    /**
     * 创建成功返回对象
     */
    public static <T> Result<T> createSuccess(T data) {
        return create(HbCodeEnum.SUCCESS.getCode(), HbCodeEnum.SUCCESS.getMsg(), data);
    }


    /**
     * 创建成功返回对象
     */
    public static <T> Result<T> createSuccess() {
        return createSuccess(null);
    }

    /**
     * 创建失败返回对象
     */
    public static <T> Result<T> createError(String code, String msg) {
        return create(code, msg, null);
    }

    /**
     * 创建失败返回对象
     */
    public static <T> Result<T> createError(HbBizException e) {
        return createError(e.getCode(), e.getMessage());
    }

    /**
     * 创建失败返回对象
     */
    public static <T> Result<T> createError(HbCodeEnum code) {
        return createError(code.getCode(), code.getMsg());
    }
}
