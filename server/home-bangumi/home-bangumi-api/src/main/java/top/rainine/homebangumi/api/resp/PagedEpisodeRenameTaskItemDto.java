package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskStatusEnum;

/**
 * @author rainine
 * @description 分页用的重命名任务item
 * @date 2024/7/17 16:42:23
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class PagedEpisodeRenameTaskItemDto {
    /**
     * 任务id
     * */
    private Long id;

    /**
     * 任务名称
     * */
    private String taskName;

    /**
     * 任务状态
     * @see EpisodeRenameTaskStatusEnum#getStatus()
     * */
    private Integer taskStatus;

    /**
     * 所有的任务项数量
     * */
    private Long totalCount;

    /**
     * 等待处理的数量
     * */
    private Long pendingCount;

    /**
     * 成功的数量
     * */
    private Long successCount;

    /**
     * 失败的数量
     * */
    private Long failedCount;
}
