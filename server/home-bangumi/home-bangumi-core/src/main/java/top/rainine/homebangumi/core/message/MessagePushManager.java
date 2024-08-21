package top.rainine.homebangumi.core.message;

/**
 * @author rainine
 * @description 消息推送管理
 * @date 2024/8/20 17:41:28
 */
public interface MessagePushManager {

    /**
     * 异步推送消息
     * */
    void asyncPushMessage(String message);
}
