package top.rainine.homebangumi.api.resp;

import lombok.*;
import top.rainine.homebangumi.def.enums.DownloaderCategoryEnum;
import top.rainine.homebangumi.def.enums.RssBangumiEpisodeStatusEnum;

/**
 * @author rainine
 * @description 解析出来的剧集信息
 * @date 2024/4/8 18:06:17
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RssBangumiEpisodeDto {
    private Long id;

    /**
     * 剧集编号
     * 一般来说为01，02
     * */
    private Integer episodeNo;

    /**
     * 原始的剧集号
     * */
    private Integer rawEpisodeNo;

    /**
     * 原始的剧集名
     * */
    private String rawEpisodeTitle;

    /**
     * 种子中获取到的剧集文件名
     * */
    private String episodeFileName;

    /**
     * 重命名后的剧集文件名
     * */
    private String renamedEpisodeFileName;

    /**
     * 下载完成后保存的路径
     * */
    private String episodeStoredPath;

    /**
     * 重命名后的剧集保存路径
     * */
    private String renamedEpisodeStoredPath;

    /**
     * 种子的发布时间
     * 毫秒时间戳
     * */
    private Long torrentPubDate;

    /**
     * 种子链接
     * */
    private String torrentLink;

    /**
     * 种子下载后的本地路径
     * */
    private String torrentStoredPath;

    /**
     * 种子在下载器中的唯一标识
     * 当提交到下载器中后，该字段就可以获取到值了
     * */
    private String torrentIdentity;

    /**
     * 剧集状态
     * @see RssBangumiEpisodeStatusEnum#getStatus()
     * */
    private Integer status;

    /**
     * 错误消息
     * */
    private String errorMessage;

    /**
     * 下载器类型
     * @see DownloaderCategoryEnum#getCategory()
     * */
    private Integer downloaderCategory;
}
