import { type ApiResult, type PagedReq, type PagedResp, hbHttp } from "./base";

/**
 * rss bangumi预览请求
 */
export type RssBangumiPreviewReq = {
  /** rss的分类 */
  rssCategory: number;
  /** rss链接名 */
  rssName: string;
  /** rss链接地址 */
  rssLink: string;
  /** 过滤规则 */
  filterRules: Array<string>;
  /** 剧集的偏移量 */
  episodeOffset: number;
  /** 收集方式 */
  handleMethod: number;
  /** 下载器分类 */
  downloaderCategory: number;
  /** 剧集标题重命名方式 */
  episodeTitleRenameMethod: number;
  /** 自定义的重命名后标题格式 */
  customizeRenamedEpisodeTitleFormat: string;
};

/**
 * rss bangumi的预览结果
 */
export type RssBangumiPreviewResp = {
  data: {
    /** 如果生成预览信息成功，返回id */
    id: number;
  };
} & ApiResult;

/**
 * 番剧信息
 */
export type BangumiInfoDto = {
  title: string;
  renamedTitle: string;
  posterUrl: string;
  broadcastDayOfWeek: number;
  broadcastDate: number;
  season: number;
};

/**
 * rss bangumi的详情
 */
export type RssBangumiDetailResp = {
  data: {
    id: number;
    rssName?: string;
    rssCategory: number;
    rssLink: string;
    handleMethod: number;
    status: number;
    filterRules: Array<string>;
    /** 剧集的偏移量 */
    episodeOffset: number;
    bangumiInfo?: BangumiInfoDto;
    /** 下载器分类 */
    downloaderCategory: number;
    /** 剧集标题重命名方式 */
    episodeTitleRenameMethod: number;
    /** 自定义的重命名后标题格式 */
    customizeRenamedEpisodeTitleFormat: string;
  };
} & ApiResult;

/**
 * 剧集信息dto
 */
export type RssBangumiEpisodeDto = {
  id: number;
  episodeNo?: number;
  rawEpisodeTitle: string;
  episodeFileName?: string;
  renamedEpisodeFileName?: string;
  episodeStoredPath?: string;
  renamedEpisodeStoredPath?: string;
  torrentPubDate?: number;
  torrentLink: string;
  torrentStoredPath: string;
  torrentIdentity?: string;
  status: number;
  errorMessage?: string;
  /** 下载器分类 */
  downloaderCategory: number;
};

/**
 * rss bangumi的剧集信息
 */
export type RssBangumiEpisodesResp = {
  data: {
    episodes: Array<RssBangumiEpisodeDto>;
  };
} & ApiResult;

/**
 * 更新rss bangumi相关信息
 */
export type RssBangumiUpdateReq = {
  /** rss链接名 */
  rssName: string;
  /** 过滤规则 */
  filterRules: Array<string>;
  /** 剧集的偏移量 */
  episodeOffset: number;
  /** 下载器分类 */
  downloaderCategory: number;
  /** 剧集标题重命名方式 */
  episodeTitleRenameMethod: number;
  /** 自定义的重命名后标题格式 */
  customizeRenamedEpisodeTitleFormat: string;
  /** 番剧信息 */
  bangumiInfo: BangumiInfoDto;
};

/** 重新解析剧集信息的返回 */
export type ReparseBangumiEpisodesResp = {
  data: number; // 成功后返回rssBangumiId
} & ApiResult;

/** 增量解析剧集信息的返回 */
export type IncrementalParseBangumiEpisodesResp = {
  data: number; // 成功后返回rssBangumiId
} & ApiResult;

/** 查询分页列表的条件 */
export type LoadPagedRssbangumisReqConditionDto = {
  rssName?: string;
  bangumiTitle?: string;
  rssCategory?: number;
  handleMethod?: number;
  status?: number;
};

/** 查询分页列表请求 */
export type LoadPagedRssbangumisReq = {
  condition: LoadPagedRssbangumisReqConditionDto;
} & PagedReq;

export type PagedRssbBangumiItemDto = {
  id: number;
  rssName: string;
  rssCategory: number;
  handleMethod: number;
  status: number;
  bangumiInfo: BangumiInfoDto;
};

export type LoadPagedRssbangumisResp = {
  data: {
    list: Array<PagedRssbBangumiItemDto>;
  } & PagedResp;
} & ApiResult;

export type RenameEpisodeFileNameReq = {
  newFileName: string;
};

/** 手动解析剧集信息 */
export type ManualParseEpisodeReq = {
  episodeNo: number;
  renamedEpisodeFileName: string;
};

export type RssBangumiStatisticOnHomeResp = {
  data: {
    /**
     * 下载中的剧集数量
     * */
    downloadingEpisodesCount: number;
    /**
     * 各种原因失败的剧集数量
     * */
    failedEpisodesCount: number;
    /**
     * 一周内有更新的bangumi数量
     * */
    updatedBangumiCountInWeek: number;

    /**
     * 今天更新的bangumi数量
     * */
    updatedBangumiCountToday: number;

    /**
     * 当前季度订阅的数量
     * */
    subscriptionsCountInCurQuarter: number;

    /**
     * 前一个季度订阅的数量
     * */
    subscriptionsCountInPreQuarter: number;

    /**
     * 收集的番剧数量
     * */
    collectedBangumiCount: number;

    /**
     * 今日收集的番剧数量
     * */
    collectedBangumiCountToday: number;
  };
} & ApiResult;

export type UpdatedBangumiDto = {
  /**
   * rss bangumi id
   * */
  rssBangumiId: number;

  /**
   * 番剧标题
   * */
  bangumiTitle: string;

  /**
   * 番剧海报地址
   * */
  bangumiPosterUrl: string;

  /**
   * 更新的最新一集剧集编号
   * */
  latestEpisodeNo: number;

  /**
   * 更新的时间
   * */
  latestUpdatedTime: number;

  /**
   * 是否延迟更新
   */
  delay?: boolean;

  /**
   * 实际更新的星期
   * */
  actualBroadcastDayOfWeek?: number;

  /**
   * 预计播放的星期
   * */
  expectedBroadcastDayOfWeek: number;
};

export type UpdatedDate2BangumisDto = {
  /**
   * 更新的日期，0点时间
   * */
  date: number;

  /**
   * 更新的星期
   */
  dayOfWeek: number;
  bangumis: Array<UpdatedBangumiDto>;
};

export type UpdatedBangumisResp = {
  data: {
    bangumis: Array<UpdatedDate2BangumisDto>;
  };
} & ApiResult;

// 删除文件的标记
type DeleteFileFlagDto = {
  deleteFile: boolean;
};

/**
 * 预览rss bangumi
 * @param data 请求参数
 * @returns
 */
export const previewRssBangumi = (data: RssBangumiPreviewReq) => {
  return hbHttp.request<RssBangumiPreviewResp>(
    "post",
    "/api/v1/rss/bangumi/preview",
    { data }
  );
};

/**
 * 获取rss bangumi详情
 * @param id rss bangumi id
 * @returns
 */
export const getRssBangumiDetail = (id: number) => {
  return hbHttp.request<RssBangumiDetailResp>(
    "get",
    `/api/v1/rss/bangumi/detail/${id}`,
    {}
  );
};

/**
 * 获取rss bangumi解析的剧集信息
 * @param id rss bangumi id
 * @returns
 */
export const getRssBangumiEpisodes = (id: number) => {
  return hbHttp.request<RssBangumiEpisodesResp>(
    "get",
    `/api/v1/rss/bangumi/detail/${id}/episodes`,
    {}
  );
};

/** 更新bangumi信息 */
export const updateRssBangumi = (id: number, data: RssBangumiUpdateReq) => {
  return hbHttp.request<RssBangumiDetailResp>(
    "put",
    `/api/v1/rss/bangumi/${id}`,
    { data }
  );
};

/** 重新解析剧集信息 */
export const reparseBangumiEpisodes = (id: number) => {
  return hbHttp.request<ReparseBangumiEpisodesResp>(
    "post",
    `/api/v1/rss/bangumi/${id}/episodes/reparse`,
    {}
  );
};

/** 增量解析剧集信息 */
export const incrementalParseBangumiEpisodes = (id: number) => {
  return hbHttp.request<IncrementalParseBangumiEpisodesResp>(
    "post",
    `/api/v1/rss/bangumi/${id}/episodes/incrementalParse`,
    {}
  );
};

/** 获取分页的rss bangumi 数据 */
export const loadPagedRssBangumis = (data: LoadPagedRssbangumisReq) => {
  return hbHttp.request<LoadPagedRssbangumisResp>(
    "post",
    "/api/v1/rss/bangumi/paged/list",
    { data }
  );
};

/** 激活rss bangumi */
export const activeRssBangumi = (id: number) => {
  return hbHttp.request<ApiResult>(
    "put",
    `/api/v1/rss/bangumi/${id}/active`,
    {}
  );
};

/** 禁用rss bangumi */
export const inactiveRssBangumi = (id: number) => {
  return hbHttp.request<ApiResult>(
    "put",
    `/api/v1/rss/bangumi/${id}/inactive`,
    {}
  );
};

/** 删除rss bangumi */
export const deleteRssBangumi = (id: number, deleteFile: boolean) => {
  const data: DeleteFileFlagDto = {
    deleteFile: deleteFile
  };
  return hbHttp.request<ApiResult>("delete", `/api/v1/rss/bangumi/${id}`, {
    data
  });
};

/** 归档rss bangumi */
export const archiveRssBangumi = (id: number, deleteFile: boolean) => {
  const data: DeleteFileFlagDto = {
    deleteFile: deleteFile
  };
  return hbHttp.request<ApiResult>("put", `/api/v1/rss/bangumi/${id}/archive`, {
    data
  });
};

/** 推送至下载器 */
export const pushToDownloader = (id: number) => {
  return hbHttp.request<ApiResult>(
    "post",
    `/api/v1/rss/bangumi/${id}/download`,
    {}
  );
};

/** 重命名剧集标题 */
export const renameEpisodeFileName = (
  id: number,
  episodeId: number,
  data: RenameEpisodeFileNameReq
) => {
  return hbHttp.request<ApiResult>(
    "put",
    `/api/v1/rss/bangumi/${id}/episode/${episodeId}/renamed-file-name`,
    { data }
  );
};

/** 忽略剧集，如果当前剧集被推送至下载器中，那么会尝试从下载器中移除 */
export const ignoreEpisode = (
  id: number,
  episodeId: number,
  deleteFile: boolean
) => {
  const data: DeleteFileFlagDto = {
    deleteFile: deleteFile
  };
  return hbHttp.request<ApiResult>(
    "put",
    `/api/v1/rss/bangumi/${id}/episode/${episodeId}/ignore`,
    { data }
  );
};

/** 删除剧集，如果当前剧集被推送至下载器中，那么会尝试从下载器中移除 */
export const deleteEpisode = (
  id: number,
  episodeId: number,
  deleteFile: boolean
) => {
  const data: DeleteFileFlagDto = {
    deleteFile: deleteFile
  };
  return hbHttp.request<ApiResult>(
    "delete",
    `/api/v1/rss/bangumi/${id}/episode/${episodeId}`,
    { data }
  );
};

/** 手动解析剧集信息 */
export const manualParseEpisode = (
  id: number,
  episodeId: number,
  data: ManualParseEpisodeReq
) => {
  return hbHttp.request<ApiResult>(
    "put",
    `/api/v1/rss/bangumi/${id}/episode/${episodeId}/manual-parse`,
    { data }
  );
};

/** 在首页的统计信息 */
export const statisticOnHome = () => {
  return hbHttp.request<RssBangumiStatisticOnHomeResp>(
    "get",
    `/api/v1/rss/bangumi/statistic/home`,
    {}
  );
};

/** 番剧动态 */
export const getUpdatedBangumisForWeek = () => {
  return hbHttp.request<UpdatedBangumisResp>(
    "get",
    `/api/v1/rss/bangumi/updated-bangumis/week`,
    {}
  );
};
