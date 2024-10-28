package top.rainine.homebangumi.api.resp;

import lombok.*;
import top.rainine.homebangumi.api.common.BangumiInfoDto;
import top.rainine.homebangumi.def.enums.*;

import java.util.List;

/**
 * @authoer rainine
 * @date 2024/4/8 00:06
 * @desc rss bangumi 详情返回值
 */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class RssBangumiDetailResp {

    private Long id;

    /**
     * 链接名
     * */
    private String rssName;

    /**
     * rss类型
     * @see RssCategoryEnum#getCategory()
     * */
    private Integer rssCategory;

    /**
     * 下载器类型
     * @see DownloaderCategoryEnum#getCategory()
     * */
    private Integer downloaderCategory;

    /**
     * rss链接
     * */
    private String rssLink;

    /**
     * 处理方式
     * @see RssHandleMethodEnum#getMethod()
     * */
    private Integer handleMethod;

    /**
     * 记录状态
     * @see RssBangumiStatusEnum#getStatus()
     * */
    private Integer status;

    /**
     * 过滤规则
     * */
    private List<String> filterRules;

    /**
     * 跳过的剧集号，如果设置为1，表示从第2集开始，忽略第二集之前的
     * */
    private Integer skippedEpisodeNo;

    /**
     * 剧集的偏移量
     * */
    private Integer episodeNoOffset;

    /**
     * 剧集标题重命名方式
     * @see EpisodeTitleRenameMethodEnum#getMethod()
     * */
    private Integer episodeTitleRenameMethod;

    /**
     * 自定义重命名后的剧集标题格式
     * 包含season、episodeNo的占位符
     * */
    private String customizeRenamedEpisodeTitleFormat;

    /**
     * 番剧信息
     * */
    private BangumiInfoDto bangumiInfo;
}
