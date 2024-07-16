package top.rainine.homebangumi.core.rss.mikan.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @authoer rainine
 * @date 2024/3/17 14:30
 * @desc
 */
@Getter
@Setter
@ToString
@XStreamAlias("torrent")
public class MikanRssChannelItemTorrentDto {


    private String link;

    private Long contentLength;

    private String pubDate;

}
