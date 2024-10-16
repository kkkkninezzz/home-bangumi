package top.rainine.homebangumi.core.common.episoderenamer;

import lombok.RequiredArgsConstructor;
import top.rainine.homebangumi.core.common.titleparser.data.EpisodeTitleInfo;

/**
 * @author rainine
 * @description 基于自定义标题
 * @date 2024/6/26 11:18:47
 */
@RequiredArgsConstructor
public class EpisodeTitleRenameByCustomizeTitleAdapter implements EpisodeTitleRenameAdapter {

    private final String customizeRenamedEpisodeTitleFormat;

    @Override
    public String renameTitle(String episodeFileName, EpisodeTitleInfo episodeTitleInfo) {
        int season = episodeTitleInfo.season();
        int episodeNo = episodeTitleInfo.episode();
        String seasonStr = STR."\{season < 10 ? "0": ""}\{season}";
        String episodeStr = STR."\{episodeNo < 10 ? "0": ""}\{episodeNo}";

        return customizeRenamedEpisodeTitleFormat.replace("{season}", seasonStr)
                .replace("{episode}", episodeStr);
    }
}
