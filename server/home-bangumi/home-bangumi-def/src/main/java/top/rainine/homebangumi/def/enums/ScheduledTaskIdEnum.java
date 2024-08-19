package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.rainine.homebangumi.def.utils.EnumUtils;

/**
 * @author rainine
 * @description 定时任务枚举id
 * @date 2024/5/17 18:32:51
 */
@Getter
@AllArgsConstructor
public enum ScheduledTaskIdEnum {
    CHECK_EPISODE_DOWNLOAD(1),

    PUSH_PARSED_EPISODES_TO_DOWNLOADER(2),

    RENAME_EPISODES(3),

    UPDATE_RSS_SUBSCRIPTION(4),

    CHECK_NOT_FINISHED_RENAME_TASK(5),

    ;

    private final int task;

    public static final ScheduledTaskIdEnum[] ENUMS = values();

    public static boolean contains(Integer task) {
        return EnumUtils.contains(ENUMS, ScheduledTaskIdEnum::getTask, task);
    }

    public static ScheduledTaskIdEnum of(Integer task) {
        return EnumUtils.ofInteger(ENUMS, ScheduledTaskIdEnum::getTask, task, null);
    }
}
