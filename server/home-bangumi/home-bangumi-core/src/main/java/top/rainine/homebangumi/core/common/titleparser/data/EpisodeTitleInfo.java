package top.rainine.homebangumi.core.common.titleparser.data;

import lombok.*;

/**
 * @param group   组
 * @param title   标题
 * @param season  季数
 * @param episode 集数
 * @author rainine
 * @description 剧集标题信息
 * @date 2024/3/14 12:00:21
 */
@Builder
public record EpisodeTitleInfo(String group, String title, int season, int episode) {

}
