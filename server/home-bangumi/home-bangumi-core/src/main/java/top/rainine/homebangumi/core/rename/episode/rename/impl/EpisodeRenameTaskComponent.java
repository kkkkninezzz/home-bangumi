package top.rainine.homebangumi.core.rename.episode.rename.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTask;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskItemRepository;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskRepository;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskItemStatusEnum;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.util.Arrays;
import java.util.List;

/**
 * @author rainine
 * @description
 * @date 2024/8/15 11:28:10
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EpisodeRenameTaskComponent {

    private final HbEpisodeRenameTaskRepository taskRepository;

    private final HbEpisodeRenameTaskItemRepository taskItemRepository;

    /**
     * 获取任务，不存在则抛出异常
     * */
    public HbEpisodeRenameTask getTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new HbBizException(HbCodeEnum.EPISODE_RENAME_TASK_NOT_EXISTS));
    }

    private static final List<Integer> TOTAL_COUNT_STATUS_LIST = Arrays.asList(
            EpisodeRenameTaskItemStatusEnum.NONE.getStatus(),
            EpisodeRenameTaskItemStatusEnum.PARSED.getStatus(),
            EpisodeRenameTaskItemStatusEnum.PENDING.getStatus(),
            EpisodeRenameTaskItemStatusEnum.SUCCESS.getStatus(),
            EpisodeRenameTaskItemStatusEnum.TITLE_PARSE_FAILED.getStatus(),
            EpisodeRenameTaskItemStatusEnum.FAILED.getStatus()
    );

    public long countTotalOfTaskItems(Long taskId) {
        return taskItemRepository.countByTaskIdAndStatusIn(taskId, TOTAL_COUNT_STATUS_LIST);
    }

    public long countPendingOfTaskItems(Long taskId) {
        return taskItemRepository.countByTaskIdAndStatus(taskId, EpisodeRenameTaskItemStatusEnum.PENDING.getStatus());
    }

    public long countSuccessOfTaskItems(Long taskId) {
        return taskItemRepository.countByTaskIdAndStatus(taskId, EpisodeRenameTaskItemStatusEnum.SUCCESS.getStatus());
    }

    public long countFailedOfTaskItems(Long taskId) {
        return taskItemRepository.countByTaskIdAndStatus(taskId, EpisodeRenameTaskItemStatusEnum.FAILED.getStatus());
    }
}
