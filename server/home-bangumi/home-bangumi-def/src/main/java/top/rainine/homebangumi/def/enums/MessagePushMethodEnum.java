package top.rainine.homebangumi.def.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rainine.homebangumi.def.utils.EnumUtils;

/**
 * @author rainine
 * @description 消息推送的方式
 * @date 2024/8/20 17:45:37
 */
@Getter
@RequiredArgsConstructor
public enum MessagePushMethodEnum {
    /**
     * 通过企业微信推送到个人微信
     * @link <a href="https://github.com/easychen/wecomchan">easychen/wecomchan</a>
     * */
    WECOMCHAN(1),
    ;

    private final Integer method;

    private static final MessagePushMethodEnum[] ENUMS = values();

    public static boolean contains(Integer method) {
        return EnumUtils.containsInteger(ENUMS, MessagePushMethodEnum::getMethod, method);
    }

    public static MessagePushMethodEnum of(Integer method) {
        return EnumUtils.ofInteger(ENUMS, MessagePushMethodEnum::getMethod, method, null);
    }
}
