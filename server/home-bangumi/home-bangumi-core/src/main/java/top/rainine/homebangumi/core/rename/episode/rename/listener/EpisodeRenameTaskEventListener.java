package top.rainine.homebangumi.core.rename.episode.rename.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.rename.episode.rename.impl.EpisodeRenameTaskAlterMessageComponent;
import top.rainine.homebangumi.core.rename.episode.rename.EpisodeRenameTaskManager;
import top.rainine.homebangumi.core.event.data.EpisodeRenameTaskExecuteEndEvent;
import top.rainine.homebangumi.core.event.data.EpisodeRenameTaskExecuteFailedEvent;
import top.rainine.homebangumi.core.event.data.EpisodeRenameTaskExecutionTimeTooLongEvent;

/**
 * @author rainine
 * @description 事件监听器
 * @date 2024/8/1 16:59:28
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EpisodeRenameTaskEventListener {

    private final EpisodeRenameTaskManager taskManager;

    private final EpisodeRenameTaskAlterMessageComponent alterMessageComponent;

    @EventListener(EpisodeRenameTaskExecuteEndEvent.class)
    @Async
    public void onEpisodeRenameTaskExecuteEndEvent(EpisodeRenameTaskExecuteEndEvent event) {
        taskManager.onTaskExecuteEnd(event.getTaskId());
    }

    @EventListener(EpisodeRenameTaskExecutionTimeTooLongEvent.class)
    @Async
    public void addTaskExecutionTimeTooLongMessage(EpisodeRenameTaskExecutionTimeTooLongEvent event) {
        alterMessageComponent.addTaskExecutionTimeTooLongMessage(event.getTaskId(), event.getStartTime());
    }

    @EventListener(EpisodeRenameTaskExecuteEndEvent.class)
    @Async
    public void addTaskExecuteEndMessage(EpisodeRenameTaskExecuteEndEvent event) {
        alterMessageComponent.addTaskExecuteEndMessage(event.getTaskId());
    }

    @EventListener(EpisodeRenameTaskExecuteFailedEvent.class)
    @Async
    public void onEpisodeRenameTaskExecuteFailedEvent(EpisodeRenameTaskExecuteFailedEvent event) {
        taskManager.onTaskExecuteFailed(event.getTaskId());
    }
}
