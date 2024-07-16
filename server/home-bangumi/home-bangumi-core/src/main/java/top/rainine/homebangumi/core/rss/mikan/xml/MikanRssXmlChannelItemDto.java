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
@XStreamAlias("item")
public class MikanRssXmlChannelItemDto {

    private String guid;

    private String link;

    private String title;

    private String description;

    private MikanRssChannelItemTorrentDto torrent;

    private MIkanRssChannelItemEnclosureDto enclosure;
}
