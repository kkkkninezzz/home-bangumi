package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @authoer rainine
 * @date 2024/5/19 23:07
 * @desc 在首页的统计的信息
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class RssBangumiStatisticOnHomeResp {

    /**
     * 下载中的剧集数量
     * */
    private Long downloadingEpisodesCount;

    /**
     * 各种原因失败的剧集数量
     * */
    private Long failedEpisodesCount;

    /**
     * 一周内有更新的bangumi数量
     * */
    private Long updatedBangumiCountInWeek;

    /**
     * 今天更新的bangumi数量
     * */
    private Long updatedBangumiCountToday;

    /**
     * 当前季度订阅的数量
     * */
    private Long subscriptionsCountInCurQuarter;

    /**
     * 前一个季度订阅的数量
     * */
    private Long subscriptionsCountInPreQuarter;

    /**
     * 收集的番剧数量
     * */
    private Long collectedBangumiCount;

    /**
     * 今日收集的番剧数量
     * */
    private Long collectedBangumiCountToday;
}
