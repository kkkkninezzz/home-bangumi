package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @authoer rainine
 * @date 2024/5/19 13:52
 * @desc 消息类型
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {
    INFO(1),

    WARNING(2),

    ERROR(3);

    private final int type;
}
