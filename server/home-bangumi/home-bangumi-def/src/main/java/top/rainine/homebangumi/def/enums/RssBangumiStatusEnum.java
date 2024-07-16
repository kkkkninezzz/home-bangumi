package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.rainine.homebangumi.def.utils.EnumUtils;

import java.util.Objects;

/**
 * @authoer rainine
 * @date 2024/3/12 21:07
 * @desc rss番剧状态
 */
@Getter
@AllArgsConstructor
public enum RssBangumiStatusEnum {

    /**
     * 未激活
     * */
    INACTIVE(1),

    /**
     * 激活状态
     * */
    ACTIVE(2),

    /**
     * 归档
     * */
    ARCHIVED(3);

    /**
     * rss番剧状态
     * */
    private final int status;

    private static final RssBangumiStatusEnum[] ENUMS = values();

    public static boolean contains(Integer status) {
        return EnumUtils.containsInteger(ENUMS, RssBangumiStatusEnum::getStatus, status);
    }

    public static RssBangumiStatusEnum of(Integer status) {
        return EnumUtils.ofInteger(ENUMS, RssBangumiStatusEnum::getStatus, status, null);
    }

    public boolean equals(Integer status) {
        if (Objects.isNull(status)) {
            return false;
        }

        return this.status == status;
    }
}
