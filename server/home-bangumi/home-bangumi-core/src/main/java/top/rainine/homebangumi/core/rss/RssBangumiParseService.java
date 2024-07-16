package top.rainine.homebangumi.core.rss;

import top.rainine.homebangumi.core.rss.data.RssBangumiEpisodePreviewInfo;
import top.rainine.homebangumi.core.rss.data.RssBangumiIncrementalParseConfig;
import top.rainine.homebangumi.core.rss.data.RssBangumiParseConfig;
import top.rainine.homebangumi.core.rss.data.RssBangumiPreviewInfo;
import top.rainine.homebangumi.def.enums.RssCategoryEnum;

import java.util.List;

/**
 * @author rainine
 * @description rss番剧解析
 * @date 2024/3/14 18:53:24
 */
public interface RssBangumiParseService {

    /**
     * 解析rss数据，目前只支持单个番剧的rss
     * @return rss番剧预览信息
     * @throws top.rainine.homebangumi.core.rss.exception.RssBangumiParseFailedException 如果解析失败，抛出异常
     * */
    RssBangumiPreviewInfo parse(RssBangumiParseConfig config);

    /**
     * 增量解析
     * @return 新增的剧集信息
     * @throws top.rainine.homebangumi.core.rss.exception.RssBangumiParseFailedException 如果解析失败，抛出异常
     * */
    List<RssBangumiEpisodePreviewInfo> incrementalParse(RssBangumiIncrementalParseConfig config);
}
