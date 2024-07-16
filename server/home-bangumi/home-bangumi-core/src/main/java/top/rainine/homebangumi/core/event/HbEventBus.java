package top.rainine.homebangumi.core.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.event.data.HbEvent;

/**
 * @authoer rainine
 * @date 2024/4/24 22:54
 * @desc 事件总线
 */
@Service
@Component
@RequiredArgsConstructor
public class HbEventBus {

    private final ApplicationContext applicationContext;


    /**
     * 发布事件
     * */
    public void publishEvent(HbEvent hbEvent) {
        applicationContext.publishEvent(hbEvent);
    }
}
