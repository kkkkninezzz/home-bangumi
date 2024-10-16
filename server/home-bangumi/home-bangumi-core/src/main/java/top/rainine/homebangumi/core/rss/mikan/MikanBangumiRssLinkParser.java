package top.rainine.homebangumi.core.rss.mikan;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.common.utils.HbDateUtils;
import top.rainine.homebangumi.core.common.net.OkHttpService;
import top.rainine.homebangumi.core.rss.BangumiRssLinkParser;
import top.rainine.homebangumi.core.rss.data.RssBangumiEpisodeParsedInfo;
import top.rainine.homebangumi.core.rss.data.RssBangumiParsedInfo;
import top.rainine.homebangumi.core.rss.exception.RssBangumiParseFailedException;
import top.rainine.homebangumi.core.rss.mikan.xml.MIkanRssChannelItemEnclosureDto;
import top.rainine.homebangumi.core.rss.mikan.xml.MikanRssXmlChannelItemDto;
import top.rainine.homebangumi.core.rss.mikan.xml.MikanRssXmlDto;
import top.rainine.homebangumi.def.enums.RssCategoryEnum;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @authoer rainine
 * @date 2024/3/17 14:04
 * @desc
 */
@Component
@Slf4j
public class MikanBangumiRssLinkParser implements BangumiRssLinkParser, InitializingBean {

    private final OkHttpService okHttpService;

    private XStream xStream;

    /**
     * 种子发布日期的时间格式
     * */
//    private static final DateTimeFormatter TORRENT_PUB_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final DateTimeFormatter TORRENT_PUB_DATE_FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    public MikanBangumiRssLinkParser(OkHttpService okHttpService) {
        this.okHttpService = okHttpService;
    }

    @Override
    public RssCategoryEnum getCategory() {
        return RssCategoryEnum.MIKAN;
    }

    @Override
    public void afterPropertiesSet() {
        initXstream();
    }

    private void initXstream() {
        this.xStream = new XStream(new Dom4JDriver());
        this.xStream.setClassLoader(this.getClass().getClassLoader());
        this.xStream.autodetectAnnotations(true);
        this.xStream.processAnnotations(MikanRssXmlDto.class);
        this.xStream.allowTypesByWildcard(new String[] {
                "top.rainine.homebangumi.core.rss.mikan.xml.**"
        });
    }

    @Override
    public List<RssBangumiParsedInfo> parse(String rssLink) {
        String rssContent = requestRssContent(rssLink);

        MikanRssXmlDto dto;
        try {
             dto = (MikanRssXmlDto) this.xStream.fromXML(rssContent);
        } catch (Exception e) {
            log.info("[MikanRssBangumiParser] format xml failed, rssLink: {}", rssLink, e);
            throw new RssBangumiParseFailedException();
        }

        return convertToRssBangumiParsedInfo(dto);
    }

    /**
     * 获取rss的内容
     * */
    private String requestRssContent(String rssLink) {
        Request request = new Request
                .Builder()
                .url(rssLink)
                .get()
                .build();
        try {
            String rssContent = okHttpService.sendRequestByProxy(request, response -> {
                ResponseBody body = response.body();
                if (Objects.isNull(body)) {
                    return null;
                }

                return body.string();
            });

            if (StringUtils.isEmpty(rssContent)) {
                log.info("[MikanRssBangumiParser] request mikan rss failed, rss content is empty, rssLink: {}", rssLink);
                throw new RssBangumiParseFailedException();
            }

            return rssContent;
        } catch (IOException e) {
            log.info("[MikanRssBangumiParser] request mikan rss failed, rssLink: {}", rssLink, e);
            throw new RssBangumiParseFailedException();
        }
    }

    /**
     * 转换为解析信息
     * */
    private List<RssBangumiParsedInfo> convertToRssBangumiParsedInfo(MikanRssXmlDto xmlDto) {
        return xmlDto.getChannels()
                .stream()
                .map(channel -> {

                    List<RssBangumiEpisodeParsedInfo> episodes = channel
                            .getItems()
                            .stream()
                            .map(this::convertToEpisode)
                            .toList();

                    return RssBangumiParsedInfo
                            .builder()
                            .rssCategory(getCategory())
                            .title(formatTitle(channel.getTitle()))
                            .episodes(episodes)
                            .build();
                }).toList();

    }

    /**
     * 格式化标题
     * */
    private String formatTitle(String title) {
        return title.replace("Mikan Project - ", "");
    }

    /**
     * 转换为剧集
     * */
    private RssBangumiEpisodeParsedInfo convertToEpisode(MikanRssXmlChannelItemDto item) {
        long timestamp = 0L;
        if (Objects.nonNull(item.getTorrent())) {
            String pubDate = item.getTorrent().getPubDate();
            if (StringUtils.isNotBlank(pubDate)) {
                try {
                    // 查找小数点的位置
                    int dotIndex = pubDate.indexOf('.');

                    // 检查是否找到了小数点
                    if (dotIndex >= 0) {
                        // 截取小数点前的字符串
                        pubDate = pubDate.substring(0, dotIndex);
                    }
                    LocalDateTime localDateTime = LocalDateTime.parse(pubDate, TORRENT_PUB_DATE_FORMATTER2);
                    // 转换为Long型时间戳
                    timestamp = HbDateUtils.toMills(localDateTime);
                } catch (Exception e) {
                    log.debug("[MikanRssBangumiParser] parse torrent pub date failed, pubDateStr: {}",
                            pubDate, e);
                }
            }

        }

        String torrentLink = Optional.ofNullable(item.getEnclosure()).map(MIkanRssChannelItemEnclosureDto::getUrl).orElse("");
        return RssBangumiEpisodeParsedInfo
                .builder()
                .rawEpisodeTitle(item.getTitle())
                .torrentPubDate(timestamp)
                .torrentLink(torrentLink)
                .build();
    }

}
























