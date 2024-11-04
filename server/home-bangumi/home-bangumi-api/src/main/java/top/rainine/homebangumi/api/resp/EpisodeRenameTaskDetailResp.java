package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskStatusEnum;
import top.rainine.homebangumi.def.enums.EpisodeTitleRenameMethodEnum;

import java.util.List;

/**
 * @author rainine
 * @description
 * @date 2024/7/17 15:59:46
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class EpisodeRenameTaskDetailResp {

    /**
     * 任务id
     * */
    private Long id;

    /**
     * 任务名称
     * */
    private String taskName;

    /**
     * 放送的是哪一季
     * */
    private Integer season;

    /**
     * 剧集的偏移量
     * */
    private Integer episodeNoOffset;

    /**
     * 任务状态
     * @see EpisodeRenameTaskStatusEnum#getStatus()
     * */
    private Integer taskStatus;

    /**
     * 剧集目录路径
     * */
    private String episodeDirPath;

    /**
     * 遍历剧集目录路径的最大深度
     * */
    private Integer episodeDirPathMaxDepth;

    /**
     * 重命名后输出的目录路径
     * */
    private String renamedOutputDirPath;

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
     * 过滤规则
     * 格式为json数组字符串，每个元素为一个用于匹配的词组
     * */
    private List<String> filteredOutRules;

    /**
     * 跳过的剧集号，如果设置为1，表示从第2集开始，忽略第二集之前的
     * */
    private Integer skippedEpisodeNo;

    /**
     * 是否删除源文件
     * */
    private Boolean deleteSourceFile;

    /**
     * 覆盖已存在文件
     * */
    private Boolean overwriteExistingFile;

    /**
     * 创建时间
     * */
    private Long createdTime;
}
