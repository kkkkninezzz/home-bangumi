package top.rainine.homebangumi.core.rss.episoderenamer;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.text.StrSubstitutor;
import top.rainine.homebangumi.core.titleparser.data.EpisodeTitleInfo;

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
