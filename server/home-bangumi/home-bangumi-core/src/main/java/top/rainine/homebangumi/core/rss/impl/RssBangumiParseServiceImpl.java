package top.rainine.homebangumi.core.rss.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import top.rainine.homebangumi.common.utils.UriUtils;
import top.rainine.homebangumi.core.common.net.OkHttpService;
import top.rainine.homebangumi.core.rss.BangumiRssLinkParser;
import top.rainine.homebangumi.core.rss.RssBangumiParseService;
import top.rainine.homebangumi.core.rss.configuration.RssBangumiConfigProperties;
import top.rainine.homebangumi.core.rss.data.*;
import top.rainine.homebangumi.core.rss.data.convertor.RssBangumiEpisodeConvertor;
import top.rainine.homebangumi.core.common.episoderenamer.EpisodeTitleRenameAdapter;
import top.rainine.homebangumi.core.common.episoderenamer.EpisodeTitleRenameAdapterFactory;
import top.rainine.homebangumi.core.rss.exception.RssBangumiParseFailedException;
import top.rainine.homebangumi.core.rss.BangumiInfoParser;
import top.rainine.homebangumi.core.common.titleparser.EpisodeTitleParser;
import top.rainine.homebangumi.def.enums.RssBangumiEpisodeStatusEnum;
import top.rainine.homebangumi.def.enums.RssCategoryEnum;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @authoer rainine
 * @date 2024/3/18 00:16
 * @desc
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RssBangumiParseServiceImpl implements RssBangumiParseService, InitializingBean {

    /**
     * rss解析器
     * */
    private Map<RssCategoryEnum, BangumiRssLinkParser> rssLinkParserMap;

    /**
     * 番剧信息的解析器
     * */
    private Map<RssCategoryEnum, BangumiInfoParser> bangumiInfoParserMap;

    private final RssBangumiConfigProperties rssBangumiConfigProperties;

    private final OkHttpService okHttpService;

    private final EpisodeTitleParser episodeTitleParser;

    private final ApplicationContext applicationContext;

    private final EpisodeTitleRenameAdapterFactory episodeTitleRenameAdapterFactory;

    private final RssBangumiEpisodeConvertor rssBangumiEpisodeConvertor;

    @Override
    public void afterPropertiesSet() throws Exception {
        Collection<BangumiRssLinkParser> rssLinkParsers = applicationContext.getBeansOfType(BangumiRssLinkParser.class).values();
        this.rssLinkParserMap = rssLinkParsers.stream().collect(Collectors.toMap(BangumiRssLinkParser::getCategory, p -> p));

        Collection<BangumiInfoParser> bangumiInfoParsers = applicationContext.getBeansOfType(BangumiInfoParser.class).values();
        this.bangumiInfoParserMap = bangumiInfoParsers.stream().collect(Collectors.toMap(BangumiInfoParser::getCategory, p -> p));
    }

    @Override
    public RssBangumiPreviewInfo parse(RssBangumiParseConfig config) {
        RssCategoryEnum rssCategory = config.rssCategory();
        String rssLink = config.rssLink();
        Integer season = config.season();
        Integer skippedEpisodeNo = config.skippedEpisodeNo();
        List<String> filteredOutRules = config.filteredOutRules();

        // 计算出rssLikn的md5
        String rssLinkMd5 = DigestUtils.md5DigestAsHex(rssLink.getBytes(StandardCharsets.UTF_8));

        BangumiRssLinkParser bangumiRssLinkParser = rssLinkParserMap.get(rssCategory);
        if (Objects.isNull(bangumiRssLinkParser)) {
            throw new IllegalArgumentException(STR."[RssBangumiParseService]not supported rss category, rssCategory: \{rssCategory}");
        }

        BangumiInfoParser bangumiInfoParser = bangumiInfoParserMap.get(rssCategory);
        if (Objects.isNull(bangumiInfoParser)) {
            throw new IllegalArgumentException(STR."[RssBangumiParseService]not supported rss category, rssCategory: \{rssCategory}");
        }

        List<RssBangumiParsedInfo> rssBangumiParsedInfoList = bangumiRssLinkParser.parse(rssLink);
        if (CollectionUtils.isEmpty(rssBangumiParsedInfoList)) {
            log.info("[RssBangumiParseService] not found bangumi, rssLink: {}", rssLink);
            throw new RssBangumiParseFailedException();
        }

        // 这里只处理解析出来的第一个番剧
        RssBangumiParsedInfo parsedInfo = rssBangumiParsedInfoList.getFirst();

        // 解析出番剧信息
        BangumiParedInfo bangumiParedInfo = parseBangumiInfo(bangumiInfoParser, rssLinkMd5, rssLink);

        // 标题重命名适配器
        EpisodeTitleRenameAdapter episodeTitleRenameAdapter = episodeTitleRenameAdapterFactory.newRenameAdapter(config.episodeTitleRenameMethod(),
                bangumiParedInfo.title(),
                config.customizeRenamedEpisodeTitleFormat());

        // 生成需要解析的剧集预览信息
        List<RssBangumiEpisodePreviewInfo> previewInfoList = parseRssBangumiEpisodes(rssLinkMd5, parsedInfo.episodes(), season,
                skippedEpisodeNo, filteredOutRules, episodeTitleRenameAdapter);

        // 如果没有指定季，且未解析出番剧的季信息
        // 那么尝试使用剧集中的季信息
        int parsedSeason;
        if (Objects.nonNull(season)) {
            parsedSeason = season;
        } else if (Objects.nonNull(bangumiParedInfo.season())) {
            parsedSeason = bangumiParedInfo.season();
        } else {
            parsedSeason = previewInfoList
                    .stream()
                    .filter(previewInfo -> previewInfo.status() == RssBangumiEpisodeStatusEnum.PARSED)
                    .findFirst()
                    .map(RssBangumiEpisodePreviewInfo::season)
                    .orElse(1);
        }

//        String scrapedTitle = previewInfoList
//                .stream()
//                .filter(previewInfo -> previewInfo.status() == RssBangumiEpisodeStatusEnum.PARSED)
//                .findFirst()
//                .map(RssBangumiEpisodePreviewInfo::bangumiTitle)
//                .orElse("");

        String posterUrl = "";
        if (StringUtils.isNotEmpty(bangumiParedInfo.posterStoredPath())) {
            posterUrl = UriUtils.convertPathToUri(rssBangumiConfigProperties.getStaticResourceParentDir(), bangumiParedInfo.posterStoredPath());
        }

        return RssBangumiPreviewInfo
                .builder()
                .title(bangumiParedInfo.title())
                .posterStoredPath(bangumiParedInfo.posterStoredPath())
                .posterUrl(posterUrl)
                .broadcastDayOfWeek(bangumiParedInfo.broadcastDayOfWeek())
                .broadcastDate(bangumiParedInfo.broadcastDate())
                .season(parsedSeason)
                .episodes(previewInfoList)
                .episodeTitleRenameMethod(config.episodeTitleRenameMethod())
                .customizeRenamedEpisodeTitleFormat(config.customizeRenamedEpisodeTitleFormat())
                .build();
    }

    private List<RssBangumiEpisodePreviewInfo> parseRssBangumiEpisodes(String rssLinkMd5, List<RssBangumiEpisodeParsedInfo> episodeParsedInfoList,
                                                                       Integer season, Integer skippedEpisodeNo, List<String> filteredOutRules,
                                                                       EpisodeTitleRenameAdapter episodeTitleRenameAdapter) {
        // 处理根据rss上解析到的剧集名进行过滤
        List<RssBangumiEpisodeParsedInfo> pendingEpisodeParsedInfoList;
        List<RssBangumiEpisodePreviewInfo> filteredOutEpisodePreviewInfoList = new ArrayList<>(episodeParsedInfoList.size());
        if (CollectionUtils.isNotEmpty(filteredOutRules)) {
            pendingEpisodeParsedInfoList = new ArrayList<>(episodeParsedInfoList.size());

            // 判断该剧集是否被过滤掉
            for (RssBangumiEpisodeParsedInfo episodeParsedInfo: episodeParsedInfoList) {
                if (isFilteredOut(episodeParsedInfo.rawEpisodeTitle(), filteredOutRules)) {
                    filteredOutEpisodePreviewInfoList.add(rssBangumiEpisodeConvertor.toRssBangumiEpisodePreviewInfo(episodeParsedInfo, RssBangumiEpisodeStatusEnum.FILTERED_OUT));
                } else {
                    pendingEpisodeParsedInfoList.add(episodeParsedInfo);
                }
            }
        } else {
            pendingEpisodeParsedInfoList = episodeParsedInfoList;
        }

        // 针对于过滤后的剧集，进行种子下载和解析
        List<RssBangumiEpisodePreviewInfo> episodePreviewInfoList;
        if (CollectionUtils.isNotEmpty(pendingEpisodeParsedInfoList)) {
            episodePreviewInfoList = generateEpisodePreviewInfoList(rssLinkMd5, season, pendingEpisodeParsedInfoList, episodeTitleRenameAdapter);
        } else {
            episodePreviewInfoList = new ArrayList<>();
        }

        episodePreviewInfoList = episodePreviewInfoList
                .stream()
                .sorted(Comparator.comparing(previewInfo -> {
                    // 解析成功的按剧集号排序
                    if (previewInfo.status() == RssBangumiEpisodeStatusEnum.PARSED) {
                        return previewInfo.episodeNo();
                    }

                    // 如果解析失败，那么将顺序调整到最后
                    return 100000;
                }))
                .map(previewInfo -> {
                    // 处理剧集偏移
                    if (previewInfo.status() == RssBangumiEpisodeStatusEnum.PARSED
                            && Objects.nonNull(skippedEpisodeNo) && previewInfo.episodeNo() <= skippedEpisodeNo) {
                        return rssBangumiEpisodeConvertor.toRssBangumiEpisodePreviewInfo(previewInfo, RssBangumiEpisodeStatusEnum.SKIPPED);
                    }

                    return previewInfo;
                })
                .toList();

        List<RssBangumiEpisodePreviewInfo> result = new ArrayList<>(episodeParsedInfoList.size());

        // 生成需要解析的剧集预览信息
        result.addAll(episodePreviewInfoList);
        result.addAll(filteredOutEpisodePreviewInfoList);

        return result;
    }

    /**
     * 是否被过滤掉
     * */
    private boolean isFilteredOut(String rawEpisodeTitle, List<String> rules) {
        return rules.stream().anyMatch(rawEpisodeTitle::contains);
    }

    /**
     * 生成剧集的预览信息列表
     * */
    private List<RssBangumiEpisodePreviewInfo> generateEpisodePreviewInfoList(String rssLinkMd5, Integer season,
                                                                              List<RssBangumiEpisodeParsedInfo> episodeParsedInfoList,
                                                                              EpisodeTitleRenameAdapter episodeTitleRenameAdapter) {
        RssBangumiEpisodeTorrentParser parser = new RssBangumiEpisodeTorrentParser(okHttpService, episodeTitleParser, episodeTitleRenameAdapter);

        Path torrentStoredDirPath = Paths.get(rssBangumiConfigProperties.getTorrentRootDir()).resolve(rssLinkMd5);
        if (Files.notExists(torrentStoredDirPath)) {
            try {
                Files.createDirectories(torrentStoredDirPath);
            } catch (IOException e) {
                log.error("[RssBangumiParseService] create torrent stored dir failed, dir: {}", torrentStoredDirPath);
                throw new RssBangumiParseFailedException();
            }
        }

        // 并发生成预览信息
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Pair<RssBangumiEpisodeParsedInfo, Future<RssBangumiEpisodePreviewInfo>>> futureList = episodeParsedInfoList.stream()
                    .map(parsedInfo -> {
                        final Path torrentStoredPath = torrentStoredDirPath.resolve(FilenameUtils.getName(parsedInfo.torrentLink()));

                        Callable<RssBangumiEpisodePreviewInfo> callable = () -> parser.parse(parsedInfo, torrentStoredPath, season);
                        return Pair.of(parsedInfo, executorService.submit(callable));
                    })
                    .toList();

            return futureList
                    .stream()
                    .map(futurePair -> {
                        RssBangumiEpisodeParsedInfo parsedInfo = futurePair.getLeft();
                        Future<RssBangumiEpisodePreviewInfo> future = futurePair.getValue();
                        try {
                            return future.get(60, TimeUnit.SECONDS);
                        } catch (InterruptedException | ExecutionException | TimeoutException e) {
                            log.error("[RssBangumiParseService]concurrent execution parsing failure, rawEpisodeTitle: {}, torrentLink: {}",
                                    parsedInfo.rawEpisodeTitle(), parsedInfo.torrentLink(), e);

                            return rssBangumiEpisodeConvertor
                                    .toRssBangumiEpisodePreviewInfo(parsedInfo, RssBangumiEpisodeStatusEnum.ERROR);
                        }
                    }).toList();
        }
    }

    private BangumiParedInfo parseBangumiInfo(BangumiInfoParser bangumiInfoParser, String rssLinkMd5, String rssLink) {
        Path posterStoredDirPath = Paths.get(rssBangumiConfigProperties.getPosterRootDir()).resolve(rssLinkMd5);
        if (Files.notExists(posterStoredDirPath)) {
            try {
                Files.createDirectories(posterStoredDirPath);
            } catch (IOException e) {
                log.error("[RssBangumiParseService] create poster stored dir failed, dir: {}", posterStoredDirPath);
                throw new RssBangumiParseFailedException();
            }
        }

        try {

            return bangumiInfoParser.parse(rssLink, posterStoredDirPath);
        } catch (Exception e) {
            log.error("[RssBangumiParseService] parse bangumi info failed, rssLink: {}", rssLink, e);
            throw new RssBangumiParseFailedException();
        }
    }

    @Override
    public List<RssBangumiEpisodePreviewInfo> incrementalParse(RssBangumiIncrementalParseConfig config) {
        RssCategoryEnum rssCategory = config.rssCategory();
        String rssLink = config.rssLink();
        Integer season = config.season();
        Integer skippedEpisodeNo = config.skippedEpisodeNo();
        List<String> filteredOutRules = config.filteredOutRules();
        List<String> parsedTorrentLinks = config.parsedTorrentLinks();

        // 计算出rssLikn的md5
        String rssLinkMd5 = DigestUtils.md5DigestAsHex(rssLink.getBytes(StandardCharsets.UTF_8));

        BangumiRssLinkParser bangumiRssLinkParser = rssLinkParserMap.get(rssCategory);
        if (Objects.isNull(bangumiRssLinkParser)) {
            throw new IllegalArgumentException(STR."[RssBangumiParseService]not supported rss category, rssCategory: \{rssCategory}");
        }

        List<RssBangumiParsedInfo> rssBangumiParsedInfoList = bangumiRssLinkParser.parse(rssLink);
        if (CollectionUtils.isEmpty(rssBangumiParsedInfoList)) {
            log.info("[RssBangumiParseService] not found bangumi, rssLink: {}", rssLink);
            throw new RssBangumiParseFailedException();
        }

        // 这里只处理解析出来的第一个番剧
        RssBangumiParsedInfo parsedInfo = rssBangumiParsedInfoList.getFirst();

        List<RssBangumiEpisodeParsedInfo> episodeParsedInfolist;
        // 过滤掉已经解析过的链接
        if (CollectionUtils.isNotEmpty(parsedTorrentLinks)) {
            episodeParsedInfolist = parsedInfo.episodes()
                    .stream()
                    .filter(episode -> !parsedTorrentLinks.contains(episode.torrentLink()))
                    .toList();
        } else {
            episodeParsedInfolist = parsedInfo.episodes();
        }

        // 生成需要解析的剧集预览信息
        return parseRssBangumiEpisodes(rssLinkMd5, episodeParsedInfolist, season, skippedEpisodeNo, filteredOutRules, config.episodeTitleRenameAdapter());
    }
}






















