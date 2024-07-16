export interface RssBangumiEpisodeTimelineCardPropType {
  id: number; // rssBnagumi id
  bangumiTitle: string; // 番剧标题
  bangumiPosterUrl: string; // 番剧海报地址
  latestEpisodeNo: number; // 更新的最新一集剧集编号
  latestUpdatedTime: number; // 更新的时间
  delay?: boolean; // 是否延迟更新
  actualBroadcastDayOfWeek?: number; // 实际更新的星期
  expectedBroadcastDayOfWeek: number; // 预计播放的星期
}

export interface AlterMessageCardPropType {
  id: number; // rssBnagumi id
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