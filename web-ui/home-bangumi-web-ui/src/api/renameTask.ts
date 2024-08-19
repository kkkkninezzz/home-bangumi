import { type ApiResult, type PagedReq, type PagedResp, hbHttp } from "./base";

export type CreateEpisodeRenameTaskReq = {
  /**
   * 任务名称
   */
  taskName: string;

  /**
   * 剧集季度
   */
  season: number;

  /**
   * 剧集目录路径
   */
  episodeDirPath: string;

  /**
   * 重命名后输出的目录路径
   */
  renamedOutputDirPath: string;

  /**
   * 剧集标题重命名方式
   */
  episodeTitleRenameMethod: number;

  /**
   * 自定义的重命名后标题格式
   * 支持占位符 {season} {episode}
   */
  customizeRenamedEpisodeTitleFormat: string;

  /**
   * 过滤规则
   */
  filteredOutRules?: string[];

  /**
   * 是否删除源文件
   */
  deleteSourceFile?: boolean;

  /**
   * 覆盖已存在文件
   */
  overwriteExistingFile?: boolean;
};

/**
 * 创建剧集重命名任务返回
 */
export type CreateEpisodeRenameTaskResp = {
  data: {
    taskId: number;
  };
} & ApiResult;

/**
 * 剧集重命名任务详情返回
 */
export type EpisodeRenameTaskDetailResp = {
  data: {
    /**
     * 任务id
     */
    id: number;

    /**
     * 任务名称
     */
    taskName: string;

    /**
     * 放送的是哪一季
     */
    season: number;

    /**
     * 任务状态
     */
    taskStatus: number;

    /**
     * 剧集目录路径
     */
    episodeDirPath: string;

    /**
     * 重命名后输出的目录路径
     */
    renamedOutputDirPath: string;

    /**
     * 剧集标题重命名方式
     */
    episodeTitleRenameMethod: number;

    /**
     * 自定义重命名后的剧集标题格式
     * 包含season、episodeNo的占位符
     */
    customizeRenamedEpisodeTitleFormat: string;

    /**
     * 过滤规则
     * 格式为json数组字符串，每个元素为一个用于匹配的词组
     */
    filteredOutRules: string[];

    /**
     * 是否删除源文件
     */
    deleteSourceFile: boolean;

    /**
     * 覆盖已存在文件
     */
    overwriteExistingFile: boolean;

    /**
     * 创建时间
     */
    createdTime?: number;
  };
} & ApiResult;

export type EpisodeRenameTaskItemDto = {
  /**
   * ID
   */
  id: number;

  /**
   * 所属的任务id
   */
  taskId: number;

  /**
   * 状态
   */
  status: number;

  /**
   * 获取到的剧集文件名
   */
  episodeFileName: string;

  /**
   * 重命名后的剧集文件名
   */
  renamedEpisodeFileName: string;

  /**
   * 剧集文件源路径
   */
  episodePath: string;

  /**
   * 重命名后的剧集输出路径
   */
  renamedEpisodeOutputPath: string;

  /**
   * 解析出来的剧集编号
   * 一般来说为01，02
   */
  episodeNo: number;

  /**
   * 错误消息
   */
  errorMessage?: string;
};

/**
 * 重命名任务项
 */
export type EpisodeRenameTaskItemsResp = {
  data: {
    taskId: number;
    taskStatus: number;
    items: Array<EpisodeRenameTaskItemDto>;
  };
} & ApiResult;

export type UpdateEpisodeRenameTaskReq = {
  /**
   * 任务名称
   */
  taskName: string;

  /**
   * 剧集季度
   */
  season: number;

  /**
   * 剧集目录路径
   */
  episodeDirPath: string;

  /**
   * 重命名后输出的目录路径
   */
  renamedOutputDirPath: string;

  /**
   * 剧集标题重命名方式
   */
  episodeTitleRenameMethod: number;

  /**
   * 自定义的重命名后标题格式
   * 支持占位符 {season} {episode}
   */
  customizeRenamedEpisodeTitleFormat: string;

  /**
   * 过滤规则
   */
  filteredOutRules: string[];

  /**
   * 是否删除源文件
   */
  deleteSourceFile: boolean;

  /**
   * 覆盖已存在文件
   */
  overwriteExistingFile: boolean;
};

export type ManualParseRenameTaskItemReq = {
  /**
   * 剧集编号
   */
  episodeNo: number;

  /**
   * 重命名后的剧集文件名
   */
  renamedEpisodeFileName: string;
};

export type LoadPagedTasksReqConditionDto = {
  /**
   * 任务名，支持模糊查询
   */
  taskName?: string;

  /**
   * 任务状态
   */
  taskStatus?: number;
};

/** 查询分页列表请求 */
export type LoadPagedTasksReq = {
  condition: LoadPagedTasksReqConditionDto;
} & PagedReq;

export type PagedEpisodeRenameTaskItemDto = {
  /**
   * 任务id
   */
  id: number;

  /**
   * 任务名称
   */
  taskName: string;

  /**
   * 任务状态
   */
  taskStatus: number;

  /**
   * 所有的任务项数量
   */
  totalCount: number;

  /**
   * 等待处理的数量
   */
  pendingCount: number;

  /**
   * 成功的数量
   */
  successCount: number;

  /**
   * 失败的数量
   */
  failedCount: number;
};

export type LoadPagedEpisodeRenameTaskItemsResp = {
  data: {
    list: Array<PagedEpisodeRenameTaskItemDto>;
  } & PagedResp;
} & ApiResult;

/**
 * 创建重命名任务
 * @param data 请求参数
 * @returns
 */
export const createRenameTask = (data: CreateEpisodeRenameTaskReq) => {
  return hbHttp.request<CreateEpisodeRenameTaskResp>(
    "post",
    "/api/v1/episode/rename/task/create",
    { data }
  );
};

/**
 * 获取任务详情
 * @param id 任务id
 * @returns
 */
export const getTaskDetail = (id: number) => {
  return hbHttp.request<EpisodeRenameTaskDetailResp>(
    "get",
    `/api/v1/episode/rename/task/detail/${id}`,
    {}
  );
};

/**
 * 获取任务项
 * @param id 任务id
 * @returns
 */
export const getTaskItems = (id: number) => {
  return hbHttp.request<EpisodeRenameTaskItemsResp>(
    "get",
    `/api/v1/episode/rename/task/detail/${id}/items`,
    {}
  );
};

/** 更新任务信息 */
export const updateRssBangumi = (
  id: number,
  data: UpdateEpisodeRenameTaskReq
) => {
  return hbHttp.request<EpisodeRenameTaskDetailResp>(
    "put",
    `/api/v1/episode/rename/task/${id}`,
    { data }
  );
};

/**
 * 提交重命名任务
 * @param data 请求参数
 * @returns
 */
export const submitTask = (id: number) => {
  return hbHttp.request<ApiResult>(
    "post",
    `/api/v1/episode/rename/task/${id}/submit`,
    {}
  );
};

/**
 * 重新解析任务项
 * 注意: 该接口将删除所有已经解析出的任务项，并重新进行解析
 * @param data 请求参数
 * @returns
 */
export const reparseTaskItems = (id: number) => {
  return hbHttp.request<ApiResult>(
    "post",
    `/api/v1/episode/rename/task/${id}/items/reparse`,
    {}
  );
};

/**
 * 忽略任务项
 * @param data 请求参数
 * @returns
 */
export const ignoreTaskItem = (id: number, itemId: number) => {
  return hbHttp.request<ApiResult>(
    "put",
    `/api/v1/episode/rename/task/${id}/item/${itemId}/ignore`,
    {}
  );
};

/**
 * 手动解析任务项
 * @param data 请求参数
 * @returns
 */
export const manualParseTaskItem = (
  id: number,
  itemId: number,
  data: ManualParseRenameTaskItemReq
) => {
  return hbHttp.request<ApiResult>(
    "put",
    `/api/v1/episode/rename/task/${id}/item/${itemId}/manual-parse`,
    { data }
  );
};

/** 获取分页的任务列表 */
export const loadPagedTasks = (data: LoadPagedTasksReq) => {
  return hbHttp.request<LoadPagedEpisodeRenameTaskItemsResp>(
    "post",
    "/api/v1/episode/rename/task/paged/list",
    { data }
  );
};
