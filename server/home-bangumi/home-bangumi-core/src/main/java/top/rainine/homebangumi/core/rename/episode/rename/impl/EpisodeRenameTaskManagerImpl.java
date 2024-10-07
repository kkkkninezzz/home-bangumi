package top.rainine.homebangumi.core.rename.episode.rename.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.rainine.homebangumi.common.utils.HbDateUtils;
import top.rainine.homebangumi.core.rename.episode.rename.EpisodeRenameTaskManager;
import top.rainine.homebangumi.core.event.HbEventBus;
import top.rainine.homebangumi.core.event.data.EpisodeRenameTaskExecutionTimeTooLongEvent;
import top.rainine.homebangumi.core.common.utils.HbAdvisor;
import top.rainine.homebangumi.core.common.utils.SpringContextUtils;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTask;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTaskItem;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskItemRepository;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskRepository;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskItemStatusEnum;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskStatusEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * @author rainine
 * @description
 * @date 2024/7/31 18:47:20
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EpisodeRenameTaskManagerImpl implements EpisodeRenameTaskManager, HbAdvisor<EpisodeRenameTaskManagerImpl> {

    private final HbEpisodeRenameTaskRepository taskRepository;

    private final HbEpisodeRenameTaskItemRepository taskItemRepository;

    private final EpisodeRenameTaskExecutor renameTaskExecutor;

    private final HbEventBus hbEventBus;

    /**
     * 已经提交了的任务
     * key: 任务id
     * value: 提交时间
     * */
    private final ConcurrentMap<Long, LocalDateTime> submitTasks = new ConcurrentHashMap<>();

    @Override
    public synchronized void submitTask(Long id) {
        // 说明任务正在执行中
        if (submitTasks.containsKey(id)) {
            return;
        }

        boolean preSubmitTaskResult = self().preSubmitTask(id);
        if (!preSubmitTaskResult) {
            return;
        }

        renameTaskExecutor.asyncExecuteTask(id);
        submitTasks.put(id, HbDateUtils.now());
    }

    /**
     * 预提交任务，更新任务的前置状态
     * @return 如果预提交成功，返回true
     * */
    @Transactional
    public boolean preSubmitTask(Long id) {
        Optional<HbEpisodeRenameTask> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            log.error("[EpisodeRenameTaskManager]submit task failed, task is null, taskId: {}", id);
            return false;
        }

        HbEpisodeRenameTask task = taskOptional.get();
        if (EpisodeRenameTaskStatusEnum.FINISHED.equals(task.getTaskStatus())) {
            log.warn("[EpisodeRenameTaskManager]submit task failed, task is finished, taskId: {}", id);
            return false;
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

        return true;
    }

    @Override
    public void onTaskExecuteEnd(Long id) {
        log.info("[EpisodeRenameTaskManager]task execute end, taskId: {}", id);
        submitTasks.remove(id);
    }

    @Override
    @Async
    public void checkAllNotFinishedTasks() {
        List<HbEpisodeRenameTask> tasks = taskRepository.findAllByTaskStatusIn(EpisodeRenameTaskStatusEnum.NOT_FINISHED_VALUES);
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        LocalDateTime now = HbDateUtils.now();
        submitTasks.forEach((taskId, startTime) -> {
            // 如果任务2个小时还没有结束，那么视为执行时间过长
            if (startTime.plusHours(2).isBefore(now)) {
                hbEventBus.publishEvent(new EpisodeRenameTaskExecutionTimeTooLongEvent(taskId, startTime));
            }
        });

        EpisodeRenameTaskManager taskManager = SpringContextUtils.getBean(EpisodeRenameTaskManager.class);
        tasks.forEach(task -> taskManager.submitTask(task.getId()));
    }

    @Override
    public void onTaskExecuteFailed(Long id) {
        log.info("[EpisodeRenameTaskManager]task execute failed, taskId: {}", id);
        submitTasks.remove(id);
    }
}









