package top.rainine.homebangumi.core.event.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import top.rainine.homebangumi.dao.po.HbRssBangumi;

/**
 * @author rainine
 * @description rss订阅更新的事件
 * @date 2024/4/24 18:18:54
 */
@Getter
@ToString
@AllArgsConstructor
public class RssSubscriptionUpdatedEvent extends HbEvent {
    /**
     *  订阅有更新的rssBangumi {@link HbRssBangumi#getId()}
     * */
    private final Long rssBangumiId;
}
