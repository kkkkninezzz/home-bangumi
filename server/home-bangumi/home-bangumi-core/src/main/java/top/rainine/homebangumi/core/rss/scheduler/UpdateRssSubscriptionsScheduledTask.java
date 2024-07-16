package top.rainine.homebangumi.core.rss.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.rss.RssSubscriptionUpdateService;
import top.rainine.homebangumi.core.scheduler.HbScheduledTask;
import top.rainine.homebangumi.core.settings.ScheduledTaskSettingsService;
import top.rainine.homebangumi.def.enums.ScheduledTaskIdEnum;

import java.time.Duration;

/**
 * @authoer rainine
 * @date 2024/4/23 00:12
 * @desc 定时更新rss bangumi
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateRssSubscriptionsScheduledTask implements HbScheduledTask {

    private final RssSubscriptionUpdateService updateService;

    private final ScheduledTaskSettingsService scheduledTaskSettingsService;

    @Override
    public ScheduledTaskIdEnum getTaskId() {
        return ScheduledTaskIdEnum.UPDATE_RSS_SUBSCRIPTION;
    }

    @Override
    public Runnable task() {
        return () -> {
            log.info("[UpdateRssSubscriptionsScheduledTask]execute update rss subscriptions task");
            updateService.updateAllRssSubscriptions();
        };
    }

    @Override
    public Trigger getTrigger() {
        return triggerContext -> {
            PeriodicTrigger trigger = new PeriodicTrigger(scheduledTaskSettingsService.getUpdateRssSubscriptionDuration());
            return trigger.nextExecution(triggerContext);
        };
    }


}
