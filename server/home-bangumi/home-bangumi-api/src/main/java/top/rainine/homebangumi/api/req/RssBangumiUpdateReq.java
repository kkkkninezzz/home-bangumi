package top.rainine.homebangumi.api.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.api.annotation.valid.ValidEnumValue;
import top.rainine.homebangumi.api.common.BangumiInfoDto;
import top.rainine.homebangumi.def.enums.DownloaderCategoryEnum;
import top.rainine.homebangumi.def.enums.EpisodeTitleRenameMethodEnum;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

import java.util.List;

/**
 * @authoer rainine
 * @date 2024/4/20 16:45
 * @desc 更新rss bangumi的请求
 */
@Getter
@Setter
@ToString
public class RssBangumiUpdateReq {

    /**
     * rss链接名
     * */
    private String rssName;

    /**
     * 过滤规则
     * */
    private List<String> filterRules;

    /**
     * 跳过的剧集号，如果设置为1，表示从第2集开始，忽略第二集之前的
     * */
    private Integer skippedEpisodeNo;

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
     * @see RssBangumiUpdateReq#episodeTitleRenameMethod == {@link EpisodeTitleRenameMethodEnum#CUSTOMIZED_TITLE}
     * 自定义的重命名后标题格式
     * 支持占位符 {season} {episode}
     * */
    private String customizeRenamedEpisodeTitleFormat;

    /**
     * 番剧信息
     * */
    @NotNull
    @Valid
    private BangumiInfoDto bangumiInfo;
}





















