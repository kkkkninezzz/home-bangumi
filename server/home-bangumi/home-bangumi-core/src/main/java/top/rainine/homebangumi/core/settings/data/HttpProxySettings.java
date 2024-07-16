package top.rainine.homebangumi.core.settings.data;

import lombok.Builder;

/**
 * @param host 主机地址
 * @param port 端口号
 *
 * @authoer rainine
 * @date 2024/3/15 00:48
 * @desc http代理配置
 */
@Builder
public record HttpProxySettings(String host, int port) {

}
