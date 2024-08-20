package top.rainine.homebangumi.core.settings.data;

import lombok.Builder;

/**
 * @param enable 是否可用
 * @param url wecomchan的地址
 * @param sendKey sendKey
 *
 * @author rainine
 * @description wecomchan的设置
 * @date 2024/8/20 17:56:24
 */
@Builder
public record WecomchanSettings(boolean enable, String url, String sendKey) {
}
