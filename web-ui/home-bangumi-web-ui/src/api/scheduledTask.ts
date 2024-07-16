import { ApiResult, hbHttp } from "./base";

export type RunOnceScheduledTaskReq = {
  taskId: number;
}


/** 执行一次定时任务 */
export const runOnceTask = (data: RunOnceScheduledTaskReq) => { 
  return hbHttp.request<ApiResult>("post", `/api/v1/scheduled-task/run-once`, { data });
}