package top.rainine.homebangumi.def.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author rainine
 * @description 剧集任务详情处理状态枚举
 * @date 2024/7/17 10:46:40
 */
@Getter
@RequiredArgsConstructor
public enum EpisodeRenameTaskDetailStatusEnum {

    /**
     * 初始状态，未处理
     * */
    NONE(0),

    /**
     * 解析标题失败
     * */
    PARSE_TITLE_FAILED(1),

    /**
     * 等待执行
     * */
    PENDING(2),

    /**
     * 成功
     * */
    SUCCESS(3),

    /**
     * 失败
     * */
    FAIL(4),

    ;

    private final int status;
}
