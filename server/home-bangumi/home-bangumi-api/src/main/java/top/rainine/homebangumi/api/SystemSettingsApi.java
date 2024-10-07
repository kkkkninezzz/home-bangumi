package top.rainine.homebangumi.api;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;

/**
 * @author rainine
 * @description
 * @date 2024/5/9 18:10:26
 */
@RequestMapping("/api/v1/system-settings")
public interface SystemSettingsApi {

    /**
     * 获取网络代理配置
     * */
    @GetMapping(value = "/network-proxy", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<NetworkProxySettingsResp> getNetworkProxySettings();

    /**
     * 更新网络代理配置
     * */
    @PutMapping(value = "/network-proxy", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<NetworkProxySettingsResp> updateNetworkProxySettings(@Valid @RequestBody UpdateNetworkProxySettingsReq req);

    /**
     * 获取qbittorrent下载器配置
     * */
    @GetMapping(value = "/downloader/qbittorrent", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<QbittorrentDownloaderSettingsResp> getQbittorrentDownloaderSettings();

    /**
     * 更新qbittorrent下载器配置
     * */
    @PutMapping(value = "/downloader/qbittorrent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<QbittorrentDownloaderSettingsResp> updateQbittorrentDownloaderSettings(@Valid @RequestBody UpdateQbittorrentDownloaderSettingsReq req);

    /**
     * 获取qbittorrent下载器配置
     * */
    @GetMapping(value = "/episode/filter-rules", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<EpisodeFilterRulesSettingsResp> getEpisodeFilterRulesSettings();

    /**
     * 更新qbittorrent下载器配置
     * */
    @PutMapping(value = "/episode/filter-rules", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<EpisodeFilterRulesSettingsResp> updateEpisodeFilterRulesSettings(@Valid @RequestBody EpisodeFilterRulesSettingsReq req);

    /**
     * 获取定时任务代理配置
     * */
    @GetMapping(value = "/scheduled-task", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<ScheduledTaskSettingsResp> getScheduledTaskSettings();

    /**
     * 更新定时任务代理配置
     * */
    @PutMapping(value = "/scheduled-task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<ScheduledTaskSettingsResp> updateScheduledTaskSettings(@Valid @RequestBody UpdateScheduledTaskSettingsReq req);


    /**
     * 获取wecomchan的消息推送配置
     * */
    @GetMapping(value = "/message-pusher/wecomchan", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<WecomchanSettingsResp> getWecomchanSettings();

    /**
     * 更新wecomchan的消息推送配置
     * */
    @PutMapping(value = "/message-pusher/wecomchan", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<WecomchanSettingsResp> updateWecomchanSettings(@Valid @RequestBody UpdateWecomchanSettingsReq req);


    /**
     * 获取重命名任务的配置
     * */
    @GetMapping(value = "/rename-task/episode", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<EpisodeRenameTaskSettingsResp> getEpisodeRenameTaskSettings();

    /**
     * 更新重命名任务的配置
     * */
    @PutMapping(value = "/rename-task/episode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<EpisodeRenameTaskSettingsResp> updateEpisodeRenameTaskSettings(@Valid @RequestBody UpdateEpisodeRenameTaskSettingsReq req);
}
