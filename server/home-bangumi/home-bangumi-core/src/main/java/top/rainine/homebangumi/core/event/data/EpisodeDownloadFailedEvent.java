package top.rainine.homebangumi.core.event.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import top.rainine.homebangumi.dao.po.HbRssBangumiEpisode;

/**
 * @author rainine
 * @description 番剧下载失败事件
 * @date 2024/4/29 18:12:29
 */
@Getter
@ToString
@AllArgsConstructor
public class EpisodeDownloadFailedEvent extends HbEvent {

    /**
     * 剧集id
     * @see HbRssBangumiEpisode#getId()
     * */
    private final Long episodeId;
}
