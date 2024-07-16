package top.rainine.homebangumi.core.settings;

import top.rainine.homebangumi.core.settings.data.EpisodeFilterRulesSettings;

/**
 * @author rainine
 * @description 默认的剧集过滤规则设置
 * @date 2024/5/14 10:33:12
 */
public interface EpisodeFilterRulesSettingsService {

    /**
     * 获取设置
     * */
    EpisodeFilterRulesSettings getSettings();

    /**
     * 更新设置
     * */
    void updateSettings(EpisodeFilterRulesSettings settings);
}
