package top.rainine.homebangumi.core.rss.episoderenamer;


import top.rainine.homebangumi.core.titleparser.data.EpisodeTitleInfo;

/**
 * @author rainine
 * @description
 * @date 2024/6/26 10:35:11
 */
public interface EpisodeTitleRenameAdapter {

    /**
     * 重命名标题
     * @param episodeFileName 原始文件名
     * @param episodeTitleInfo 解析后的标题信息
     * @return 返回重命名后的标题
     * */
    String renameTitle(String episodeFileName, EpisodeTitleInfo episodeTitleInfo);
}
