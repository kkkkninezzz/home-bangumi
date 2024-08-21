package top.rainine.homebangumi.def.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author rainine
 * @description 系统配置key枚举定义
 * @date 2024/3/14 11:35:23
 */
@Getter
public enum SystemSettingKeyEnum {
    /**
     * 无效key
     * */
    NONE("", Object.class),

    //region 代理配置
    /**
     * 代理是否启用
     * */
    NET_PROXY_ENABLED("NET_PROXY_ENABLED", Boolean.class, Boolean.FALSE),

    /**
     * 网络代理的类型
     * @see NetProxyTypeEnum
     * */
    NET_PROXY_TYPE("NET_PROXY_TYPE", Integer.class, NetProxyTypeEnum.NONE.getType()),

    //region http代理配置
    /**
     * http网络代理的host
     * */
    HTTP_NET_PROXY_HOST("HTTP_NET_PROXY_HOST", String.class, ""),

    /**
     * http网络代理的port
     * */
    HTTP_NET_PROXY_PORT("HTTP_NET_PROXY_PORT", Integer.class, 0),
    //endregion

    //region socks5代理配置
    /**
     * socks5网络代理的host
     * */
    SOCKS5_NET_PROXY_HOST("SOCKS5_NET_PROXY_HOST", String.class, ""),

    /**
     * socks5网络代理的port
     * */
    SOCKS5_NET_PROXY_PORT("SOCKS5_NET_PROXY_PORT", Integer.class, 0),

    /**
     * socks5网络代理的用户名
     * */
    SOCKS5_NET_PROXY_USERNAME("SOCKS5_NET_PROXY_USERNAME", String.class, ""),

    /**
     * socks5网络代理的密码
     * */
    SOCKS5_NET_PROXY_PASSWORD("SOCKS5_NET_PROXY_PASSWORD", String.class, ""),
    //endregion

    //endregion

    //region 下载器配置
    //region qb下载器配置
    /**
     * qb的基础url
     * */
    QBITTORRENT_BASE_URL("QBITTORRENT_BASE_URL", String.class, ""),

    /**
     * qb的用户名
     * */
    QBITTORRENT_USERNAME("QBITTORRENT_USERNAME", String.class, ""),

    /**
     * qb的密码
     * */
    QBITTORRENT_PASSWORD("QBITTORRENT_PASSWORD", String.class, ""),

    /**
     * qb的基础url
     * */
    QBITTORRENT_DOWNLOAD_DIR("QBITTORRENT_DOWNLOAD_DIR", String.class, "/downloads/home-bangumi"),

    //endregion

    //endregion

    //region 剧集过滤规则配置
    EPISODE_FILTER_RULES("EPISODE_FILTER_RULES", String.class, ""),
    //endregion

    //region 定时任务的配置
    /**
     * 检查番剧下载状态的定时任务周期，单位分钟，默认半小时执行一次
     * */
    CHECK_EPISODE_DOWNLOAD_STATUS_DURATION("CHECK_EPISODE_DOWNLOAD_STATUS_DURATION", Integer.class, 30),

    /**
     * 推送已经解析好的番剧到下载器的定时任务周期，单位分钟，默认每小时执行一次
     * */
    PUSH_PARSED_EPISODES_TO_DOWNLOADER_DURATION("PUSH_PARSED_EPISODES_TO_DOWNLOADER_DURATION", Integer.class, 60),

    /**
     * 重命名剧集的定时任务周期，单位分钟，默认2小时执行一次
     * */
    RENAME_EPISODES_DURATION("RENAME_EPISODES_DURATION", Integer.class, 120),

    /**
     * 定时更新rss bangumi的定时任务周期，单位分钟，默认2小时执行一次
     * */
    UPDATE_RSS_SUBSCRIPTION_DURATION("UPDATE_RSS_SUBSCRIPTION_DURATION", Integer.class, 120),

    /**
     * 检查未完成的重命名任务的定时任务周期，单位分钟，默认2小时执行一次
     * */
    CHECK_NOT_FINISHED_RENAME_TASK_DURATION("CHECK_NOT_FINISHED_RENAME_TASK_DURATION", Integer.class, 120),
    //endregion


    //region wecomchan配置
    /**
     * wecomchan是否可用
     * */
    MESSAGE_PUSHER_WECOMCHAN_ENABLE("MESSAGE_PUSHER_WECOMCHAN_ENABLE", Boolean.class, Boolean.FALSE),

    /**
     * wecomchan的url
     * */
    MESSAGE_PUSHER_WECOMCHAN_URL("MESSAGE_PUSHER_WECOMCHAN_URL", String.class, ""),

    /**
     * wecomchan的sendKey
     * */
    MESSAGE_PUSHER_WECOMCHAN_SEND_KEY("MESSAGE_PUSHER_WECOMCHAN_SEND_KEY", String.class, ""),
    //endregion

    // TODO 定义配置key
    ;

    private final String key;

    /**
     * 值的类型
     * 只支持 {@link Integer}、{@link Long}、{@link Double}、{@link Float}、{@link String}
     * */
    private final Class<?> valueType;

    /**
     * 默认值
     * */
    private final Object defaultValue;

    SystemSettingKeyEnum(String key, Class<?> valueType) {
        this(key, valueType, null);
    }

    SystemSettingKeyEnum(String key, Class<?> valueType, Object defaultValue) {
        this.key = key;
        this.valueType = valueType;
        this.defaultValue = defaultValue;
    }

    private static final SystemSettingKeyEnum[] ENUMS = values();

    private static final Map<String, SystemSettingKeyEnum> MAP = Arrays.stream(ENUMS).collect(Collectors.toMap(SystemSettingKeyEnum::getKey, e -> e));

    /**
     * 获取配置key
     * */
    public static SystemSettingKeyEnum of(String key) {
        SystemSettingKeyEnum systemSettingKeyEnum = MAP.get(key);
        if (Objects.isNull(systemSettingKeyEnum)) {
            return NONE;
        }

        return systemSettingKeyEnum;
    }
}
