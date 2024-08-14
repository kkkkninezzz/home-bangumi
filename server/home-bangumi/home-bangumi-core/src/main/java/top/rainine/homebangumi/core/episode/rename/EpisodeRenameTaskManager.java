package top.rainine.homebangumi.core.episode.rename;

/**
 * @author rainine
 * @description 任务管理器
 * @date 2024/7/31 18:45:45
 */
public interface EpisodeRenameTaskManager {

    /**
     * 提交任务
     * */
    void submitTask(Long id);

    /**
     * 当前任务执行结束
     * */
    void onTaskExecuteEnd(Long id);

    /**
     * 检查所有未完成的任务
     * */
    void checkAllNotFinishedTasks();
}
