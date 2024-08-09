export interface EpisodeRenameTaskCardPropType {
  id: number;
  taskName: string; // 任务名
  taskStatus: number; // 任务状态
  totalCount: number; // 所有的任务项数量
  pendingCount: number; // 等待处理的数量
  successCount: number; // 成功的数量
  failedCount: number; // 失败的数量
}
