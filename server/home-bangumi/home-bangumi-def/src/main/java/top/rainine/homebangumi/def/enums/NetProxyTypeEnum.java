package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.rainine.homebangumi.def.utils.EnumUtils;

/**
 * @authoer rainine
 * @date 2024/3/15 00:49
 * @desc 网络代理类型
 */
@Getter
@AllArgsConstructor
public enum NetProxyTypeEnum {
    /**
     * 无代理
     * */
    NONE(0),

    /**
     * HTTP类型代理，包含https
     * */
    HTTP(1),

    /**
     * SOCKS5类型
     * */
    SOCKS5(2);

    private final int type;

    /**
     * 私有缓存
     * */
    private static final NetProxyTypeEnum[] ENUMS = values();

    public static NetProxyTypeEnum of(Integer type) {
        return EnumUtils.ofInteger(ENUMS, NetProxyTypeEnum::getType, type, NONE);
    }

    public static boolean contains(Integer type) {
        return EnumUtils.containsInt(ENUMS, NetProxyTypeEnum::getType, type);
    }
}
