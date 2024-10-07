package top.rainine.homebangumi.core.settings;

import top.rainine.homebangumi.core.settings.data.EpisodeRenameTaskSettings;

/**
 * @authoer rainine
 * @date 2024/10/7 13:13
 * @desc
 */
public interface RenameTaskSettingsService {

    /**
     * 获取剧集重命名任务配置
     * */
    EpisodeRenameTaskSettings getEpisodeRenameTaskSettings();

    /**
     * 更新剧集重命名任务配置
     * */
    void updateEpisodeRenameTaskSettings(EpisodeRenameTaskSettings settings);
}
