package top.rainine.homebangumi.core.scheduler;

import org.springframework.scheduling.Trigger;
import top.rainine.homebangumi.def.enums.ScheduledTaskIdEnum;

/**
 * @author rainine
 * @description 定时任务
 * @date 2024/4/22 18:13:58
 */
public interface HbScheduledTask {

    /**
     * 获取task唯一id
     * */
    ScheduledTaskIdEnum getTaskId();

    /**
     * 定时任务执行的逻辑
     * */
    Runnable task();

    /**
     * 触发器
     * */
    Trigger getTrigger();
}
