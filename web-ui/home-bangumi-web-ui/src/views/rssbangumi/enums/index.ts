import type { EpPropMergeType } from "element-plus/lib/utils/vue/index.js";

export enum RssBangumiStatusEnum {
  NONE = 0,
  INACTIVE = 1,
  ACTIVE = 2,
  ARCHIVED = 3
}

// rss bangumi的状态下拉列表
export const RssBangumiStatusOptions = [
  {
    label: "已暂停",
    value: RssBangumiStatusEnum.INACTIVE,
    color: "blue"
  },
  {
    label: "订阅中",
    value: RssBangumiStatusEnum.ACTIVE,
    color: "blue"
  },
  {
    label: "已归档",
    value: RssBangumiStatusEnum.ARCHIVED,
    color: "blue"
  }
];

export const RssBangumiStatusTagMap = new Map<number, CardTag>();
RssBangumiStatusTagMap.set(RssBangumiStatusEnum.INACTIVE, {
  color: "#e6a23c",
  content: "已暂停"
});
RssBangumiStatusTagMap.set(RssBangumiStatusEnum.ACTIVE, {
  color: "#00a870",
  content: "订阅中"
});
RssBangumiStatusTagMap.set(RssBangumiStatusEnum.ARCHIVED, {
  color: "#909399",
  content: "已归档"
});

export const CollectTag = {
  color: "#004080",
  content: "收集型"
};

export const ArchivedCollectTag = {
  color: "#909399",
  content: "收集型"
};

export enum RssCategoryEnum {
  Mikan = 1
}

// 卡片tag
export interface CardTag {
  color: string; // 颜色
  content: string; // 标签内容
}

// 星期几 tag定义
export const BroadcastDayOfWeekTagMap = new Map<number, CardTag>();
BroadcastDayOfWeekTagMap.set(1, { color: "#9b79a7", content: "Mon." });
BroadcastDayOfWeekTagMap.set(2, { color: "#6fbe70", content: "Tues." });
BroadcastDayOfWeekTagMap.set(3, { color: "#ff894c", content: "Wed." });
BroadcastDayOfWeekTagMap.set(4, { color: "#7e8fb9", content: "Thur." });
BroadcastDayOfWeekTagMap.set(5, { color: "#fad153", content: "Fri." });
BroadcastDayOfWeekTagMap.set(6, { color: "#55ace0", content: "Sat." });
BroadcastDayOfWeekTagMap.set(7, { color: "#e36a7b", content: "Sun." });

// 星期几与描述的映射
export const DayOfWeekToDescMap = new Map<number, string>();
DayOfWeekToDescMap.set(1, "星期一");
DayOfWeekToDescMap.set(2, "星期二");
DayOfWeekToDescMap.set(3, "星期三");
DayOfWeekToDescMap.set(4, "星期四");
DayOfWeekToDescMap.set(5, "星期五");
DayOfWeekToDescMap.set(6, "星期六");
DayOfWeekToDescMap.set(7, "星期天");

// 类别tag map
export const RssCategoryTagMap = new Map<number, CardTag>();
RssCategoryTagMap.set(RssCategoryEnum.Mikan, {
  color: "#faaa3b",
  content: "Mikan"
});

// rss bangumi的类别
export const RssBangumRssCategoryOptions = [
  {
    label: "Mikan",
    value: RssCategoryEnum.Mikan,
    color: "blue"
  }
];

// rss bangumi的处理方式枚举
export enum RssBangumiHandleMethodEnum {
  COLLECT = 1,
  SUBSCRIBE = 2
}

// rss bangumi的处理方式options
export const RssBangumiHandleMethodOptions = [
  {
    label: "收集",
    value: RssBangumiHandleMethodEnum.COLLECT,
    color: "blue"
  },
  {
    label: "订阅",
    value: RssBangumiHandleMethodEnum.SUBSCRIBE,
    color: "blue"
  }
];

// 状态枚举
export enum RssBangumiEpisodeStatusEnum {
  PARSED = 1,
  TITLE_PARSE_FAILED = 2,
  TORRENT_DOWNLOAD_FAILED = 22,
  TORRENT_PARSE_FAILED = 24,
  TORRENT_STORED_FAILED = 25,
  EPISODE_DOWNLOADING = 31,
  EPISODE_DOWNLOAD_FAILED = 32,
  EPISODE_DOWNLOAD_FINISHED = 33,
  RENAMING = 41,
  RENAME_FAILED = 42,
  RENAME_FINISHED = 43,
  FINISHED = 51,
  ARCHIVED = 95,
  IGNORED = 96,
  SKIPPED = 97,
  FILTERED_OUT = 98,
  ERROR = 99
}

// 状态tag
export interface RssBangumiEpisodeStatusTag {
  tagType: EpPropMergeType<
    StringConstructor,
    "success" | "warning" | "info" | "primary" | "danger",
    unknown
  >;
  tagContent: string;
}
// rss bangumi episode 状态的tag映射
export const RssBangumiEpisodeStatusTagMap = new Map<
  number,
  RssBangumiEpisodeStatusTag
>();
RssBangumiEpisodeStatusTagMap.set(RssBangumiEpisodeStatusEnum.PARSED, {
  tagType: "primary",
  tagContent: "已解析"
});
RssBangumiEpisodeStatusTagMap.set(
  RssBangumiEpisodeStatusEnum.TITLE_PARSE_FAILED,
  { tagType: "danger", tagContent: "标题解析失败" }
);
RssBangumiEpisodeStatusTagMap.set(
  RssBangumiEpisodeStatusEnum.TORRENT_DOWNLOAD_FAILED,
  { tagType: "danger", tagContent: "种子下载失败" }
);
RssBangumiEpisodeStatusTagMap.set(
  RssBangumiEpisodeStatusEnum.TORRENT_PARSE_FAILED,
  { tagType: "danger", tagContent: "种子解析失败" }
);
RssBangumiEpisodeStatusTagMap.set(
  RssBangumiEpisodeStatusEnum.TORRENT_STORED_FAILED,
  { tagType: "danger", tagContent: "种子保存失败" }
);
RssBangumiEpisodeStatusTagMap.set(
  RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOADING,
  { tagType: "primary", tagContent: "剧集下载中" }
);
RssBangumiEpisodeStatusTagMap.set(
  RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOAD_FAILED,
  { tagType: "danger", tagContent: "剧集下载失败" }
);
RssBangumiEpisodeStatusTagMap.set(
  RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOAD_FINISHED,
  { tagType: "primary", tagContent: "剧集下载完成" }
);
RssBangumiEpisodeStatusTagMap.set(RssBangumiEpisodeStatusEnum.RENAMING, {
  tagType: "primary",
  tagContent: "重命名中"
});
RssBangumiEpisodeStatusTagMap.set(RssBangumiEpisodeStatusEnum.RENAME_FAILED, {
  tagType: "danger",
  tagContent: "重命名失败"
});
RssBangumiEpisodeStatusTagMap.set(RssBangumiEpisodeStatusEnum.RENAME_FINISHED, {
  tagType: "primary",
  tagContent: "重命名完成"
});
RssBangumiEpisodeStatusTagMap.set(RssBangumiEpisodeStatusEnum.FINISHED, {
  tagType: "success",
  tagContent: "成功"
});
RssBangumiEpisodeStatusTagMap.set(RssBangumiEpisodeStatusEnum.ARCHIVED, {
  tagType: "info",
  tagContent: "归档"
});
RssBangumiEpisodeStatusTagMap.set(RssBangumiEpisodeStatusEnum.IGNORED, {
  tagType: "info",
  tagContent: "忽略"
});
RssBangumiEpisodeStatusTagMap.set(RssBangumiEpisodeStatusEnum.SKIPPED, {
  tagType: "info",
  tagContent: "跳过"
});
RssBangumiEpisodeStatusTagMap.set(RssBangumiEpisodeStatusEnum.FILTERED_OUT, {
  tagType: "info",
  tagContent: "过滤"
});
RssBangumiEpisodeStatusTagMap.set(RssBangumiEpisodeStatusEnum.ERROR, {
  tagType: "danger",
  tagContent: "未知错误"
});

export enum DownloaderCategoryEnum {
  /** qbittorrent */
  QBITTORRENT = 1,

  /** 迅雷下载器 */
  XUNLEI = 2
}

// rss bangumi的处理方式options
export const DownloaderCategoryOptions = [
  {
    label: "qbittorrent",
    value: DownloaderCategoryEnum.QBITTORRENT,
    color: "blue"
  }
  // {
  //   label: "迅雷",
  //   value: DownloaderCategoryEnum.XUNLEI,
  //   color: "blue"
  // }
];

// 下载器tag
export interface DownloaderCategoryTag {
  tagType: string;
  tagContent: string;
}
// 下载器tag映射
export const DownloaderCategoryTagMap = new Map<
  number,
  DownloaderCategoryTag
>();
DownloaderCategoryTagMap.set(DownloaderCategoryEnum.QBITTORRENT, {
  tagType: "primary",
  tagContent: "qbittorrent"
});
DownloaderCategoryTagMap.set(DownloaderCategoryEnum.XUNLEI, {
  tagType: "primary",
  tagContent: "迅雷"
});

/**
 * 剧集重命名方式
 */
export enum EpisodeTitleRenameMethodEnum {
  /** 不进行重命名，保留种子的文件名 */
  NONE = 0,

  /** 基于种子文件解析出来的标题进行重命名 */
  TORRENT_PARSED_TITLE = 1,

  /** 基于官方标题进行重命名 */
  OFFICIAL_TITLE = 2,

  /** 基于自定义标题进行重命名 */
  CUSTOMIZED_TITLE = 3
}

// 剧集重命名方式options
export const EpisodeTitleRenameMethodOptions = [
  {
    label: "NONE",
    value: EpisodeTitleRenameMethodEnum.NONE,
    color: "blue"
  },
  {
    label: "Torrent parsed title",
    value: EpisodeTitleRenameMethodEnum.TORRENT_PARSED_TITLE,
    color: "blue"
  },
  {
    label: "Official title",
    value: EpisodeTitleRenameMethodEnum.OFFICIAL_TITLE,
    color: "blue"
  },
  {
    label: "Customized title",
    value: EpisodeTitleRenameMethodEnum.CUSTOMIZED_TITLE,
    color: "blue"
  }
];
