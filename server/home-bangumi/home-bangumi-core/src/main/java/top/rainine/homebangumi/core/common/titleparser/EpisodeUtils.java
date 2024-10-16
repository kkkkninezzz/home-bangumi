package top.rainine.homebangumi.core.common.titleparser;

/**
 * @author rainine
 * @description 番剧刮削文件名工具类
 * @date 2024/5/6 17:48:12
 */
public class EpisodeUtils {
    private EpisodeUtils() {}

    /**
     * 获取刮削文件名
     * */
    public static String getScrapedEpisodeFileName(Integer season, Integer episodeNo, String scrapedEpisodeTitle, String fileExtension) {
        String seasonStr = STR."\{season < 10 ? "0": ""}\{season}";
        String episodeStr = STR."\{episodeNo < 10 ? "0": ""}\{episodeNo}";
        return STR."\{scrapedEpisodeTitle} S\{seasonStr}E\{episodeStr}.\{fileExtension}";
    }
}
