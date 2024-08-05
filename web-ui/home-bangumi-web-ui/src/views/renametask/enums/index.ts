import type { EpPropMergeType } from "element-plus/lib/utils/vue/index.js";

export enum EpisodeRenameTaskStatusEnum {
  NONE = 0, // 初始状态，未处理
  PENDING = 1, // 等待处理
  PROCESSING = 2, // 处理中
  FINISHED = 3 // 处理结束
}

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
