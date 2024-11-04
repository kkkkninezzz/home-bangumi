package top.rainine.homebangumi.dao.po;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.DownloaderCategoryEnum;
import top.rainine.homebangumi.def.enums.RssBangumiEpisodeStatusEnum;

/**
 * @author rainine
 * @description rss番剧剧集信息
 * @date 2024/3/13 17:10:37
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "hb_rss_bangumi_episode",
        indexes = {
        @Index(name = "idx_rss_bangumi_id", columnList = "rss_bangumi_id")
        })
public class HbRssBangumiEpisode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属的rss番剧id
     * @see HbRssBangumi#getId()
     * */
    @Column(name = "rss_bangumi_id", nullable = false)
    private Long rssBangumiId;

    /**
     * 剧集编号，可能经过偏移处理，非原始编号
     * 一般来说为01，02
     * 可能有特殊的ova1
     * */
    @Column(name = "episode_No")
    private Integer episodeNo;

    /**
     * 解析出来的原始剧集号
     * */
    @Column(name = "raw_episode_no")
    private Integer rawEpisodeNo;

    /**
     * 原始的剧集名
     * */
    @Column(name = "raw_episode_title", length = 256, nullable = false)
    private String rawEpisodeTitle;

    /**
     * 种子中获取到的剧集文件名
     * */
    @Column(name = "episode_file_name", length = 256)
    private String episodeFileName;

    /**
     * 重命名后的剧集文件名
     * */
    @Column(name = "renamed_episode_file_name", length = 256)
    private String renamedEpisodeFileName;

    /**
     * 下载完成后保存的路径
     * */
    @Column(name = "episode_stored_path", length = 512)
    private String episodeStoredPath;

    /**
     * 重命名后的剧集保存路径
     * */
    @Column(name = "renamed_episode_stored_path", length = 512)
    private String renamedEpisodeStoredPath;

    /**
     * 种子的发布时间
     * 毫秒时间戳
     * */
    @Column(name = "torrent_pub_date")
    private Long torrentPubDate;

    /**
     * 种子链接
     * */
    @Column(name = "torrent_link", length = 1024)
    private String torrentLink;

    /**
     * 种子下载后的本地路径
     * */
    @Column(name = "torrent_stored_path", length = 512)
    private String torrentStoredPath;

    /**
     * 种子在下载器中的唯一标识
     * 当提交到下载器中后，该字段就可以获取到值了
     * */
    @Column(name = "torrent_identity", length = 64)
    private String torrentIdentity;

    /**
     * 剧集状态
     * @see RssBangumiEpisodeStatusEnum#getStatus()
     * */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 错误消息
     * */
    @Column(name = "error_message", length = 512)
    private String errorMessage;

    /**
     * 下载器类型，默认是qb类型
     * @see DownloaderCategoryEnum#getCategory()
     * */
    @Column(name = "downloader_category", nullable = false, columnDefinition = "integer default 1")
    private Integer downloaderCategory;
}







