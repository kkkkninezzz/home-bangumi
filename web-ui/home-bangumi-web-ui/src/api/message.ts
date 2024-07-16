import { ApiResult, hbHttp } from "./base";

export type MessageDto = {
  id: number;

  /**
   * 消息分类
   * @see MessageCategoryEnum#getCategory()
   * */
  category: number;

  /**
   * 消息类型
   * @see MessageTypeEnum#getType()
   * */
  type: number;

  /**
   * 是否已读
   * */
  read: boolean;

  /**
   * 消息标题
   * */
  title: string;

  /**
   * 消息内容
   * */
  content: string;

  /**
   * 主体id
   * */
  subjectId: string;

  /**
   * 创建时间
   * */
  createdTime: number;
}

export type MessagesResp = {
  data: {
    messages: Array<MessageDto>;
  }
} & ApiResult;


/** 获取消息列表 */
export const getMessages = () => { 
  return hbHttp.request<MessagesResp>("get", `/api/v1/message/list`, { });
}

/** 读消息 */
export const readMessage = (id: number) => { 
  return hbHttp.request<ApiResult>("put", `/api/v1/message/read/${id}`, { });
}


/** 读所有的消息 */
export const readAllMessages = () => { 
  return hbHttp.request<ApiResult>("put", `/api/v1/message/read/all`, { });
}