package top.rainine.homebangumi.api.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @authoer rainine
 * @date 2024/4/10 20:28
 * @desc
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PreviewRssBangumiResp {

    /**
     * rss bangumi的id
     * */
    private Long id;
}
