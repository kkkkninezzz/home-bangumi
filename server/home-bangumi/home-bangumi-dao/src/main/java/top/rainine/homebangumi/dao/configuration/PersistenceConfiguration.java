package top.rainine.homebangumi.dao.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author rainine
 * @description 持久化相关配置
 * @date 2024/3/14 15:54:02
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "top.rainine.homebangumi.dao.repository")
@EnableJpaAuditing
@EntityScan("top.rainine.homebangumi.dao.po")
public class PersistenceConfiguration {
}
