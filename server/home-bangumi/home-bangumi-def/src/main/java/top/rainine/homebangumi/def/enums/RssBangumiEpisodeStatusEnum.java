package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.rainine.homebangumi.def.utils.EnumUtils;

import java.util.Objects;

/**
 * @author rainine
 * @description rss番剧剧集状态枚举
 * @date 2024/3/13 17:37:23
 */
@Getter
@AllArgsConstructor
public enum RssBangumiEpisodeStatusEnum {
    NONE(0),

    /**
     * 已解析
     * */
    PARSED(1),

    /**
     * 标题解析失败，需要人工参与
     * */
    TITLE_PARSE_FAILED(2),

    /**
     * 种子下载中
     * */
    TORRENT_DOWNLOADING(21),

    /**
     * 种子下载失败
     * */
    TORRENT_DOWNLOAD_FAILED(22),

    /**
     * 种子下载成功
     * */
    TORRENT_DOWNLOAD_FINISHED(23),

    /**
     * 种子解析失败
     * */
    TORRENT_PARSE_FAILED(24),

    /**
     * 种子写入到本地失败
     * */
    TORRENT_STORED_FAILED(25),

    /**
     * 剧集下载中
     * */
    EPISODE_DOWNLOADING(31),

    /**
     * 剧集下载失败
     * */
    EPISODE_DOWNLOAD_FAILED(32),

    /**
     * 剧集下载完成
     * */
    EPISODE_DOWNLOAD_FINISHED(33),

    /**
     * 重命名中
     * */
    RENAMING(41),

    /**
     * 重命名失败
     * */
    RENAME_FAILED(42),

    /**
     * 重命名结束
     * */
    RENAME_FINISHED(43),

    /**
     * 全部完成
     * */
    FINISHED(51),

    /**
     * 归档
     * */
    ARCHIVED(95),

    /**
     * 被忽略的
     * */
    IGNORED(96),

    /**
     * 跳过
     * */
    SKIPPED(97),

    /**
     * 被过滤掉的
     * */
    FILTERED_OUT(98),

    /**
     * 未知错误
     * */
    ERROR(99),
    ;

    private final int status;

    private static final RssBangumiEpisodeStatusEnum[] ENUMS = values();

    public boolean equals(Integer status) {
        if (Objects.isNull(status)) {
            return false;
        }
        return this.status == status;
    }

    public static RssBangumiEpisodeStatusEnum of(Integer status) {
        return EnumUtils.of(ENUMS, RssBangumiEpisodeStatusEnum::getStatus, status, NONE);
    }
}
