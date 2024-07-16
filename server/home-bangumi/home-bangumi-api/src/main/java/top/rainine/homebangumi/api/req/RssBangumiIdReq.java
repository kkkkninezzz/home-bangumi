package top.rainine.homebangumi.api.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @author rainine
 * @description rss bangumi id的请求
 * @date 2024/4/15 16:37:55
 */
@Getter
@Setter
@ToString
public class RssBangumiIdReq {

    @NotNull(message = HbCodeEnum.ErrorCode.RSS_BANGUMI_ID_INVALID)
    @Min(value = 0, message = HbCodeEnum.ErrorCode.RSS_BANGUMI_ID_INVALID)
    private Long id;
}
