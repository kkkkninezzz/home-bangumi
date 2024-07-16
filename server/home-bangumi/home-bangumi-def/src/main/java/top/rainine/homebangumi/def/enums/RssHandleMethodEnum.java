package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.rainine.homebangumi.def.utils.EnumUtils;

/**
 * @authoer rainine
 * @date 2024/3/12 21:45
 * @desc rss的处理方式
 */
@Getter
@AllArgsConstructor
public enum RssHandleMethodEnum {

    NONE(0),

    /**
     * 收集，指会对当前rss链接做一次性下载全部的操作
     * */
    COLLECT(1),

    /**
     * 订阅，指会对当前rss链接进行定时拉取数据，判断是否有新的番剧信息需要处理
     * */
    SUBSCRIBE(2)
    ;

    /**
     * 处理方式
     * */
    private final int method;

    private static final RssHandleMethodEnum[] ENUMS = values();

    public static boolean contains(Integer method) {
        return EnumUtils.containsInteger(ENUMS, RssHandleMethodEnum::getMethod, method);
    }
}
