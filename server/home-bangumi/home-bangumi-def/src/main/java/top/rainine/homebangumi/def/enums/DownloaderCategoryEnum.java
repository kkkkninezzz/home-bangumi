package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.rainine.homebangumi.def.utils.EnumUtils;

/**
 * @author rainine
 * @description 下载器分类
 * @date 2024/5/10 17:05:44
 */
@Getter
@AllArgsConstructor
public enum DownloaderCategoryEnum {
    /**
     * qbittorrent
     * */
    QBITTORRENT(1),

    /**
     * 迅雷下载器
     * */
    XUNLEI(2),
    ;

    private final int category;

    private static final DownloaderCategoryEnum[] ENUMS = values();

    public static boolean contains(Integer value) {
        return EnumUtils.containsInteger(ENUMS, DownloaderCategoryEnum::getCategory, value);
    }

    public static DownloaderCategoryEnum of(Integer value) {
        return EnumUtils.ofInteger(ENUMS, DownloaderCategoryEnum::getCategory, value, null);
    }
}
