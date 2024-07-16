package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @authoer rainine
 * @date 2024/5/21 21:03
 * @desc
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UpdatedBangumiDto {
    /**
     * rss bangumi id
     * */
    private Long rssBangumiId;


    /**
     * 番剧标题
     * */
    private String bangumiTitle;

    /**
     * 番剧海报地址
     * */
    private String bangumiPosterUrl;

    /**
     * 更新的最新一集剧集编号
     * */
    private Integer latestEpisodeNo;

    /**
     * 更新的时间
     * */
    private Long latestUpdatedTime;

    /**
     * 预计播放的星期
     * */
    private Integer expectedBroadcastDayOfWeek;

    /**
     * 实际更新的星期
     * */
    private Integer actualBroadcastDayOfWeek;

    /**
     * 是否延迟
     * */
    private Boolean delay;
}
