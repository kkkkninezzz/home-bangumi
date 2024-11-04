package top.rainine.homebangumi.api.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.api.annotation.valid.ValidEnumValue;
import top.rainine.homebangumi.def.enums.EpisodeTitleRenameMethodEnum;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

import java.util.List;

/**
 * @author rainine
 * @description 更新剧集重命名任务请求
 * @date 2024/7/17 15:02:36
 */
@Getter
@Setter
@ToString
public class UpdateEpisodeRenameTaskReq {

    /**
     * 任务名称
     * */
    @NotEmpty(message = HbCodeEnum.ErrorCode.EPISODE_RENAME_TASK_NAME_INVALID)
    private String taskName;

    /**
     * 剧集季度
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.BANGUMI_SEASON_INVALID)
    @Min(value = 0, message = HbCodeEnum.ErrorCode.BANGUMI_SEASON_INVALID)
    private Integer season;

    /**
     * 剧集的偏移量
     * */
    private Integer episodeNoOffset = 0;

    /**
     * 剧集目录路径
     * */
    @NotEmpty(message = HbCodeEnum.ErrorCode.EPISODE_DIR_PATH_INVALID)
    private String episodeDirPath;

    /**
     * 遍历剧集目录路径的最大深度
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.EPISODE_DIR_PATH_MAX_DEPTH_INVALID)
    @Min(value = 1, message = HbCodeEnum.ErrorCode.EPISODE_DIR_PATH_MAX_DEPTH_INVALID)
    private Integer episodeDirPathMaxDepth;

    /**
     * 重命名后输出的目录路径
     * */
    @NotEmpty(message = HbCodeEnum.ErrorCode.RENAMED_EPISODE_OUTPUT_DIR_PATH_INVALID)
    private String renamedOutputDirPath;

    /**
     * 剧集标题重命名方式
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.EPISODE_TITLE_RENAME_METHOD_ENUM_INVALID)
    @ValidEnumValue(enumClass = EpisodeTitleRenameMethodEnum.class, message = HbCodeEnum.ErrorCode.EPISODE_TITLE_RENAME_METHOD_ENUM_INVALID)
    private Integer episodeTitleRenameMethod;

    /**
     * @see UpdateEpisodeRenameTaskReq#episodeTitleRenameMethod == {@link EpisodeTitleRenameMethodEnum#CUSTOMIZED_TITLE}
     * 自定义的重命名后标题格式
     * 支持占位符 {season} {episode}
     * */
    private String customizeRenamedEpisodeTitleFormat;

    /**
     * 跳过的剧集号，如果设置为1，表示从第2集开始，忽略第二集之前的
     * */
    private Integer skippedEpisodeNo = 0;

    /**
     * 过滤规则
     * */
    private List<String> filteredOutRules;

    /**
     * 是否删除源文件
     * */
    private Boolean deleteSourceFile;

    /**
     * 覆盖已存在文件
     * */
    private Boolean overwriteExistingFile;
}
