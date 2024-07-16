package top.rainine.homebangumi.core.downloader.data;

import lombok.Builder;
import top.rainine.homebangumi.def.enums.TorrentDownloadStatusEnum;
import top.rainine.homebangumi.def.enums.TorrentFileRenameResultEnum;

/**
 * @param result   重命名结果
 * @param errorMessage 错误消息，非{@link TorrentFileRenameResultEnum#SUCCESS}状态时，可以设置错误消息

 * @author rainine
 * @description 种子的下载状态信息
 * @date 2024/3/14 18:00:46
 */
@Builder
public record TorrentFileRenameResultInfo(TorrentFileRenameResultEnum result, String errorMessage) {

    public static final TorrentFileRenameResultInfo SUCCESS = TorrentFileRenameResultInfo.builder().result(TorrentFileRenameResultEnum.SUCCESS).build();

    public static final TorrentFileRenameResultInfo DEFAULT_ERROR = TorrentFileRenameResultInfo.builder().result(TorrentFileRenameResultEnum.ERROR).build();
}
