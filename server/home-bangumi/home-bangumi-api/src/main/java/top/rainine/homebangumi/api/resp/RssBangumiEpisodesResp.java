package top.rainine.homebangumi.api.resp;

import lombok.*;

import java.util.List;

/**
 * @author rainine
 * @description rss bangumi解析出来的剧集信息
 * @date 2024/4/15 18:36:48
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class RssBangumiEpisodesResp {

    /**
     * 解析出来的剧集信息
     * */
    private List<RssBangumiEpisodeDto> episodes;
}
