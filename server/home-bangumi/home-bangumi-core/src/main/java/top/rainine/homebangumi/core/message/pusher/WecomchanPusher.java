package top.rainine.homebangumi.core.message.pusher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.common.utils.GsonUtils;
import top.rainine.homebangumi.core.message.MessagePusher;
import top.rainine.homebangumi.core.common.net.OkHttpService;
import top.rainine.homebangumi.core.settings.MessagePusherSettingsService;
import top.rainine.homebangumi.core.settings.data.WecomchanSettings;
import top.rainine.homebangumi.def.enums.MessagePushMethodEnum;

import java.io.IOException;
import java.util.Objects;

/**
 * @author rainine
 * @description wecomchan消息推送
 * @date 2024/8/20 18:23:06
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WecomchanPusher implements MessagePusher, InitializingBean {

    private final OkHttpService okHttpService;

    private final MessagePusherSettingsService messagePusherSettingsService;

    private volatile WecomchanSettings wecomchanSettings;

    @Override
    public void afterPropertiesSet() throws Exception {
        setSettings();
    }

    public void setSettings() {
        this.wecomchanSettings = messagePusherSettingsService.getWecomchanSettings();
    }

    @Override
    public MessagePushMethodEnum pushMethod() {
        return MessagePushMethodEnum.WECOMCHAN;
    }

    @Override
    public void pushMessage(String message) {
        if (!wecomchanSettings.enable()) {
            return;
        }

        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(wecomchanSettings.url()))
                .newBuilder()
                .addQueryParameter("sendkey", wecomchanSettings.sendKey())
                .addQueryParameter("msg", message)
                .addQueryParameter("msg_type", "text")
                .build();
        // 构造请求
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            boolean result = okHttpService.sendRequest(request, response -> {
                if (response.isSuccessful() && Objects.nonNull(response.body())) {
                    WecomchanPushResponse pushResponse = GsonUtils.toJson(response.body().string(), WecomchanPushResponse.class);
                    return Objects.equals(pushResponse.errcode, 0);
                }

                if (Objects.nonNull(response.body())) {
                    log.warn("[WecomchanPusher]push message failed, responseBody: {}", response.body().string());
                } else {
                    log.warn("[WecomchanPusher]push message failed, responseCode: {}", response.code());
                }

                return false;
            });
            log.info("[WecomchanPusher]push message end, result: {}", result);
        } catch (IOException e) {
            log.error("[WecomchanPusher]push message failed, message: {}", message, e);
        }
    }

    @Getter
    @Setter
    @ToString
    public static class WecomchanPushResponse {
        private Integer errcode;

        private String errmsg;

        private String msgid;
    }
}
