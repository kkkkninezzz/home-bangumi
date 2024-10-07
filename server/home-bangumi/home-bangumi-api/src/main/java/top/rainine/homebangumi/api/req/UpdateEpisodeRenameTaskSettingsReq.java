package top.rainine.homebangumi.api.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @author rainine
 * @description 更新剧集重命名任务的设置请求
 * @date 2024/8/20 17:56:24
 */
@Getter
@Setter
@ToString
public class UpdateEpisodeRenameTaskSettingsReq {

    /**
     * 源目录路径
     * */
    @NotBlank(message = HbCodeEnum.ErrorCode.EPISODE_RENAME_TASK_SOURCE_DIR_PATH_INVALID)
    private String sourceDirPath;

    /**
     * 输出目录路径
     * */
    @NotBlank(message = HbCodeEnum.ErrorCode.EPISODE_RENAME_TASK_OUT_DIR_PATH_INVALID)
    private String outDirPath;
}
