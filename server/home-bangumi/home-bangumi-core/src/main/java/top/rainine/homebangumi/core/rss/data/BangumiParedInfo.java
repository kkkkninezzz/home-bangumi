package top.rainine.homebangumi.core.rss.data;

import lombok.Builder;

/**
 * @param title              番剧标题
 * @param posterStoredPath   番剧海报保存的路径
 * @param broadcastDayOfWeek 放送的星期几
 *                           从1开始到7
 *                           解析失败返回null
 * @param broadcastDate      开始放送的日期，毫秒时间戳，如果没有获取到，返回null
 * @param season             放送的是哪一季，如果没有获取到，返回null
 *
 * @author rainine
 * @description 番剧被解析出来的信息
 * @date 2024/3/19 17:39:28
 */
@Builder
public record BangumiParedInfo(String title,
                               String posterStoredPath,
                               Integer broadcastDayOfWeek,
                               Long broadcastDate,
                               Integer season) {
}
