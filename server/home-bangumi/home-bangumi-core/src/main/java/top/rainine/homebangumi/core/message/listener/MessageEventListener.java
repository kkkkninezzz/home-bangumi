package top.rainine.homebangumi.core.message.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.event.data.WecomchanConfigModifiedEvent;
import top.rainine.homebangumi.core.message.pusher.WecomchanPusher;

/**
 * @author rainine
 * @description 消息监听器
 * @date 2024/8/20 18:21:30
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MessageEventListener {

    private final WecomchanPusher wecomchanPusher;

    @EventListener(WecomchanConfigModifiedEvent.class)
    @Async
    public void onWecomchanConfigModifiedEvent(WecomchanConfigModifiedEvent event) {
        wecomchanPusher.setSettings();
    }

}
