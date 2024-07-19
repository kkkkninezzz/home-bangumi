package top.rainine.homebangumi.def.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rainine.homebangumi.def.utils.EnumUtils;

import java.util.Objects;

/**
 * @author rainine
 * @description 剧集任务处理状态枚举
 * @date 2024/7/17 10:46:40
 */
@Getter
@RequiredArgsConstructor
public enum EpisodeRenameTaskStatusEnum {

    /**
     * 初始状态，未处理
     * */
    NONE(0),

    /**
     * 等待处理
     * */
    PENDING(1),

    /**
     * 处理中
     * */
    PROCESSING(2),

    /**
     * 处理结束
     * */
    FINISHED(3),
    ;

    private final int status;

    private static final EpisodeRenameTaskStatusEnum[] ENUMS = values();

    public static boolean contains(Integer status) {
        return EnumUtils.containsInteger(ENUMS, EpisodeRenameTaskStatusEnum::getStatus, status);
    }

    public boolean equals(Integer status) {
        if (Objects.isNull(status)) {
            return false;
        }
        return this.status == status;
    }
}
