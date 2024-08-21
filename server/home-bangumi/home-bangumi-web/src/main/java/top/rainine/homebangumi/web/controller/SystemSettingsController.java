package top.rainine.homebangumi.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import top.rainine.homebangumi.api.SystemSettingsApi;
import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;
import top.rainine.homebangumi.core.settings.SystemSettingsApiFacadeService;

/**
 * @authoer rainine
 * @date 2024/5/10 00:02
 * @desc 系统配置的控制器
 */
@RestController
@RequiredArgsConstructor
public class SystemSettingsController implements SystemSettingsApi {

    private final SystemSettingsApiFacadeService apiFacadeService;

    @Override
    public Result<NetworkProxySettingsResp> getNetworkProxySettings() {
        return Result.createSuccess(apiFacadeService.getNetworkProxySettings());
    }

    @Override
    public Result<NetworkProxySettingsResp> updateNetworkProxySettings(UpdateNetworkProxySettingsReq req) {
        return Result.createSuccess(apiFacadeService.updateNetworkProxySettings(req));
    }

    @Override
    public Result<QbittorrentDownloaderSettingsResp> getQbittorrentDownloaderSettings() {
        return Result.createSuccess(apiFacadeService.getQbittorrentDownloaderSettings());
    }

    @Override
    public Result<QbittorrentDownloaderSettingsResp> updateQbittorrentDownloaderSettings(UpdateQbittorrentDownloaderSettingsReq req) {
        return Result.createSuccess(apiFacadeService.updateQbittorrentDownloaderSettings(req));
    }

    @Override
    public Result<EpisodeFilterRulesSettingsResp> getEpisodeFilterRulesSettings() {
        return Result.createSuccess(apiFacadeService.getEpisodeFilterRulesSettings());
    }

    @Override
    public Result<EpisodeFilterRulesSettingsResp> updateEpisodeFilterRulesSettings(EpisodeFilterRulesSettingsReq req) {
        return Result.createSuccess(apiFacadeService.updateEpisodeFilterRulesSettings(req));
    }

    @Override
    public Result<ScheduledTaskSettingsResp> getScheduledTaskSettings() {
        return Result.createSuccess(apiFacadeService.getScheduledTaskSettings());
    }

    @Override
    public Result<ScheduledTaskSettingsResp> updateScheduledTaskSettings(UpdateScheduledTaskSettingsReq req) {
        return Result.createSuccess(apiFacadeService.updateScheduledTaskSettings(req));
    }

    @Override
    public Result<WecomchanSettingsResp> getWecomchanSettings() {
        return Result.createSuccess(apiFacadeService.getWecomchanSettings());
    }

    @Override
    public Result<WecomchanSettingsResp> updateWecomchanSettings(UpdateWecomchanSettingsReq req) {
        return Result.createSuccess(apiFacadeService.updateWecomchanSettings(req));
    }
}
