package top.rainine.homebangumi.core.rss.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.message.MessageService;
import top.rainine.homebangumi.core.message.data.AddMessageInfo;
import top.rainine.homebangumi.dao.po.HbRssBangumi;
import top.rainine.homebangumi.dao.po.HbRssBangumiEpisode;
import top.rainine.homebangumi.def.enums.MessageCategoryEnum;
import top.rainine.homebangumi.def.enums.MessageTypeEnum;

/**
 * @authoer rainine
 * @date 2024/5/19 22:31
 * @desc
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RssBangumiAlterMessageComponent {

    private final MessageService messageService;

    private final RssBangumiComponent rssBangumiComponent;

    @FunctionalInterface
    interface MessageContentGenerator {
        String generate(HbRssBangumiEpisode episode, String bangumiTitle);
    }

    /**
     * 新增剧集重命名失败消息
     * */
    public void addEpisodeRenameFailedMessage(Long episodeId) {

        addWaringMessage(episodeId, "剧集重命名失败",
                (episode, bangumiTitle) -> STR."\{bangumiTitle} 第\{episode.getEpisodeNo()}集 重命名失败，请手动处理");
    }

    private void addWaringMessage(Long episodeId, String title, MessageContentGenerator messageContentGenerator) {
        HbRssBangumiEpisode episode = rssBangumiComponent.getRssBangumiEpisodeOrThrow(episodeId);
        HbRssBangumi rssBangumi = rssBangumiComponent.getRssBangumiOrThrow(episode.getRssBangumiId());
        String bangumiTitle = rssBangumiComponent.getBangumiTitle(rssBangumi.getBangumiId());

        String messageContent = messageContentGenerator.generate(episode, bangumiTitle);

        messageService.addMessage(AddMessageInfo.builder()
                .category(MessageCategoryEnum.RSS_BANGUMI_EPISODE)
                .type(MessageTypeEnum.WARNING)
                .title(title)
                .content(messageContent)
                .subjectId(rssBangumi.getId().toString())
                .addToBox(true)
                .push(true)
                .build());
    }

    /**
     * 新增剧集下载失败消息
     * */
    public void addEpisodeDownloadFailedMessage(Long episodeId) {
        addWaringMessage(episodeId, "剧集下载失败",
                (episode, bangumiTitle) -> STR."\{bangumiTitle} 第\{episode.getEpisodeNo()}集 下载失败，请手动处理");
    }

    /**
     * 新增剧集解析失败消息
     * */
    public void addEpisodeTitleParseFailedMessage(Long episodeId) {
        addWaringMessage(episodeId, "剧集标题解析失败",
                (episode, bangumiTitle) -> STR."\{bangumiTitle} 出现标题解析失败，请手动处理。从种子获取到的剧集名: \"\{episode.getEpisodeFileName()}\"");
    }

    /**
     * 新增剧集种子下载失败的消息
     * */
    public void addTorrentDownloadFailedMessage(Long episodeId) {
        addWaringMessage(episodeId, "剧集种子下载失败",
                (episode, bangumiTitle) -> STR."\{bangumiTitle} 下载种子失败，请手动处理。种子链接: \"\{episode.getTorrentLink()}\"");
    }

    /**
     * 新增剧集完成的消息
     * */
    public void addEpisodeFinishedMessage(Long episodeId) {
        HbRssBangumiEpisode episode = rssBangumiComponent.getRssBangumiEpisodeOrThrow(episodeId);
        HbRssBangumi rssBangumi = rssBangumiComponent.getRssBangumiOrThrow(episode.getRssBangumiId());
        String bangumiTitle = rssBangumiComponent.getBangumiTitle(rssBangumi.getBangumiId());

        String title = "剧集有更新";
        String messageContent = STR."\{bangumiTitle} 更新了第\{episode.getEpisodeNo()}集";

        messageService.addMessage(AddMessageInfo.builder()
                .category(MessageCategoryEnum.RSS_BANGUMI_EPISODE)
                .type(MessageTypeEnum.INFO)
                .title(title)
                .content(messageContent)
                .subjectId(rssBangumi.getId().toString())
                .addToBox(false)
                .push(true)
                .build());
    }
}
