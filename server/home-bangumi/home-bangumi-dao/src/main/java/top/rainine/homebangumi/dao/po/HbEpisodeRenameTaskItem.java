package top.rainine.homebangumi.dao.po;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskItemStatusEnum;

/**
 * @author rainine
 * @description 剧集重命名任务明细
 * @date 2024/7/17 10:38:19
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "hb_episode_rename_task_item", indexes = {
        @Index(name = "idx_task_id", columnList = "task_id")
})
public class HbEpisodeRenameTaskItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属的任务id
     * @see HbEpisodeRenameTask#getId()
     * */
    @Column(name = "task_id", nullable = false)
    private Long taskId;

    /**
     * 状态
     * @see EpisodeRenameTaskItemStatusEnum#getStatus()
     * */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 获取到的剧集文件名
     * */
    @Column(name = "episode_file_name", length = 256)
    private String episodeFileName;

    /**
     * 重命名后的剧集文件名
     * */
    @Column(name = "renamed_episode_file_name", length = 256)
    private String renamedEpisodeFileName;

    /**
     * 剧集文件源路径
     * */
    @Column(name = "episode_path", length = 512)
    private String episodePath;

    /**
     * 重命名后的剧集输出路径
     * */
    @Column(name = "renamed_episode_output_path", length = 512)
    private String renamedEpisodeOutputPath;

    /**
     * 解析出来的剧集编号
     * 一般来说为01，02
     * */
    @Column(name = "episode_No")
    private Integer episodeNo;

    /**
     * 解析出来的原始剧集号
     * */
    @Column(name = "raw_episode_no")
    private Integer rawEpisodeNo;

    /**
     * 错误消息
     * */
    @Column(name = "error_message", length = 512)
    private String errorMessage;
}
