package top.rainine.homebangumi.api.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.api.annotation.valid.ValidEnumValue;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskStatusEnum;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @author rainine
 * @description 获取分页的任务请求条件
 * @date 2024/7/17 16:29:11
 */
@Getter
@Setter
@ToString
public class LoadPagedTasksReqConditionDto {

    /**
     * 任务名，支持模糊查询
     * */
    private String taskName;

    /**
     * 任务状态
     * @see EpisodeRenameTaskStatusEnum#getStatus()
     * */
    @ValidEnumValue(enumClass = EpisodeRenameTaskStatusEnum.class, message = HbCodeEnum.ErrorCode.EPISODE_RENAME_TASK_STATUS_INVALID)
    private Integer taskStatus;

}
