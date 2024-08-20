package top.rainine.homebangumi.core.message.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.message.MessagePushManager;
import top.rainine.homebangumi.core.message.MessagePusher;
import top.rainine.homebangumi.def.enums.MessagePushMethodEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rainine
 * @description 消息推送管理的实现
 * @date 2024/8/20 17:44:09
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessagePushManagerImpl implements MessagePushManager, InitializingBean {

    private final ApplicationContext applicationContext;

    private final Map<MessagePushMethodEnum, MessagePusher> pusherMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBeansOfType(MessagePusher.class)
                .values()
                .forEach(pusher -> pusherMap.put(pusher.pushMethod(), pusher));
    }

    @Override
    @Async
    public void asyncPushMessage(String message) {
        pusherMap.forEach((method, pusher) -> {
            try {
                pusher.pushMessage(message);
            } catch (Exception e) {
                log.error("[MessagePushManagerImpl]push message failed, push method: {}, message: {}", method, message, e);
            }
        });
    }

}
