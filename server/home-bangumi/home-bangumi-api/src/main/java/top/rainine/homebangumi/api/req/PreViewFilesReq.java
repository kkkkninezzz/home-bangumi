package top.rainine.homebangumi.api.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @authoer rainine
 * @date 2024/10/7 21:22
 * @desc
 */
@Getter
@Setter
@ToString
public class PreViewFilesReq {

    /**
     * 文件路径
     * */
    @NotBlank(message = HbCodeEnum.ErrorCode.PRE_VIEW_FILES_PATH_INVALID)
    private String path;
}
