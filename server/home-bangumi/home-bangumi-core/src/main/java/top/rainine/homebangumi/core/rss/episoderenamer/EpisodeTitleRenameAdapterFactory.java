package top.rainine.homebangumi.core.rss.episoderenamer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.def.enums.EpisodeTitleRenameMethodEnum;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

/**
 * @author rainine
 * @description 剧集标题重命名服务类
 * @date 2024/6/25 18:53:21
 */
@Component
@Slf4j
public class EpisodeTitleRenameAdapterFactory {

    /**
     * 获取重命名适配器
     * */
    public EpisodeTitleRenameAdapter newRenameAdapter(EpisodeTitleRenameMethodEnum renameMethod, String bangumiOfficialTitle, String customizeRenamedEpisodeTitleFormat) {
        switch (renameMethod) {
            case NONE -> {
                return new EpisodeTitleRenameNoneAdapter();
            }
            case TORRENT_PARSED_TITLE -> {
                return new EpisodeTitleRenameByTorrentParsedTitleAdapter();
            }
            case OFFICIAL_TITLE -> {
                return new EpisodeTitleRenameByOfficialTitleAdapter(bangumiOfficialTitle);
            }
            case CUSTOMIZED_TITLE -> {
                return new EpisodeTitleRenameByCustomizeTitleAdapter(customizeRenamedEpisodeTitleFormat);
            }
            default -> {
                log.error("[EpisodeTitleRenameAdapterFactory]new rename adapter failed, not supported method: {}", renameMethod);
                throw new HbBizException(HbCodeEnum.EPISODE_TITLE_RENAME_ADAPTER_NOT_FOUND);
            }
        }
    }
}
