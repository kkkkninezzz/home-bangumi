package top.rainine.homebangumi.core.episode.rename.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.rainine.homebangumi.core.episode.rename.EpisodeRenameTaskManager;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTask;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTaskItem;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskItemRepository;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskRepository;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskItemStatusEnum;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskStatusEnum;

import java.util.List;
import java.util.Optional;

/**
 * @author rainine
 * @description
 * @date 2024/7/31 18:47:20
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EpisodeRenameTaskManagerImpl implements EpisodeRenameTaskManager {

    private final HbEpisodeRenameTaskRepository taskRepository;

    private final HbEpisodeRenameTaskItemRepository taskItemRepository;

    @Override
    @Transactional
    public void submitTask(Long id) {
        Optional<HbEpisodeRenameTask> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            log.error("[EpisodeRenameTaskManager]submit task failed, task is null, taskId: {}", id);
            return;
        }

        HbEpisodeRenameTask task = taskOptional.get();
        if (EpisodeRenameTaskStatusEnum.FINISHED.equals(task.getTaskStatus())) {
            log.warn("[EpisodeRenameTaskManager]submit task failed, task is finished, taskId: {}", id);
            return;
        }

        if (EpisodeRenameTaskStatusEnum.NONE.equals(task.getTaskStatus())) {
            task.setTaskStatus(EpisodeRenameTaskStatusEnum.PENDING.getStatus());
            taskRepository.save(task);
        }

        List<HbEpisodeRenameTaskItem> parsedItems = taskItemRepository.findAllByTaskIdAndStatus(id, EpisodeRenameTaskItemStatusEnum.PARSED.getStatus());
        if (CollectionUtils.isNotEmpty(parsedItems)) {
            parsedItems.forEach(item -> item.setStatus(EpisodeRenameTaskItemStatusEnum.PENDING.getStatus()));
            taskItemRepository.saveAll(parsedItems);
        }

        // TODO 提交到线程池中
    }
}









