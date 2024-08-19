package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @authoer rainine
 * @date 2024/5/19 13:42
 * @desc 消息分类枚举
 */
@Getter
@AllArgsConstructor
public enum MessageCategoryEnum {
    /**
     * rss bangumi 剧集
     * */
    RSS_BANGUMI_EPISODE(1),

    /**
     * 剧集重命名任务
     * */
    EPISODE_RENAME_TASK(2),
    ;

    private final int category;
}
