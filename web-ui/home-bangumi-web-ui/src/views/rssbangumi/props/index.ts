
export interface RssBangumiCardPropType {
  id: number;
  rssName: string; // rss链接名
  rssCategory: number; // 链接类型
  handleMethod: number; // 链接类型
  status: number; // 链接状态
  bangumiTitle: string; // 番剧标题
  posterUrl: string; // 番剧海报地址
  broadcastDayOfWeek: number; // 放送的星期几
  broadcastDate: number; //开始放送的日期
  season: number; // 放送的是哪一季
}