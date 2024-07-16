package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.rainine.homebangumi.def.utils.EnumUtils;

/**
 * @author rainine
 * @description rss链接的分类
 * @date 2024/3/12 18:24:27
 */
@Getter
@AllArgsConstructor
public enum RssCategoryEnum {

    /**
     * 蜜柑计划
     * https://mikanani.me
     * */
    MIKAN(1);

    private final int category;

    private static final RssCategoryEnum[] ENUMS = values();

    public static boolean contains(Integer value) {
        return EnumUtils.containsInteger(ENUMS, RssCategoryEnum::getCategory, value);
    }

    public static RssCategoryEnum of(Integer value) {
        return EnumUtils.ofInteger(ENUMS, RssCategoryEnum::getCategory, value, null);
    }
}
