package top.rainine.homebangumi.api.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.api.annotation.valid.ValidEnumValue;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.enums.ScheduledTaskIdEnum;

/**
 * @author rainine
 * @description
 * @date 2024/5/17 18:36:47
 */
@Getter
@Setter
@ToString
public class RunOnceScheduledTaskReq {

    /**
     * 执行定时任务
     * @see ScheduledTaskIdEnum#getTask()
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.SCHEDULED_TASK_ID_ENUM_INVALID)
    @ValidEnumValue(enumClass = ScheduledTaskIdEnum.class, message = HbCodeEnum.ErrorCode.SCHEDULED_TASK_ID_ENUM_INVALID)
    private Integer taskId;
}
