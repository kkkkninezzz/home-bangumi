package top.rainine.homebangumi.core.rss.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @authoer rainine
 * @date 2024/3/18 22:58
 * @desc 番剧相关配置信息
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "hb.rss.bangumi")
public class RssBangumiConfigProperties {

    /**
     * 静态资源的父路径
     * */
    private String staticResourceParentDir;

    /**
     * 种子下载后的目录
     * */
    private String torrentRootDir;

    /**
     * 番剧海报下载后的目录
     * */
    private String posterRootDir;

}
