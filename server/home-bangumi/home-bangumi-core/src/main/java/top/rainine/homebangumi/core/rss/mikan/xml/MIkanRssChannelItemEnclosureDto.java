package top.rainine.homebangumi.core.rss.mikan.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @authoer rainine
 * @date 2024/3/17 15:22
 * @desc
 */
@Getter
@Setter
@ToString
@XStreamAlias("enclosure")
public class MIkanRssChannelItemEnclosureDto {

    @XStreamAsAttribute
    private String url;
}
