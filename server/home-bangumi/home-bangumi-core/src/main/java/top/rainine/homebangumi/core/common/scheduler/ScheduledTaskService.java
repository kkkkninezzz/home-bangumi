package top.rainine.homebangumi.core.common.scheduler;

import top.rainine.homebangumi.def.enums.ScheduledTaskIdEnum;

/**
 * @author rainine
 * @description 定时任务管理器
 * @date 2024/4/22 18:21:02
 */
public interface ScheduledTaskService {

    void addTask(HbScheduledTask task);

    /**
     * 执行一次定时任务
     * */
    void runOnceTask(ScheduledTaskIdEnum task);
}
