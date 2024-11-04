package top.rainine.homebangumi.core.common.episoderenamer;

import org.apache.commons.io.FilenameUtils;
import top.rainine.homebangumi.core.common.titleparser.data.EpisodeTitleInfo;

/**
 * @author rainine
 * @description 保存原始命名
 * @date 2024/6/26 11:18:47
 */
public class EpisodeTitleRenameNoneAdapter implements EpisodeTitleRenameAdapter {

    @Override
    public boolean withParseTitle() {
        return false;
    }

    @Override
    public String renameTitle(String episodeFileName, EpisodeTitleInfo episodeTitleInfo) {
        return FilenameUtils.getBaseName(episodeFileName);
    }
}
