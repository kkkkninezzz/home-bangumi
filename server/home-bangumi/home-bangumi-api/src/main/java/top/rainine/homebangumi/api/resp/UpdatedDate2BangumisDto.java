package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @authoer rainine
 * @date 2024/5/22 00:39
 * @desc
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UpdatedDate2BangumisDto {
    /**
     * 更新的日期，0点时间
     * */
    private Long date;

    /**
     * 星期几
     * */
    private Integer dayOfWeek;

    private List<UpdatedBangumiDto> bangumis;
}
