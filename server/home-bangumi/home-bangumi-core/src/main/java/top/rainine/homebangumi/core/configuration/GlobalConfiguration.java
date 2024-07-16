package top.rainine.homebangumi.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author rainine
 * @description 全局的一些配置
 * @date 2024/4/24 15:49:47
 */
@Configuration
@EnableAsync
@EnableScheduling
public class GlobalConfiguration {
}
