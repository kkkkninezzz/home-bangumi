package top.rainine.homebangumi.core.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import top.rainine.homebangumi.core.rss.configuration.RssBangumiConfigProperties;
import top.rainine.homebangumi.core.user.configuration.DefaultAccountProperties;

/**
 * @authoer rainine
 * @date 2024/3/18 22:59
 * @desc 所有的配置类声明
 */
@Configuration
@EnableConfigurationProperties({
        RssBangumiConfigProperties.class,
        DefaultAccountProperties.class
})
public class PropertiesConfiguration {
}
