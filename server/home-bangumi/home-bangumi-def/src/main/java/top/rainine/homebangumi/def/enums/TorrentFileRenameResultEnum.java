package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @authoer rainine
 * @date 2024/5/1 10:16
 * @desc 剧集文件重命名结果
 */
@Getter
@AllArgsConstructor
public enum TorrentFileRenameResultEnum {
    SUCCESS(1),

    /**
     * 重命名失败
     * */
    FAILED(2),

    /**
     * 发生错误
     * */
    ERROR(3);

    private final int result;

}
