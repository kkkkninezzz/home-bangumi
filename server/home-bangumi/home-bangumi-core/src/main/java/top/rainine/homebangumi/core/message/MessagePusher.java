package top.rainine.homebangumi.core.message;

import top.rainine.homebangumi.def.enums.MessagePushMethodEnum;

/**
 * @author rainine
 * @description 消息推送器
 * @date 2024/8/20 17:45:00
 */

public interface MessagePusher {

    /**
     * 推送方式
     * */
    MessagePushMethodEnum pushMethod();

    /**
     * 推送消息
     * */
    void pushMessage(String message);
}
