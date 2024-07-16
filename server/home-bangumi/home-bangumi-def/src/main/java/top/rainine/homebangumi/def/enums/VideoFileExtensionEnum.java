package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.rainine.homebangumi.def.utils.EnumUtils;

/**
 * @author rainine
 * @description 视频文件后缀枚举
 * @date 2024/5/6 17:21:52
 */
@Getter
@AllArgsConstructor
public enum VideoFileExtensionEnum {

    MP4("mp4"),

    MKV("mkv"),

    AVI("avi"),

    MOV("mov"),

    WMV("wmv"),
    ;

    private final String extension;

    private static final VideoFileExtensionEnum[] ENUMS = values();

    public static boolean contains(String extension) {
        return EnumUtils.contains(ENUMS, VideoFileExtensionEnum::getExtension, extension);
    }
}
