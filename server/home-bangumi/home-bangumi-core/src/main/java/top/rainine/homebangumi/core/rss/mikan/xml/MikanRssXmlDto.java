package top.rainine.homebangumi.core.rss.mikan.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @authoer rainine
 * @date 2024/3/17 14:30
 * @desc
 */
@Getter
@Setter
@ToString
@XStreamAlias("rss")
public class MikanRssXmlDto {

    @XStreamImplicit
    private List<MikanRssXmlChannelDto> channels = new ArrayList<>();
}
