package top.rainine.homebangumi.core.settings.data;

import lombok.Builder;

/**
 * @param host 主机地址
 * @param port 端口号
 * @param username socks5代理的用户名，如果为空，表示无需验证
 * @param password socks5代理的密码
 *
 * @authoer rainine
 * @date 2024/3/15 00:48
 * @desc socks5代理配置
 */
@Builder
public record Socks5ProxySettings(String host, int port, String username, String password) {

}
