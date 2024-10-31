package top.rainine.homebangumi.dao.po;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskStatusEnum;
import top.rainine.homebangumi.def.enums.EpisodeTitleRenameMethodEnum;

/**
 * @author rainine
 * @description 剧集重命名任务
 * @date 2024/7/17 10:38:19
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "hb_episode_rename_task", indexes = {
})
public class HbEpisodeRenameTask extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 任务名称
     * */
    @Column(name = "task_name", length = 256, nullable = false)
    private String taskName;

    /**
     * 放送的是哪一季
     * */
    @Column(name = "season", nullable = false)
    private Integer season;

    /**
     * 剧集号的偏移，作用于解析出来的剧集号
     * 在此基础上，加上偏移量再显示给用户
     * */
    @Column(name = "episode_no_offset")
    private Integer episodeNoOffset;

    /**
     * 任务状态
     * @see EpisodeRenameTaskStatusEnum#getStatus()
     * */
    @Column(name = "task_status", nullable = false)
    private Integer taskStatus;

    /**
     * 剧集目录路径
     * */
    @Column(name = "episode_dir_path", length = 512, nullable = false)
    private String episodeDirPath;

    /**
     * 剧集目录路径，遍历时的深度，默认为1
     * */
    @Column(name = "episode_dir_path_max_depth", nullable = false, columnDefinition = "integer default 1")
    private Integer episodeDirPathMaxDepth;

    /**
     * 重命名后输出的目录路径
     * */
    @Column(name = "renamed_output_dir_path", length = 512, nullable = false)
    private String renamedOutputDirPath;

    /**
     * 剧集标题重命名方式
     * @see EpisodeTitleRenameMethodEnum#getMethod()
     * */
    @Column(name = "episode_title_rename_method", nullable = false)
    private Integer episodeTitleRenameMethod;

    /**
     * 自定义重命名后的剧集标题格式
     * 包含season、episodeNo的占位符
     * */
    @Column(name = "customize_rename_episode_title_format", length = 256)
    private String customizeRenamedEpisodeTitleFormat;

    /**
     * 跳过的剧集号，如果设置为1，表示从第2集开始，忽略第二集之前的
     * */
    @Column(name = "skipped_episode_no")
    private Integer skippedEpisodeNo;

    /**
     * 过滤规则
     * 格式为json数组字符串，每个元素为一个用于匹配的词组
     * */
    @Column(name = "filtered_out_rules", length = 512, nullable = false)
    private String filteredOutRules;

    /**
     * 是否删除源文件
     * */
    @Column(name = "delete_source_file", nullable = false)
    private Boolean deleteSourceFile;

    /**
     * 覆盖已存在文件
     * */
    @Column(name = "overwrite_existing_file", nullable = false)
    private Boolean overwriteExistingFile;
}
