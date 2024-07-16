package top.rainine.homebangumi.api.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @authoer rainine
 * @date 2024/5/1 11:51
 * @desc
 */
@Getter
@Setter
@ToString
public class RenameEpisodeFileNameReq {

    /**
     * 新的文件名
     * */
    @NotBlank(message = HbCodeEnum.ErrorCode.RENAMED_EPISODE_FILE_NAME_INVALID)
    private String newFileName;
}
