package top.rainine.homebangumi.core.event.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTask;

/**
 * @author rainine
 * @description 重命名任务执行失败事件
 * @date 2024/8/1 16:55:47
 */
@Getter
@ToString
@AllArgsConstructor
public class EpisodeRenameTaskExecuteFailedEvent extends HbEvent {
    /**
     * 任务id
     * @see HbEpisodeRenameTask#getId()
     * */
    private final Long taskId;
}
