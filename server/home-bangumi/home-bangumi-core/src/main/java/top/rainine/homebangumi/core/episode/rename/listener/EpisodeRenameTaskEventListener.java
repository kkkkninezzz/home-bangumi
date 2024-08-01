package top.rainine.homebangumi.core.episode.rename.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.episode.rename.EpisodeRenameTaskManager;
import top.rainine.homebangumi.core.event.data.EpisodeRenameTaskExecuteEndEvent;

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

    @EventListener(EpisodeRenameTaskExecuteEndEvent.class)
    @Async
    public void onEpisodeRenameTaskExecuteEndEvent(EpisodeRenameTaskExecuteEndEvent event) {
        taskManager.onTaskExecuteEnd(event.getTaskId());
    }
}
