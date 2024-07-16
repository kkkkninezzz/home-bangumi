package top.rainine.homebangumi.core.downloader.data;

import lombok.*;
import top.rainine.homebangumi.def.enums.TorrentDownloadStatusEnum;

/**
 * @param status   下载状态
 * @param progress 下载进度
 *                 如果处于{@link TorrentDownloadStatusEnum#NONE}状态时，该字段为null
 * @author rainine
 * @description 种子的下载状态信息
 * @date 2024/3/14 18:00:46
 */
@Builder
public record TorrentDownloadStatusInfo(TorrentDownloadStatusEnum status, Float progress) {

    public static final TorrentDownloadStatusInfo NOT_FOUND = TorrentDownloadStatusInfo
            .builder()
            .status(TorrentDownloadStatusEnum.NOT_FOUND)
            .build();

    public static final TorrentDownloadStatusInfo DEFAULT_ERROR = TorrentDownloadStatusInfo
            .builder()
            .status(TorrentDownloadStatusEnum.ERROR)
            .build();
}
