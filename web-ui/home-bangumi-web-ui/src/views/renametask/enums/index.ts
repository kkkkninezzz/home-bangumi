import type { EpPropMergeType } from "element-plus/lib/utils/vue/index.js";

// 卡片tag
export interface CardTag {
  color: string; // 颜色
  content: string; // 标签内容
}

export enum EpisodeRenameTaskStatusEnum {
  NONE = 0, // 初始状态，未处理
  PENDING = 1, // 等待处理
  PROCESSING = 2, // 处理中
  FINISHED = 3 // 处理结束
}

export const EpisodeRenameTaskStatusTagMap = new Map<number, CardTag>();
EpisodeRenameTaskStatusTagMap.set(EpisodeRenameTaskStatusEnum.NONE, {
  color: "#e6a23c",
  content: "未处理"
});
EpisodeRenameTaskStatusTagMap.set(EpisodeRenameTaskStatusEnum.PENDING, {
  color: "#00a870",
  content: "等待处理"
});
EpisodeRenameTaskStatusTagMap.set(EpisodeRenameTaskStatusEnum.PROCESSING, {
  color: "#909399",
  content: "处理中"
});
EpisodeRenameTaskStatusTagMap.set(EpisodeRenameTaskStatusEnum.FINISHED, {
  color: "#909399",
  content: "结束"
});

export enum EpisodeRenameTaskItemStatusEnum {
  NONE = 0, // 初始状态，未处理
  PARSED = 1, // 解析完成
  PENDING = 2, // 等待执行
  SUCCESS = 3, // 成功
  SKIPPED = 95, // 跳过
  TITLE_PARSE_FAILED = 96, // 解析标题失败
  IGNORED = 97, // 忽略
  FILTERED_OUT = 98, // 被过滤掉的
  FAILED = 99 // 失败
}

// 状态tag
export interface EpisodeRenameTaskItemStatusTag {
  tagType: EpPropMergeType<
    StringConstructor,
    "success" | "warning" | "info" | "primary" | "danger",
    unknown
  >;
  tagContent: string;
}

// task item 状态的tag映射
export const EpisodeRenameTaskItemStatusTagMap = new Map<
  number,
  EpisodeRenameTaskItemStatusTag
>();

EpisodeRenameTaskItemStatusTagMap.set(EpisodeRenameTaskItemStatusEnum.NONE, {
  tagType: "primary",
  tagContent: "未处理"
});
EpisodeRenameTaskItemStatusTagMap.set(EpisodeRenameTaskItemStatusEnum.PARSED, {
  tagType: "primary",
  tagContent: "已解析"
});
EpisodeRenameTaskItemStatusTagMap.set(EpisodeRenameTaskItemStatusEnum.PENDING, {
  tagType: "primary",
  tagContent: "等待执行"
});
EpisodeRenameTaskItemStatusTagMap.set(EpisodeRenameTaskItemStatusEnum.SUCCESS, {
  tagType: "success",
  tagContent: "成功"
});
EpisodeRenameTaskItemStatusTagMap.set(EpisodeRenameTaskItemStatusEnum.SKIPPED, {
  tagType: "info",
  tagContent: "跳过"
});
EpisodeRenameTaskItemStatusTagMap.set(
  EpisodeRenameTaskItemStatusEnum.TITLE_PARSE_FAILED,
  {
    tagType: "danger",
    tagContent: "标题解析失败"
  }
);
EpisodeRenameTaskItemStatusTagMap.set(EpisodeRenameTaskItemStatusEnum.IGNORED, {
  tagType: "info",
  tagContent: "忽略"
});
EpisodeRenameTaskItemStatusTagMap.set(
  EpisodeRenameTaskItemStatusEnum.FILTERED_OUT,
  {
    tagType: "info",
    tagContent: "过滤"
  }
);
EpisodeRenameTaskItemStatusTagMap.set(EpisodeRenameTaskItemStatusEnum.FAILED, {
  tagType: "danger",
  tagContent: "失败"
});

// 重命名任务的状态下拉列表
export const EpisodeRenameTaskStatusOptions = [
  {
    label: "未处理",
    value: EpisodeRenameTaskStatusEnum.NONE,
    color: "blue"
  },
  {
    label: "等待中",
    value: EpisodeRenameTaskStatusEnum.PENDING,
    color: "blue"
  },
  {
    label: "处理中",
    value: EpisodeRenameTaskStatusEnum.PROCESSING,
    color: "blue"
  },
  {
    label: "结束",
    value: EpisodeRenameTaskStatusEnum.FINISHED,
    color: "blue"
  }
];

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
    label: "Torrent parsed title",
    value: EpisodeTitleRenameMethodEnum.TORRENT_PARSED_TITLE,
    color: "blue"
  },
  {
    label: "Customized title",
    value: EpisodeTitleRenameMethodEnum.CUSTOMIZED_TITLE,
    color: "blue"
  }
];
