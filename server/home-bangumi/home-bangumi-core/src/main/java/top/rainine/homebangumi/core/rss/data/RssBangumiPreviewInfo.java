package top.rainine.homebangumi.core.rss.data;

import lombok.Builder;
import top.rainine.homebangumi.def.enums.EpisodeTitleRenameMethodEnum;
import top.rainine.homebangumi.def.enums.RssCategoryEnum;

import java.util.List;

/**
 * @param rssCategory               rss分类
 * @param title                     番剧标题
 * @param posterStoredPath          番剧海报存储的路径
 * @param posterUrl                 番剧海报资源路径
 * @param broadcastDayOfWeek        放送的星期几
 *                                  从1开始到7
 *                                  如果-1，那么认为暂无信息
 * @param broadcastDate             开始放送的日期，毫秒时间戳
 * @param season                    放送的是哪一季
 * @param episodeTitleRenameMethod  重命名方式
 * @param customizeRenamedEpisodeTitleFormat 自定义重命名剧集标题的格式
 * @param episodes                  剧集列表
 * @author rainine
 * @description 生成的番剧预览信息
 * @date 2024/3/14 18:54:39
 */
@Builder
public record RssBangumiPreviewInfo(RssCategoryEnum rssCategory,
                                    String title,
                                    String posterStoredPath,
                                    String posterUrl,
                                    Integer broadcastDayOfWeek,
                                    Long broadcastDate,
                                    Integer season,
                                    EpisodeTitleRenameMethodEnum episodeTitleRenameMethod,
                                    String customizeRenamedEpisodeTitleFormat,
                                    List<RssBangumiEpisodePreviewInfo> episodes) {

}
