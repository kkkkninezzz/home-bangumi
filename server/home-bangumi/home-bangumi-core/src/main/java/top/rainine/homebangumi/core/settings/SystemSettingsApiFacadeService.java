package top.rainine.homebangumi.core.settings;

import top.rainine.homebangumi.api.req.EpisodeFilterRulesSettingsReq;
import top.rainine.homebangumi.api.req.UpdateNetworkProxySettingsReq;
import top.rainine.homebangumi.api.req.UpdateQbittorrentDownloaderSettingsReq;
import top.rainine.homebangumi.api.req.UpdateScheduledTaskSettingsReq;
import top.rainine.homebangumi.api.resp.EpisodeFilterRulesSettingsResp;
import top.rainine.homebangumi.api.resp.NetworkProxySettingsResp;
import top.rainine.homebangumi.api.resp.QbittorrentDownloaderSettingsResp;
import top.rainine.homebangumi.api.resp.ScheduledTaskSettingsResp;

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
}
