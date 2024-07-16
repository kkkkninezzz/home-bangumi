package top.rainine.homebangumi.api.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @authoer rainine
 * @date 2024/4/29 22:52
 * @desc 分页的请求
 */
@Getter
@Setter
@ToString
public class PagedReq<T> {

    /**
     * 当前页
     * 从1开始
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.CURRENT_PAGE_INVALID)
    @Min(value = 1, message = HbCodeEnum.ErrorCode.CURRENT_PAGE_INVALID)
    private Integer current;

    /**
     * 每页大小
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.PAGE_SIZE_INVALID)
    @Min(value = 1, message = HbCodeEnum.ErrorCode.CURRENT_PAGE_INVALID)
    private Integer pageSize;

    /**
     * 查询条件
     * */
    @Valid
    @NotNull
    private T condition;
}
