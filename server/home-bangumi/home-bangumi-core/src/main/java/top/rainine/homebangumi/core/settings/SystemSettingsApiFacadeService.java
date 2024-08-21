package top.rainine.homebangumi.core.settings;

import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;

/**
 * @authoer rainine
 * @date 2024/5/9 23:58
 * @desc 针对于api的门面类
 */
public interface SystemSettingsApiFacadeService {

    NetworkProxySettingsResp getNetworkProxySettings();

    NetworkProxySettingsResp updateNetworkProxySettings(UpdateNetworkProxySettingsReq req);

    QbittorrentDownloaderSettingsResp getQbittorrentDownloaderSettings();

    QbittorrentDownloaderSettingsResp updateQbittorrentDownloaderSettings(UpdateQbittorrentDownloaderSettingsReq req);

    EpisodeFilterRulesSettingsResp getEpisodeFilterRulesSettings();

    EpisodeFilterRulesSettingsResp updateEpisodeFilterRulesSettings(EpisodeFilterRulesSettingsReq req);

    ScheduledTaskSettingsResp getScheduledTaskSettings();

    ScheduledTaskSettingsResp updateScheduledTaskSettings(UpdateScheduledTaskSettingsReq req);

    WecomchanSettingsResp getWecomchanSettings();

    WecomchanSettingsResp updateWecomchanSettings(UpdateWecomchanSettingsReq req);
}
