package top.rainine.homebangumi.core.common.titleparser;

import top.rainine.homebangumi.core.common.titleparser.data.EpisodeTitleInfo;

/**
 * @author rainine
 * @description 标题解析器
 * @date 2024/3/14 11:58:55
 */
public interface EpisodeTitleParser {

    /**
     * 解析标题
     * @param torrentName 种子名
     * @param season 外部传入的季数，如果为null，会尝试自动生成
     * @return 返回标题信息
     * */
    EpisodeTitleInfo parseTitle(String torrentName, Integer season);
}
