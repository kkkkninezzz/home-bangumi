package top.rainine.homebangumi.api.common;

import jakarta.validation.constraints.Min;
import lombok.*;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @author rainine
 * @description 番剧信息
 * @date 2024/4/8 17:58:23
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BangumiInfoDto {

    /**
     * 番剧名
     * */
    private String title;

    /**
     * 番剧的海报地址
     * 如果获取失败，使用一个默认值
     * */
    private String posterUrl;

    /**
     * 放送的星期几
     * 从1开始到7
     * 如果为null，那么认为暂无信息
     * */
    @Min(value = 1, message = HbCodeEnum.ErrorCode.BANGUMI_BROADCAST_DAY_OF_WEEK_INVALID)
    private Integer broadcastDayOfWeek;

    /**
     * 开始放送的日期
     * 毫秒时间戳
     * */
    private Long broadcastDate;

    /**
     * 放送的是哪一季
     * */
    @Min(value = 0, message = HbCodeEnum.ErrorCode.BANGUMI_SEASON_INVALID)
    private Integer season;

}
