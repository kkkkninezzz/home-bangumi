import { http } from "@/utils/http";
import { resetRouter } from "@/router";
import { useUserStoreHook } from "@/store/modules/user";
import type {
  RequestMethods,
  PureHttpRequestConfig
} from "@/utils/http/types.d";

import Axios, { type AxiosError, type AxiosRequestConfig } from "axios";

import { message } from "@/utils/message";

export type ApiResult = {
  success: boolean;
  code: string;
  msg: string;
};

const defaultResult: unknown = {
  success: false,
  code: "999999",
  msg: "ERR_NETWORK"
};

export type PagedReq = {
  /** 当前页 */
  current: number;
  /** 每页大小 */
  pageSize: number;
  /** 查询条件 */
  //condition: T;
};

export type PagedResp = {
  /** 当前页 */
  current: number;
  /** 每页大小 */
  pageSize: number;
  /** 总数 */
  total: number;
  /** 列表数据 */
  //list: T;
};

class HbHttp {
  /** 通用请求工具函数 */
  public request<T extends ApiResult>(
    method: RequestMethods,
    url: string,
    param?: AxiosRequestConfig,
    axiosConfig?: PureHttpRequestConfig
  ): Promise<T> {
    return new Promise<T>((resolve, reject) => {
      http
        .request<T>(method, url, param, axiosConfig)
        .then(data => {
          if (data) {
            if (!data.success) {
              message(data.msg, { type: "warning" });
            }
            resolve(data);
          }
        })
        .catch(error => {
          const axiosError = error as AxiosError;
          const status = axiosError.response?.status;
          // 如果状态为401那么认为是未登录，跳转到登录页
          if (status && status == 401) {
            message("Login expired", { type: "warning" });
            useUserStoreHook().logOut();
            return;
          }

          message(axiosError.code, { type: "error" });
          console.log("HbHttp error:", error);
          resolve(defaultResult as T);
        });
    });
  }
}

export const hbHttp = new HbHttp();
