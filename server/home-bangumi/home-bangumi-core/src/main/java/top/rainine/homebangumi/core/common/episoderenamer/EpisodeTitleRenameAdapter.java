package top.rainine.homebangumi.core.common.episoderenamer;


import top.rainine.homebangumi.core.common.titleparser.data.EpisodeTitleInfo;

/**
 * @author rainine
 * @description
 * @date 2024/6/26 10:35:11
 */
public interface EpisodeTitleRenameAdapter {

    /**
     * 是否需要先进行title的解析
     * */
    default boolean whitParseTitle() {
        return true;
    }

    /**
     * 重命名标题
     * @param episodeFileName 原始文件名
     * @param episodeTitleInfo 解析后的标题信息
     * @return 返回重命名后的标题
     * */
    String renameTitle(String episodeFileName, EpisodeTitleInfo episodeTitleInfo);
}
