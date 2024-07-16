package top.rainine.homebangumi.core.settings.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.api.req.EpisodeFilterRulesSettingsReq;
import top.rainine.homebangumi.api.req.UpdateNetworkProxySettingsReq;
import top.rainine.homebangumi.api.req.UpdateQbittorrentDownloaderSettingsReq;
import top.rainine.homebangumi.api.req.UpdateScheduledTaskSettingsReq;
import top.rainine.homebangumi.api.resp.EpisodeFilterRulesSettingsResp;
import top.rainine.homebangumi.api.resp.NetworkProxySettingsResp;
import top.rainine.homebangumi.api.resp.QbittorrentDownloaderSettingsResp;
import top.rainine.homebangumi.api.resp.ScheduledTaskSettingsResp;
import top.rainine.homebangumi.core.settings.DownloaderSettingsService;
import top.rainine.homebangumi.core.settings.EpisodeFilterRulesSettingsService;
import top.rainine.homebangumi.core.settings.NetProxySettingsService;
import top.rainine.homebangumi.core.settings.SystemSettingsApiFacadeService;
import top.rainine.homebangumi.core.settings.data.EpisodeFilterRulesSettings;
import top.rainine.homebangumi.core.settings.data.NetProxySettings;
import top.rainine.homebangumi.core.settings.data.QbittorrentDownloaderSettings;
import top.rainine.homebangumi.core.settings.data.ScheduledTaskSettings;
import top.rainine.homebangumi.core.settings.data.convertor.SystemSettingsConvertor;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.util.Objects;

/**
 * @authoer rainine
 * @date 2024/5/10 00:04
 * @desc api门面的实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SystemSettingsApiFacadeServiceImpl implements SystemSettingsApiFacadeService {

    private final NetProxySettingsService netProxySettingsService;

    private final DownloaderSettingsService downloaderSettingsService;

    private final EpisodeFilterRulesSettingsService  episodeFilterRulesSettingsService;

    private final ScheduledTaskSettingsServiceImpl scheduledTaskSettingsService;

    private final SystemSettingsConvertor systemSettingsConvertor;

    @Override
    public NetworkProxySettingsResp getNetworkProxySettings() {
        NetProxySettings proxySettings = netProxySettingsService.getProxySettings();

        return systemSettingsConvertor.toNetworkProxySettingsResp(proxySettings);
    }

    @Override
    public NetworkProxySettingsResp updateNetworkProxySettings(UpdateNetworkProxySettingsReq req) {
        if (req.getEnable() && Objects.isNull(req.getHttpProxySettings()) && Objects.isNull(req.getSocks5ProxySettings())) {
            throw new HbBizException(HbCodeEnum.NETWORK_PROXY_SETTINGS_INVALID);
        }

        NetProxySettings proxySettings = systemSettingsConvertor.toNetProxySettings(req);

        netProxySettingsService.updateProxySettings(proxySettings);
        return getNetworkProxySettings();
    }

    @Override
    public QbittorrentDownloaderSettingsResp getQbittorrentDownloaderSettings() {
        QbittorrentDownloaderSettings settings = downloaderSettingsService.getQbittorrentDownloaderSettings();
        return systemSettingsConvertor.toQbittorrentDownloaderSettingsResp(settings);
    }

    @Override
    public QbittorrentDownloaderSettingsResp updateQbittorrentDownloaderSettings(UpdateQbittorrentDownloaderSettingsReq req) {
        String baseUrl = req.getBaseUrl();
        // 末尾不需要有 /
        if (baseUrl.endsWith("/")) {
            req.setBaseUrl(baseUrl.substring(0, baseUrl.length() -1));
        }

        QbittorrentDownloaderSettings settings = systemSettingsConvertor.toQbittorrentDownloaderSettings(req);
        downloaderSettingsService.updateQbittorrentDownloaderSettings(settings);
        return getQbittorrentDownloaderSettings();
    }

    @Override
    public EpisodeFilterRulesSettingsResp getEpisodeFilterRulesSettings() {
        EpisodeFilterRulesSettings settings = episodeFilterRulesSettingsService.getSettings();
        return systemSettingsConvertor.toEpisodeFilterRulesSettingsResp(settings);
    }

    @Override
    public EpisodeFilterRulesSettingsResp updateEpisodeFilterRulesSettings(EpisodeFilterRulesSettingsReq req) {
        EpisodeFilterRulesSettings settings = systemSettingsConvertor.toEpisodeFilterRulesSettings(req);
        episodeFilterRulesSettingsService.updateSettings(settings);
        return getEpisodeFilterRulesSettings();
    }

    @Override
    public ScheduledTaskSettingsResp getScheduledTaskSettings() {
        ScheduledTaskSettings settings = scheduledTaskSettingsService.getSettings();
        return systemSettingsConvertor.toScheduledTaskSettingsResp(settings);
    }

    @Override
    public ScheduledTaskSettingsResp updateScheduledTaskSettings(UpdateScheduledTaskSettingsReq req) {
        ScheduledTaskSettings settings = systemSettingsConvertor.toScheduledTaskSettings(req);
        scheduledTaskSettingsService.updateSettings(settings);
        return getScheduledTaskSettings();
    }
}






























