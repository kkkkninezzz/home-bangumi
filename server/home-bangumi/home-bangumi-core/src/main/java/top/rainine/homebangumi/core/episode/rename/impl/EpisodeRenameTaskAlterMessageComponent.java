package top.rainine.homebangumi.core.episode.rename.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.common.utils.HbDateUtils;
import top.rainine.homebangumi.core.message.MessageService;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTask;
import top.rainine.homebangumi.def.enums.MessageCategoryEnum;
import top.rainine.homebangumi.def.enums.MessageTypeEnum;

import java.time.LocalDateTime;

/**
 * @author rainine
 * @description 重命名任务告警信息组件
 * @date 2024/8/15 11:22:35
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EpisodeRenameTaskAlterMessageComponent {

    private final MessageService messageService;

    private final EpisodeRenameTaskComponent taskComponent;

    /**
     * 添加任务执行时间过长的消息
     * */
    public void addTaskExecutionTimeTooLongMessage(Long taskId, LocalDateTime startTime) {
        HbEpisodeRenameTask renameTask = taskComponent.getTaskOrThrow(taskId);

        String messageContent = STR."\{renameTask.getTaskName()} 执行时间过长，请手动处理。开始时间: \{HbDateUtils.formatLocalDateTime(startTime)}";

        messageService.addMessage(MessageCategoryEnum.EPISODE_RENAME_TASK, MessageTypeEnum.WARNING,
                STR."重命名任务 \{renameTask.getTaskName()}", messageContent, taskId.toString());
    }

    /**
     * 添加执行结束的消息
     * */
    public void addTaskExecuteEndMessage(Long taskId) {
        HbEpisodeRenameTask renameTask = taskComponent.getTaskOrThrow(taskId);
        long successOfTaskItems = taskComponent.countSuccessOfTaskItems(taskId);
        long failedOfTaskItems = taskComponent.countFailedOfTaskItems(taskId);

        String messageContent = STR."\{renameTask.getTaskName()} 执行结束。 成功数量: \{successOfTaskItems}，失败数量: \{failedOfTaskItems}";

        messageService.addMessage(MessageCategoryEnum.EPISODE_RENAME_TASK, MessageTypeEnum.INFO,
                STR."重命名任务 \{renameTask.getTaskName()}", messageContent, taskId.toString());
    }
}
