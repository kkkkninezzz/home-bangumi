package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author rainine
 * @description 种子下载状态枚举定义
 * @date 2024/3/14 18:02:04
 */
@Getter
@AllArgsConstructor
public enum TorrentDownloadStatusEnum {

    /**
     * 未知
     * */
    NONE(0),

    /**
     * 下载中
     * */
    DOWNLOADING(1),

    /**
     * 失败
     * */
    FAILED(2),

    /**
     * 结束
     * */
    FINISHED(3),

    /**
     * 没有找到该种子
     * */
    NOT_FOUND(98),

    /**
     * 意外错误
     * */
    ERROR(99)
    ;

    private final int status;
}
