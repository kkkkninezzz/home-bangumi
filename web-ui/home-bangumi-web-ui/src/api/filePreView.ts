import { type ApiResult, hbHttp } from "./base";

export type ChildFileInfo = {
  /**
   * 绝对路径
   */
  path: string;

  /**
   * 当前的文件名或者目录名
   *
   * */
  name: string;

  /**
   * 是否为文件
   * */
  isFile: boolean;
};

export type PreViewFilesResp = {
  data: {
    /**
     * 绝对路径
     */
    path: string;

    /**
     * 当前的文件名或者目录名
     *
     * */
    name: string;

    /**
     * 是否为文件
     * */
    isFile: boolean;
    children: Array<ChildFileInfo>;
  };
} & ApiResult;

type PreViewFilesReq = {
  /**
   * 绝对路径
   */
  path: string;
};

/** 获取消息列表 */
export const preViewFiles = (path: string) => {
  const data: PreViewFilesReq = {
    path: path
  };

  return hbHttp.request<PreViewFilesResp>("post", `/api/v1/file/pre-view`, {
    data
  });
};
