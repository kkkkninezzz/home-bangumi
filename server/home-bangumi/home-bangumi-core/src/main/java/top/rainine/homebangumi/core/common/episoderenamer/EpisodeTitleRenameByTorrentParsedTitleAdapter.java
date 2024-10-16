package top.rainine.homebangumi.core.common.episoderenamer;

import top.rainine.homebangumi.core.common.titleparser.data.EpisodeTitleInfo;

/**
 * @author rainine
 * @description 基于种子解析出来的标题
 * @date 2024/6/26 11:18:47
 */
public class EpisodeTitleRenameByTorrentParsedTitleAdapter implements EpisodeTitleRenameAdapter {

    @Override
    public String renameTitle(String episodeFileName, EpisodeTitleInfo episodeTitleInfo) {
        int season = episodeTitleInfo.season();
        int episodeNo = episodeTitleInfo.episode();
        String seasonStr = STR."\{season < 10 ? "0": ""}\{season}";
        String episodeStr = STR."\{episodeNo < 10 ? "0": ""}\{episodeNo}";
        return STR."\{episodeTitleInfo.title()} S\{seasonStr}E\{episodeStr}";
    }
}
