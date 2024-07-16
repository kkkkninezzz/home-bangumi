package top.rainine.homebangumi.dao.po;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.*;

/**
 * @author rainine
 * @description rss番剧信息
 * @date 2024/3/12 18:22:55
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "hb_rss_bangumi",
        indexes = {
        @Index(name = "idx_rss_link", columnList = "rss_link")
})
public class HbRssBangumi extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 链接名
     * */
    @Column(name = "rss_name", length = 128, nullable = false)
    private String rssName;

    /**
     * rss类型
     * @see RssCategoryEnum#getCategory()
     * */
    @Column(name = "rss_category", nullable = false)
    private Integer rssCategory;

    /**
     * rss链接
     * */
    @Column(name = "rss_link", length = 512, nullable = false)
    private String rssLink;

    /**
     * 处理方式
     * @see RssHandleMethodEnum#getMethod()
     * */
    @Column(name = "handle_method", nullable = false)
    private Integer handleMethod;

    /**
     * 记录状态
     * @see RssBangumiStatusEnum#getStatus()
     * */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 过滤规则
     * 格式为json数组字符串，每个元素为一个正则表达式
     * */
    @Column(name = "filter_rules", length = 512, nullable = false)
    private String filterRules;

    /**
     * 剧集的偏移量，如果设置为1，表示从第2集开始，忽略第二集之前的
     * */
    @Column(name = "episode_offset")
    private Integer episodeOffset;

    /**
     * 番剧id
     * @see HbBangumi#getId()
     * */
    @Column(name = "bangumi_id")
    private Long bangumiId;

    /**
     * 下载器类型，默认是qb类型
     * @see DownloaderCategoryEnum#getCategory()
     * */
    @Column(name = "downloader_category", nullable = false, columnDefinition = "integer default 1")
    private Integer downloaderCategory;

    /**
     * 剧集标题重命名方式
     * @see EpisodeTitleRenameMethodEnum#getMethod()
     * */
    @Column(name = "episode_title_rename_method", nullable = false, columnDefinition = "integer default 1")
    private Integer episodeTitleRenameMethod;

    /**
     * 自定义重命名后的剧集标题格式
     * 包含season、episodeNo的占位符
     * */
    @Column(name = "customize_rename_episode_title_format", length = 256)
    private String customizeRenamedEpisodeTitleFormat;
}
