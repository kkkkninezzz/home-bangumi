package top.rainine.homebangumi.core.user.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @authoer rainine
 * @date 2024/3/21 23:24
 * @desc 默认账号配置
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "hb.user.default")
public class DefaultAccountProperties {

    /**
     * 账号
     * */
    private String username = "admin";

    /**
     * 密码
     * */
    private String password = "admin123";
}
