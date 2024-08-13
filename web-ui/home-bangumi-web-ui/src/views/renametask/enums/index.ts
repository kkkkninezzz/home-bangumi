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
