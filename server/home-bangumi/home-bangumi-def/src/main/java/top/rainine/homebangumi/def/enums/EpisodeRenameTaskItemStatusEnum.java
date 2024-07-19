package top.rainine.homebangumi.def.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * @author rainine
 * @description 剧集任务详情处理状态枚举
 * @date 2024/7/17 10:46:40
 */
@Getter
@RequiredArgsConstructor
public enum EpisodeRenameTaskItemStatusEnum {

    /**
     * 初始状态，未处理
     * */
    NONE(0),

    /**
     * 解析完成
     * */
    PARSED(1),

    /**
     * 等待执行
     * */
    PENDING(2),

    /**
     * 成功
     * */
    SUCCESS(3),

    /**
     * 解析标题失败
     * */
    TITLE_PARSE_FAILED(96),

    /**
     * 忽略
     * */
    IGNORED(97),

    /**
     * 被过滤掉的
     * */
    FILTERED_OUT(98),

    /**
     * 失败
     * */
    FAIL(99),
    ;

    private final int status;

    public boolean equals(Integer status) {
        if (Objects.isNull(status)) {
            return false;
        }
        return this.status == status;
    }
}
