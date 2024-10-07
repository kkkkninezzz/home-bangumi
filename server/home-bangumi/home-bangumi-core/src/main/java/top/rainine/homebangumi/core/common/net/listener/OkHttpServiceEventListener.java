package top.rainine.homebangumi.core.common.net.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.event.data.NetProxySettingsModifiedEvent;
import top.rainine.homebangumi.core.common.net.OkHttpService;

/**
 * @author rainine
 * @description ok http服务类的事件监听
 * @date 2024/4/24 15:57:34
 */
@Component
@RequiredArgsConstructor
public class OkHttpServiceEventListener {

    private final OkHttpService okHttpService;


    /**
     * 监听网络代理配置更新事件
     * */
    @EventListener(NetProxySettingsModifiedEvent.class)
    @Async
    public void onNetProxySettingsModifiedEvent(NetProxySettingsModifiedEvent event) {
        // 重新初始化代理客户端
        okHttpService.initProxyClient();
    }
}
