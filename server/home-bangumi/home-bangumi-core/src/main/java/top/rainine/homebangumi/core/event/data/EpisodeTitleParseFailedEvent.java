package top.rainine.homebangumi.core.event.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import top.rainine.homebangumi.dao.po.HbRssBangumiEpisode;

/**
 * @authoer rainine
 * @date 2024/5/19 14:24
 * @desc
 */
@Getter
@ToString
@AllArgsConstructor
public class EpisodeTitleParseFailedEvent extends HbEvent {

    /**
     * 剧集id
     * @see HbRssBangumiEpisode#getId()
     * */
    private final Long episodeId;
}
