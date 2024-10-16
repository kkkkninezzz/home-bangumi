import { type ApiResult, hbHttp } from "./base";

export type NetworkProxySettingsResp = {
  data: {
    enable: boolean;
    networkProxyType?: number;
    httpProxySettings?: HttpProxySettingsDto;
    socks5ProxySettings?: Socks5ProxySettingsDto;
  };
} & ApiResult;

export type HttpProxySettingsDto = {
  host: string;
  port: number;
};

export type Socks5ProxySettingsDto = {
  host: string;
  port: number;
  username?: string;
  password?: string;
};

export type UpdateNetworkProxySettingsReq = {
  enable: boolean;
  networkProxyType?: number;
  httpProxySettings?: HttpProxySettingsDto;
  socks5ProxySettings?: Socks5ProxySettingsDto;
};

export type QbittorrentDownloaderSettingsResp = {
  data: {
    baseUrl?: string;
    username?: string;
    password?: string;
    downloadDir?: string;
  };
} & ApiResult;

export type UpdateQbittorrentDownloaderSettingsReq = {
  baseUrl: string;
  username: string;
  password: string;
  downloadDir: string;
};

export type EpisodeFilterRulesSettingsResp = {
  data: {
    rules: Array<string>;
  };
} & ApiResult;

export type EpisodeFilterRulesSettingsReq = {
  rules: Array<string>;
};

export type ScheduledTaskSettingsResp = {
  data: {
    /**
     * 检查番剧下载状态的定时任务周期，单位分钟
     * */
    checkEpisodeDownloadStatusDuration: number;

    /**
     * 推送已经解析好的番剧到下载器的定时任务周期，单位分钟
     * */
    pushParsedEpisodesToDownloaderDuration: number;

    /**
     * 重命名剧集的定时任务周期，单位分钟
     * */
    renameEpisodesDuration: number;

    /**
     * 更新rss bangumi的定时任务周期，单位分钟
     * */
    updateRssSubscriptionDuration: number;

    /**
     * 检查未完成的重命名任务 的定时任务周期，单位分钟
     * */
    checkNotFinishedRenameTaskDuration: number;
  };
} & ApiResult;

export type UpdateScheduledTaskSettingsReq = {
  /**
   * 检查番剧下载状态的定时任务周期，单位分钟
   * */
  checkEpisodeDownloadStatusDuration: number;

  /**
   * 推送已经解析好的番剧到下载器的定时任务周期，单位分钟
   * */
  pushParsedEpisodesToDownloaderDuration: number;

  /**
   * 重命名剧集的定时任务周期，单位分钟
   * */
  renameEpisodesDuration: number;

  /**
   * 定时更新rss bangumi的定时任务周期，单位分钟
   * */
  updateRssSubscriptionDuration: number;

  /**
   * 检查未完成的重命名任务 的定时任务周期，单位分钟
   * */
  checkNotFinishedRenameTaskDuration: number;
};

/** 更新wecomchan的设置 */
export type UpdateWecomchanSettingsReq = {
  /**
   * 是否可用
   */
  enable: boolean;

  /**
   * wecomchan的地址
   */
  url?: string;

  /**
   * sendKey
   */
  sendKey?: string;
};

export type WecomchanSettingsResp = {
  data: {
    /**
     * 是否可用
     */
    enable: boolean;

    /**
     * wecomchan的地址
     */
    url: string;

    /**
     * sendKey
     */
    sendKey: string;
  };
} & ApiResult;

/** 更新剧集重命名任务的配置 */
export type UpdateEpisodeRenameTaskSettingsReq = {
  /**
   * 源目录路径
   */
  sourceDirPath: string;

  /**
   * 输出目录路径
   */
  outDirPath: string;
};

export type EpisodeRenameTaskSettingsResp = {
  data: {
    /**
     * 源目录路径
     */
    sourceDirPath: string;

    /**
     * 输出目录路径
     */
    outDirPath: string;
  };
} & ApiResult;

/** 获取网络代理配置 */
export const getNetworkProxySettings = () => {
  return hbHttp.request<NetworkProxySettingsResp>(
    "get",
    `/api/v1/system-settings/network-proxy`,
    {}
  );
};

/** 更新网络代理配置 */
export const updateNetworkProxySettings = (
  data: UpdateNetworkProxySettingsReq
) => {
  return hbHttp.request<NetworkProxySettingsResp>(
    "put",
    `/api/v1/system-settings/network-proxy`,
    { data }
  );
};

/** 获取qb配置 */
export const getQbittorrentDownloaderSettings = () => {
  return hbHttp.request<QbittorrentDownloaderSettingsResp>(
    "get",
    `/api/v1/system-settings/downloader/qbittorrent`,
    {}
  );
};

/** 更新网络代理配置 */
export const updateQbittorrentDownloaderSettings = (
  data: UpdateQbittorrentDownloaderSettingsReq
) => {
  return hbHttp.request<QbittorrentDownloaderSettingsResp>(
    "put",
    `/api/v1/system-settings/downloader/qbittorrent`,
    { data }
  );
};

/** 获取过滤规则配置 */
export const getEpisodeFilterRulesSettings = () => {
  return hbHttp.request<EpisodeFilterRulesSettingsResp>(
    "get",
    `/api/v1/system-settings/episode/filter-rules`,
    {}
  );
};

/** 更新过滤规则配置 */
export const updateEpisodeFilterRulesSettings = (
  data: EpisodeFilterRulesSettingsReq
) => {
  return hbHttp.request<EpisodeFilterRulesSettingsResp>(
    "put",
    `/api/v1/system-settings/episode/filter-rules`,
    { data }
  );
};

/** 获取定时任务配置 */
export const getScheduledTaskSettings = () => {
  return hbHttp.request<ScheduledTaskSettingsResp>(
    "get",
    `/api/v1/system-settings/scheduled-task`,
    {}
  );
};

/** 更新定时任务配置 */
export const updateScheduledTaskSettings = (
  data: UpdateScheduledTaskSettingsReq
) => {
  return hbHttp.request<ScheduledTaskSettingsResp>(
    "put",
    `/api/v1/system-settings/scheduled-task`,
    { data }
  );
};

/** 获取wecomchan配置 */
export const getWecomchanSettings = () => {
  return hbHttp.request<WecomchanSettingsResp>(
    "get",
    `/api/v1/system-settings/message-pusher/wecomchan`,
    {}
  );
};

/** 更新wecomchan配置 */
export const updateWecomchanSettings = (data: UpdateWecomchanSettingsReq) => {
  return hbHttp.request<WecomchanSettingsResp>(
    "put",
    `/api/v1/system-settings/message-pusher/wecomchan`,
    { data }
  );
};

/** 获取剧集重命名任务配置 */
export const getEpisodeRenameTaskSettings = () => {
  return hbHttp.request<EpisodeRenameTaskSettingsResp>(
    "get",
    `/api/v1/system-settings/rename-task/episode`,
    {}
  );
};

/** 更新剧集重命名任务配置 */
export const updateEpisodeRenameTaskSettings = (
  data: UpdateEpisodeRenameTaskSettingsReq
) => {
  return hbHttp.request<EpisodeRenameTaskSettingsResp>(
    "put",
    `/api/v1/system-settings/rename-task/episode`,
    { data }
  );
};
