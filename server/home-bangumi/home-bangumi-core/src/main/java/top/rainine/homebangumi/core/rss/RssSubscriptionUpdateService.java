package top.rainine.homebangumi.core.rss;

import top.rainine.homebangumi.dao.po.HbRssBangumi;

/**
 * @authoer rainine
 * @date 2024/4/23 00:17
 * @desc rss 订阅更新服务
 */
public interface RssSubscriptionUpdateService {

    /**
     * 更新所有的rss订阅
     * */
    void updateAllRssSubscriptions();

    /**
     * 更新rss订阅
     * @param rssBangumiId {@link HbRssBangumi#getId()}
     * */
    void updateRssSubscription(Long rssBangumiId);
}
