package top.rainine.homebangumi.core.common.titleparser.impl;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.common.titleparser.EpisodeTitleParser;
import top.rainine.homebangumi.core.common.titleparser.data.EpisodeTitleInfo;
import top.rainine.homebangumi.core.common.titleparser.exception.EpisodeTitleParseFailedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rainine
 * @description 基于种子名的解析器
 * @date 2024/3/14 12:09:09
 */
@Component
@Slf4j
public class TorrentEpisodeTitleParser implements EpisodeTitleParser {

//    public static void main(String[] args) {
//        TorrentEpisodeTitleParser parser = new TorrentEpisodeTitleParser();
//        EpisodeTitleInfo episodeTitleInfo = parser.parseTitle("[Sakurato] Dungeon Meshi [09][AVC-8bit 1080p AAC][CHT]", 1);
//        System.out.println(episodeTitleInfo);
//    }

    /**
     * 标题规则
     * */
    private static final List<Pattern> TITLE_RULES = Arrays.asList(
            Pattern.compile("(.*) - (\\d{1,4}(?!\\d|p)|\\d{1,4}\\.\\d{1,2}(?!\\d|p))(?:v\\d{1,2})?(?: )?(?:END)?(.*)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(.*)[\\[\\ E](\\d{1,4}|\\d{1,4}\\.\\d{1,2})(?:v\\d{1,2})?(?: )?(?:END)?[\\]\\ ](.*)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(.*)\\[(?:第)?(\\d*\\.*\\d*)[话集話](?:END)?\\](.*)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(.*)第?(\\d*\\.*\\d*)[话話集](?:END)?(.*)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(.*)(?:S\\d{2})?EP?(\\d+)(.*)", Pattern.CASE_INSENSITIVE)
    );

    /**
     * 在季度名上解析标题的正则
     * */
    private static final Pattern TITLE_PATTERN = Pattern.compile("([Ss]|Season )\\d{1,3}");

    /**
     * 在季度名上解析出季度
     * */
    private static final Pattern SEASON_PATTERN = Pattern.compile("([Ss]|Season )(\\d{1,3})", Pattern.CASE_INSENSITIVE);

    @Override
    public EpisodeTitleInfo parseTitle(String torrentName, Integer season) {
        Matcher matcher = null;
        for (Pattern pattern: TITLE_RULES) {
            matcher = pattern.matcher(torrentName);
            if (matcher.matches()) {
                break;
            }
        }

        if (Objects.isNull(matcher)) {
            log.debug("[TorrentEpisodeTitleParser]parse failed, not found matched rule. torrentName: {}", torrentName);
            throw new EpisodeTitleParseFailedException();
        }

        try {
            Pair<String, String> groupAndTitle = getGroupAndTitle(matcher.group(1));
            String group = groupAndTitle.getLeft();
            String title = getTitle(groupAndTitle.getRight());
            if (Objects.isNull(season)) {
                season = getSeason(groupAndTitle.getRight());
            }

            int episode = Integer.parseInt(matcher.group(2));

            return EpisodeTitleInfo
                    .builder()
                    .group(group)
                    .title(title)
                    .season(season)
                    .episode(episode)
                    .build();
        } catch (Exception e) {
            log.debug("[TorrentEpisodeTitleParser]parse failed. torrentName: {}", torrentName, e);
            throw new EpisodeTitleParseFailedException();
        }
    }


    /**
     * 解析出group和title
     * @return 0: group, 1: title
     * */
    private Pair<String, String> getGroupAndTitle(String groupAndTitle) {
        String[] strArr = groupAndTitle.split("[\\[\\]()【】（）]");
        List<String> stringList = new ArrayList<>(strArr.length);
        for (String str: strArr) {
            if (StringUtils.isNotBlank(str)) {
                stringList.add(str);
            }
        }

        if (stringList.isEmpty()) {
            log.error("[TorrentEpisodeTitleParser]parse title failed, groupAndTitle: {}", groupAndTitle);
            throw new EpisodeTitleParseFailedException();
        }

        if (stringList.size() == 1) {
            return Pair.of(null, stringList.getFirst());
        }

        if (Pattern.compile("\\d+").matcher(stringList.getFirst()).matches()) {
            return Pair.of(null, groupAndTitle);
        }

        return Pair.of(stringList.getFirst(), stringList.get(1));
    }

    /**
     * 解析出title
     * @return 最终的title
     * */
    private String getTitle(String titleAndSeason) {
        Matcher m = TITLE_PATTERN.matcher(titleAndSeason);
        return m.replaceAll("").trim();
    }

    /**
     * 解析出季
     * */
    private int getSeason(String titleAndSeason) {
        Matcher m = SEASON_PATTERN.matcher(titleAndSeason);
        if (m.find()) {
            try {
                return Integer.parseInt(m.group(2));
            } catch (EpisodeTitleParseFailedException e) {
                log.debug("[TorrentEpisodeTitleParser]get season failed, titleAndSeason: {}", titleAndSeason);
                return 1;
            }

        } else {
            return 1;
        }
    }
}
