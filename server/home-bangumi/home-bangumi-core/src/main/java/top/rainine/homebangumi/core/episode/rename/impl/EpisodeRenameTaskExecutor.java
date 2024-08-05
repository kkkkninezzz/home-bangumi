package top.rainine.homebangumi.core.episode.rename.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.event.HbEventBus;
import top.rainine.homebangumi.core.event.data.EpisodeRenameFinishedEvent;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTask;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTaskItem;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskItemRepository;
import top.rainine.homebangumi.dao.repository.HbEpisodeRenameTaskRepository;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskItemStatusEnum;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskStatusEnum;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

/**
 * @author rainine
 * @description 重命名任务执行器
 * @date 2024/7/31 18:43:44
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EpisodeRenameTaskExecutor {

    private final HbEpisodeRenameTaskRepository taskRepository;

    private final HbEpisodeRenameTaskItemRepository taskItemRepository;

    private final HbEventBus hbEventBus;

    /**
     * 异步执行任务
     * */
    @Async
    public void asyncExecuteTask(Long taskId) {
        Optional<HbEpisodeRenameTask> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            log.error("[EpisodeRenameTaskExecutor]task not exists, taskId: {}", taskId);
            return;
        }

        HbEpisodeRenameTask task = taskOptional.get();

        if (!EpisodeRenameTaskStatusEnum.FINISHED.equals(task.getTaskStatus())) {
            log.error("[EpisodeRenameTaskExecutor]task is finished, taskId: {}", taskId);
            return;
        }

        task.setTaskStatus(EpisodeRenameTaskStatusEnum.PROCESSING.getStatus());
        taskRepository.save(task);

        boolean deleteSourceFile = Optional.ofNullable(task.getDeleteSourceFile()).orElse(false);
        boolean overwriteExistingFile = Optional.ofNullable(task.getOverwriteExistingFile()).orElse(false);

        List<HbEpisodeRenameTaskItem> taskItems = taskItemRepository.findAllByTaskIdAndStatus(taskId, EpisodeRenameTaskItemStatusEnum.PENDING.getStatus());
        taskItems.forEach(item -> {
            try {
                handleTaskItem(item, deleteSourceFile, overwriteExistingFile);
            } catch (Exception e) {
                log.error("[EpisodeRenameTaskExecutor]task item execute failed, taskId: {}, itemId: {}", taskId, item.getId());
                item.setStatus(EpisodeRenameTaskItemStatusEnum.FAILED.getStatus());
                item.setErrorMessage(STR."rename file happen exception.\n\{e.getMessage()}");
                taskItemRepository.save(item);
            }
        });

        // 更新为结束状态
        task.setTaskStatus(EpisodeRenameTaskStatusEnum.FINISHED.getStatus());
        taskRepository.save(task);

        hbEventBus.publishEvent(new EpisodeRenameFinishedEvent(taskId));
    }

    private void handleTaskItem(HbEpisodeRenameTaskItem taskItem, boolean deleteSourceFile, boolean overwriteExistingFile) {
        Path renamedEpisodeOutputPath = Paths.get(taskItem.getRenamedEpisodeOutputPath());

        // 如果输出路径已经存在且不允许覆盖文件，那么视为结束
        if (Files.exists(renamedEpisodeOutputPath) && !overwriteExistingFile) {
            taskItem.setStatus(EpisodeRenameTaskItemStatusEnum.SKIPPED.getStatus());
            taskItem.setErrorMessage("renamed file is exists, skip it");
            taskItemRepository.save(taskItem);
            return;
        }

        Path outputPathParent = renamedEpisodeOutputPath.getParent();
        if (Files.notExists(outputPathParent)) {
            try {
                Files.createDirectories(outputPathParent);
            } catch (IOException e) {
                taskItem.setStatus(EpisodeRenameTaskItemStatusEnum.FAILED.getStatus());
                taskItem.setErrorMessage("create output parent dir failed");
                taskItemRepository.save(taskItem);
                return;
            }
        }


        Path episodePath = Paths.get(taskItem.getEpisodePath());
        if (Files.notExists(episodePath)) {
            taskItem.setStatus(EpisodeRenameTaskItemStatusEnum.FAILED.getStatus());
            taskItem.setErrorMessage("episode file is not exists");
            taskItemRepository.save(taskItem);
            return;
        }

        // 如果要删除源文件，那么直接使用move
        if (deleteSourceFile) {
            try {
                Files.move(episodePath, renamedEpisodeOutputPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            } catch (IOException e) {
                log.error("[EpisodeRenameTaskExecutor]move episode file failed.", e);
                taskItem.setStatus(EpisodeRenameTaskItemStatusEnum.FAILED.getStatus());
                taskItem.setErrorMessage(STR."rename episode file failed.\n\{e.getMessage()}");
                taskItemRepository.save(taskItem);
            }
            return;
        }

        // 如果不删除源文件，那么就使用copy
        try {
            Files.copy(episodePath, renamedEpisodeOutputPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            log.error("[EpisodeRenameTaskExecutor]copy episode file failed.", e);
            taskItem.setStatus(EpisodeRenameTaskItemStatusEnum.FAILED.getStatus());
            taskItem.setErrorMessage(STR."rename episode file failed.\n\{e.getMessage()}");
            taskItemRepository.save(taskItem);
            return;
        }

        taskItem.setStatus(EpisodeRenameTaskItemStatusEnum.SUCCESS.getStatus());
        taskItemRepository.save(taskItem);
    }
}

















