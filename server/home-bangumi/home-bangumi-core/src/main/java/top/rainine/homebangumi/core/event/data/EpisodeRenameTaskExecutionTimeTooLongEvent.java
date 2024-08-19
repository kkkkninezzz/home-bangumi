package top.rainine.homebangumi.core.event.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTask;

import java.time.LocalDateTime;

/**
 * @author rainine
 * @description 重命名任务执行时间过长事件
 * @date 2024/8/15 10:53:41
 */
@Getter
@ToString
@AllArgsConstructor
public class EpisodeRenameTaskExecutionTimeTooLongEvent extends HbEvent {

    /**
     * 任务id
     * @see HbEpisodeRenameTask#getId()
     * */
    private Long taskId;

    /**
     * 任务的开始时间
     * */
    private LocalDateTime startTime;

}
