package top.rainine.homebangumi.core.runtimehints;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import top.rainine.homebangumi.core.rss.mikan.xml.MikanRssXmlDto;

/**
 * @authoer rainine
 * @date 2024/7/11 20:47
 * @desc mikan xml结构对象的提示
 */
@ImportRuntimeHints({MikanXmlRuntimeHints.MikanXmlObjectRegistrar.class})
@Configuration
@Slf4j
public class MikanXmlRuntimeHints implements InitializingBean {

    @Value("${hb.runtime-hints: false}")
    private boolean runtimeHints;

    private final static String TEST_XML = "<?xml\n" +
            "version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<rss version=\"2.0\">\n" +
            "    <channel>\n" +
            "        <title>Mikan Project - 我要【招架】一切〜反误解的世界最强想成为冒险者〜</title>\n" +
            "        <link>http://mikanime.tv/RSS/Bangumi?bangumiId=3368 &amp;subgroupid=583</link>\n" +
            "        <description>Mikan Project - 我要【招架】一切〜反误解的世界最强想成为冒险者〜</description>\n" +
            "        <item>\n" +
            "            <guid isPermaLink=\"false\">[ANi] I Parry Everything /  我要【招架】一切～反误解的世界最强想成为冒险家～ - 02 [1080P][Baha][WEB-DL][AAC AVC][CHT][MP4]</guid>\n" +
            "            <link>https://mikanime.tv/Home/Episode/9743d65f65a659f301dcbbb29369968e4bd05f36</link>\n" +
            "            <title>[ANi] I Parry Everything /  我要【招架】一切～反误解的世界最强想成为冒险家～ - 02 [1080P][Baha][WEB-DL][AAC AVC][CHT][MP4]</title>\n" +
            "            <description>[ANi] I Parry Everything /  我要【招架】一切～反误解的世界最强想成为冒险家～ - 02 [1080P][Baha][WEB-DL][AAC AVC][CHT][MP4][303.15 MB]</description>\n" +
            "            <torrent xmlns=\"https://mikanime.tv/0.1/\">\n" +
            "                <link>https://mikanime.tv/Home/Episode/9743d65f65a659f301dcbbb29369968e4bd05f36</link>\n" +
            "                <contentLength>317875808</contentLength>\n" +
            "                <pubDate>2024-07-11T23:01:33.937</pubDate>\n" +
            "            </torrent>\n" +
            "            <enclosure type=\"application/x-bittorrent\" length=\"317875808\" url=\"https://mikanime.tv/Download/20240711/9743d65f65a659f301dcbbb29369968e4bd05f36.torrent\"/>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <guid isPermaLink=\"false\">[ANi] I Parry Everything /  我要【招架】一切～反误解的世界最强想成为冒险家～ - 01 [1080P][Baha][WEB-DL][AAC AVC][CHT][MP4]</guid>\n" +
            "            <link>https://mikanime.tv/Home/Episode/6320e951231dab3d3e371f018491b98ee1f5b2df</link>\n" +
            "            <title>[ANi] I Parry Everything /  我要【招架】一切～反误解的世界最强想成为冒险家～ - 01 [1080P][Baha][WEB-DL][AAC AVC][CHT][MP4]</title>\n" +
            "            <description>[ANi] I Parry Everything /  我要【招架】一切～反误解的世界最强想成为冒险家～ - 01 [1080P][Baha][WEB-DL][AAC AVC][CHT][MP4][386.68 MB]</description>\n" +
            "            <torrent xmlns=\"https://mikanime.tv/0.1/\">\n" +
            "                <link>https://mikanime.tv/Home/Episode/6320e951231dab3d3e371f018491b98ee1f5b2df</link>\n" +
            "                <contentLength>405463360</contentLength>\n" +
            "                <pubDate>2024-07-04T23:07:27.933</pubDate>\n" +
            "            </torrent>\n" +
            "            <enclosure type=\"application/x-bittorrent\" length=\"405463360\" url=\"https://mikanime.tv/Download/20240704/6320e951231dab3d3e371f018491b98ee1f5b2df.torrent\"/>\n" +
            "        </item>\n" +
            "    </channel>\n" +
            "</rss>\n";

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!runtimeHints) {
            return;
        }

        log.info("[MikanXmlRuntimeHints] start");
        XStream xStream = new XStream(new Dom4JDriver());
        xStream.setClassLoader(this.getClass().getClassLoader());
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(MikanRssXmlDto.class);
        xStream.allowTypesByWildcard(new String[] {
                "top.rainine.homebangumi.core.rss.mikan.xml.**"
        });

        MikanRssXmlDto dto;
        try {
            dto = (MikanRssXmlDto) xStream.fromXML(TEST_XML);
        } catch (Exception e) {
            log.info("[MikanXmlRuntimeHints] format xml failed", e);
            System.exit(-1);
        }
    }

    static class MikanXmlObjectRegistrar implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(MikanRssXmlDto.class, MemberCategory.values());
        }
    }
}
