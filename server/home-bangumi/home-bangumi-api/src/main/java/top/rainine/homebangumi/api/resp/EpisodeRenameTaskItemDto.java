package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.rainine.homebangumi.def.enums.EpisodeRenameTaskItemStatusEnum;

/**
 * @author rainine
 * @description 重命名任务项
 * @date 2024/7/17 16:06:22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class EpisodeRenameTaskItemDto {
    private Long id;

    /**
     * 所属的任务id
     * */
    private Long taskId;

    /**
     * 状态
     * @see EpisodeRenameTaskItemStatusEnum#getStatus()
     * */
    private Integer status;

    /**
     * 获取到的剧集文件名
     * */
    private String episodeFileName;

    /**
     * 重命名后的剧集文件名
     * */
    private String renamedEpisodeFileName;

    /**
     * 剧集文件源路径
     * */
    private String episodePath;

    /**
     * 重命名后的剧集输出路径
     * */
    private String renamedEpisodeOutputPath;

    /**
     * 解析出来的剧集编号
     * 一般来说为01，02
     * */
    private Integer episodeNo;

    /**
     * 错误消息
     * */
    private String errorMessage;
}
