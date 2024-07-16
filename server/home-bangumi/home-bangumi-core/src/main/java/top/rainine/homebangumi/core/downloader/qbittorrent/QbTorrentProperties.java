package top.rainine.homebangumi.core.downloader.qbittorrent;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @authoer rainine
 * @date 2024/4/28 22:07
 * @desc 种子信息
 *
 *     "pieces_have": 1059,
 *     "pieces_num": 1059,
 */
@Getter
@Setter
@ToString
public class QbTorrentProperties {
    /**
     * 种子分片文件当前已经下载的数量
     * */
    @SerializedName("pieces_have")
    private Integer piecesHave;

    /**
     * 种子分片文件的总数
     * */
    @SerializedName("pieces_num")
    private Integer piecesNum;
}
