package top.rainine.homebangumi.api.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @author rainine
 * @description 手动解析剧集信息
 * @date 2024/5/6 17:14:52
 */
@Getter
@Setter
@ToString
public class ManualParseEpisodeReq {

    /**
     * 剧集编号
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.EPISODE_NO_INVALID)
    @Min(value = 0, message = HbCodeEnum.ErrorCode.EPISODE_NO_INVALID)
    private Integer episodeNo;

//
//    /**
//     * 文件扩展名
//     * */
//    @NotEmpty(message = HbCodeEnum.ErrorCode.VIDEO_FILE_EXTENSION_INVALID)
//    @ValidEnumValue(enumClass = VideoFileExtensionEnum.class, message = HbCodeEnum.ErrorCode.VIDEO_FILE_EXTENSION_INVALID)
//    private String fileExtension;

    /**
     * 重命名后的剧集文件名
     * */
    @NotBlank(message = HbCodeEnum.ErrorCode.RENAMED_EPISODE_FILE_NAME_INVALID)
    private String renamedEpisodeFileName;
}
