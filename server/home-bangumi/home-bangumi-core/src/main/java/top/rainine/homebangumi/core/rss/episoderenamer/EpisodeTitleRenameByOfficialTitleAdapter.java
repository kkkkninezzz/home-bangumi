package top.rainine.homebangumi.core.rss.episoderenamer;

import lombok.RequiredArgsConstructor;
import top.rainine.homebangumi.core.titleparser.data.EpisodeTitleInfo;

/**
 * @author rainine
 * @description 基于官方标题
 * @date 2024/6/26 11:18:47
 */
@RequiredArgsConstructor
public class EpisodeTitleRenameByOfficialTitleAdapter implements EpisodeTitleRenameAdapter {

    private final String officialTitle;

    @Override
    public String renameTitle(String episodeFileName, EpisodeTitleInfo episodeTitleInfo) {
        int season = episodeTitleInfo.season();
        int episodeNo = episodeTitleInfo.episode();
        String seasonStr = STR."\{season < 10 ? "0": ""}\{season}";
        String episodeStr = STR."\{episodeNo < 10 ? "0": ""}\{episodeNo}";
        return STR."\{officialTitle} S\{seasonStr}E\{episodeStr}";
    }
}
