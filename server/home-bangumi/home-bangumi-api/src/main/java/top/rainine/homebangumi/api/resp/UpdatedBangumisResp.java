package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @authoer rainine
 * @date 2024/5/21 21:02
 * @desc
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UpdatedBangumisResp {

    private List<UpdatedDate2BangumisDto> bangumis;
}
