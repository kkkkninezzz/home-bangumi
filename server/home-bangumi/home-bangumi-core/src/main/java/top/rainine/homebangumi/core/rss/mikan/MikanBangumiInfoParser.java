package top.rainine.homebangumi.core.rss.mikan;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.common.net.OkHttpService;
import top.rainine.homebangumi.core.rss.BangumiInfoParser;
import top.rainine.homebangumi.core.rss.data.BangumiParedInfo;
import top.rainine.homebangumi.def.enums.RssCategoryEnum;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rainine
 * @description mikan的番剧信息解析
 * @date 2024/3/20 10:57:06
 */
@Component
@Slf4j
public class MikanBangumiInfoParser implements BangumiInfoParser {
    private static final BangumiParedInfo DEFAULT_INFO = BangumiParedInfo
            .builder()
            .title("unknown")
            .posterStoredPath("")
            .broadcastDayOfWeek(null)
            .broadcastDate(null)
            .season(null)
            .build();

    private static final Pattern POSTER_URL_PATTERN = Pattern.compile("url\\('([^']+)");
    private static final Pattern POSTER_NAME_PATTERN = Pattern.compile("/([^/]+)\\?");

    private static final DateTimeFormatter BROADCAST_DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    private final OkHttpService okHttpService;

    @Autowired
    public MikanBangumiInfoParser(OkHttpService okHttpService) {
        this.okHttpService = okHttpService;
    }

    @Override
    public RssCategoryEnum getCategory() {
        return RssCategoryEnum.MIKAN;
    }

    @Override
    public BangumiParedInfo parse(String rssLink, Path posterStoredDirPath) {
        Pair<String, String> domainAndBangumiId = parseRssLink(rssLink);
        if (Objects.isNull(domainAndBangumiId)) {
            return DEFAULT_INFO;
        }

        String domain = domainAndBangumiId.getLeft();
        String bangumiId = domainAndBangumiId.getRight();
        String bangumiHomeUrl = STR."https://\{domain}/Home/Bangumi/\{bangumiId}";

        String homeHtml = getHomeHtml(bangumiHomeUrl);
        if (StringUtils.isEmpty(homeHtml)) {
            return DEFAULT_INFO;
        }

        return parseHtml(domain, homeHtml, posterStoredDirPath);
    }

    private Pair<String, String> parseRssLink(String rssLink) {
        try {
            URL url = URI.create(rssLink).toURL();

            // 获取域名
            String domain = url.getHost();

            String bangumiId = "";
            // 获取查询参数
            String query = url.getQuery();
            if (query != null) {
                Map<String, String> queryParams = parseQuery(query);
                bangumiId = queryParams.get("bangumiId");
            }

            if (StringUtils.isEmpty(bangumiId)) {
                return null;
            }

            return Pair.of(domain, bangumiId);
        } catch (Exception e) {
            log.debug("[MikanBangumiInfoParser]get home url failed, rssLink: {}", rssLink, e);
            return null;
        }
    }


    public static Map<String, String> parseQuery(String query) {
        Map<String, String> queryParams = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                queryParams.put(key, value);
            } catch (Exception e) {
                // 忽略解析错误
            }
        }
        return queryParams;
    }

    private String getHomeHtml(String bangumiHomeUrl) {
        Request request = new Request
                .Builder()
                .url(bangumiHomeUrl)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                .get()
                .build();

        try {
            return okHttpService.sendRequestByProxy(request, response -> {
                if (response.code() != 200) {
                    return "";
                }

                ResponseBody body = response.body();
                if (Objects.isNull(body)) {
                    return "";
                }
                return body.string();
            });
        } catch (IOException e) {
            log.debug("[MikanBangumiInfoParser]get home html failed, bangumiHomeUrl: {}", bangumiHomeUrl, e);
            return "";
        }
    }

    /**
     * 解析html
     * */
    private BangumiParedInfo parseHtml(String domain, String html, Path posterStoredDirPath) {
        try {
            Document document = Jsoup.parse(html);
            String posterStoredPath = downloadPoster(document, domain, posterStoredDirPath);
            String bangumiTitle = parseBangumiTitle(document);
            Pair<Integer, Long> broadcastDayOfWeekAndDate = parseBroadcastDayOfWeekAndDate(document);

            return BangumiParedInfo
                    .builder()
                    .title(bangumiTitle)
                    .posterStoredPath(posterStoredPath)
                    .broadcastDayOfWeek(broadcastDayOfWeekAndDate.getLeft())
                    .broadcastDate(broadcastDayOfWeekAndDate.getRight())
                    .season(null)
                    .build();
        } catch (Exception e) {
            log.debug("[MikanBangumiInfoParser]parse home html failed", e);
            return DEFAULT_INFO;
        }

    }

    /**
     * 下载海报
     * @return 下载成功后，返回存储路径
     * */
    private String downloadPoster(Document document, String domain, Path posterStoredDirPath) {
        Element bangumiPosterDiv = document.getElementsByClass("bangumi-poster").getFirst();
        String style  = bangumiPosterDiv.attribute("style").getValue();
        Matcher posterUrlMatcher = POSTER_URL_PATTERN.matcher(style);
        if (!posterUrlMatcher.find()) {
            return "";
        }

        String posterUrl = STR."https://\{domain}\{posterUrlMatcher.group(1)}";

        Matcher posterNameMatcher = POSTER_NAME_PATTERN.matcher(posterUrl);
        String posterName;
        if (posterNameMatcher.find()) {
            posterName = posterNameMatcher.group(1);
        } else {
            posterName = STR."\{UUID.randomUUID().toString()}.jpg";
        }

        Path stoerdPath = posterStoredDirPath.resolve(posterName);

        Request request = new Request
                .Builder()
                .url(posterUrl)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                .get()
                .build();

        try {
            byte[] img = okHttpService.sendRequestByProxy(request, response -> {
                ResponseBody body = response.body();
                if (Objects.isNull(body)) {
                    return null;
                }
                return body.bytes();
            });

            // 如果不存在对应文件，那么保存
            if (Files.notExists(stoerdPath)) {
                Files.write(stoerdPath, img);
            }
            return stoerdPath.toString();
        } catch (IOException e) {
            log.debug("[MikanBangumiInfoParser]download poster failed", e);
            return "";
        }

    }

    /**
     * 解析出标题
     * */
    private String parseBangumiTitle(Document document) {
        Element bangumiTitleDiv = document.getElementsByClass("bangumi-title").getFirst();

        String content  = bangumiTitleDiv.text();
        int end = content.indexOf("<a");
        if (end < 0) {
            return content;
        }
        return content.substring(end);
    }

    /**
     * 解析出放松的星期几
     * */
    private Pair<Integer, Long> parseBroadcastDayOfWeekAndDate(Document document) {
        Elements bangumiInfoDivs = document.getElementsByClass("bangumi-info");

        Integer broadcastDayOfWeek = null;
        Long broadcastDate = null;
        for (Element element : bangumiInfoDivs) {
            String content = element.text();
            if (content.startsWith("放送日期：")) {
                String dayOfWeek = content.replace("放送日期：", "");
                switch (dayOfWeek) {
                    case "星期一" -> broadcastDayOfWeek = 1;
                    case "星期二" -> broadcastDayOfWeek = 2;
                    case "星期三" -> broadcastDayOfWeek = 3;
                    case "星期四" -> broadcastDayOfWeek = 4;
                    case "星期五" -> broadcastDayOfWeek = 5;
                    case "星期六" -> broadcastDayOfWeek = 6;
                    case "星期天", "星期日" -> broadcastDayOfWeek = 7;
                }
            } else if (content.startsWith("放送开始：")) {
                String date = content.replace("放送开始：", "");
                LocalDate localDate = LocalDate.parse(date, BROADCAST_DATE_FORMATTER);
                broadcastDate = localDate.atStartOfDay(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
            }
        }

        return Pair.of(broadcastDayOfWeek, broadcastDate);
    }
}
