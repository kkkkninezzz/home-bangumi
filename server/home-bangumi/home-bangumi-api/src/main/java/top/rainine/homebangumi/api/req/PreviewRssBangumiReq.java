package top.rainine.homebangumi.api.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.api.annotation.valid.ValidEnumValue;
import top.rainine.homebangumi.def.enums.*;

import java.util.List;

/**
 * @authoer rainine
 * @date 2024/4/7 23:54
 * @desc 预览rss bangumi的请求
 */
@Getter
@Setter
@ToString
public class PreviewRssBangumiReq {

    /**
     * rss的分类
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.RSS_CATEGORY_INVALID)
    @ValidEnumValue(enumClass = RssCategoryEnum.class, message = HbCodeEnum.ErrorCode.RSS_CATEGORY_INVALID)
    private Integer rssCategory;

    /**
     * rss链接名
     * */
    private String rssName;

    /**
     * rss链接地址
     * */
    @NotBlank(message = HbCodeEnum.ErrorCode.RSS_LINK_INVALID)
    private String rssLink;

    /**
     * 过滤规则
     * */
    private List<String> filterRules;

    /**
     * 剧集的偏移量，如果设置为1，表示从第2集开始，忽略第二集之前的
     * */
    private Integer episodeOffset;

    /**
     * 收集方式
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.RSS_HANDLE_METHOD_INVALID)
    @ValidEnumValue(enumClass = RssHandleMethodEnum.class, message = HbCodeEnum.ErrorCode.RSS_HANDLE_METHOD_INVALID)
    private Integer handleMethod;

    /**
     * 下载器分类
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.DOWNLOADER_CATEGORY_INVALID)
    @ValidEnumValue(enumClass = DownloaderCategoryEnum.class, message = HbCodeEnum.ErrorCode.DOWNLOADER_CATEGORY_INVALID)
    private Integer downloaderCategory;

    /**
     * 剧集标题重命名方式
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.EPISODE_TITLE_RENAME_METHOD_ENUM_INVALID)
    @ValidEnumValue(enumClass = EpisodeTitleRenameMethodEnum.class, message = HbCodeEnum.ErrorCode.EPISODE_TITLE_RENAME_METHOD_ENUM_INVALID)
    private Integer episodeTitleRenameMethod;

    /**
     * @see PreviewRssBangumiReq#episodeTitleRenameMethod == {@link EpisodeTitleRenameMethodEnum#CUSTOMIZED_TITLE}
     * 自定义的重命名后标题格式
     * 支持占位符 {season} {episode}
     * */
    private String customizeRenamedEpisodeTitleFormat;
}



