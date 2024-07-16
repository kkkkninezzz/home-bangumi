package top.rainine.homebangumi.core.rss.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.event.data.*;
import top.rainine.homebangumi.core.rss.RssBangumiEpisodeDownloadService;
import top.rainine.homebangumi.core.rss.RssBangumiService;
import top.rainine.homebangumi.core.rss.impl.RssBangumiAlterMessageComponent;

/**
 * @authoer rainine
 * @date 2024/4/24 23:32
 * @desc 事件监听器
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RssBangumiEventListener {

    private final RssBangumiEpisodeDownloadService episodeDownloadService;

    private final RssBangumiService rssBangumiService;

    private final RssBangumiAlterMessageComponent alterMessageComponent;

    @EventListener(RssSubscriptionUpdatedEvent.class)
    @Async
    public void onRssSubscriptionUpdatedEvent(RssSubscriptionUpdatedEvent event) {
        episodeDownloadService.pushToDownloader(event.getRssBangumiId());
    }

    @EventListener(EpisodeDownloadFinishedEvent.class)
    @Async
    public void onEpisodeDownloadFinishedEvent(EpisodeDownloadFinishedEvent event) {
        episodeDownloadService.renameEpisode(event.getEpisodeId());
    }

    @EventListener(EpisodeRenameFinishedEvent.class)
    @Async
    public void onEpisodeRenameFinishedEvent(EpisodeRenameFinishedEvent event) {
        rssBangumiService.onEpisodeRenameFinished(event.getEpisodeId());
    }

    @EventListener(EpisodeRenameFailedEvent.class)
    @Async
    public void onEpisodeRenameFailedEvent(EpisodeRenameFailedEvent event) {
        alterMessageComponent.addEpisodeRenameFailedMessage(event.getEpisodeId());
    }

    @EventListener(EpisodeDownloadFailedEvent.class)
    @Async
    public void onEpisodeDownloadFailedEvent(EpisodeDownloadFailedEvent event) {
        alterMessageComponent.addEpisodeDownloadFailedMessage(event.getEpisodeId());
    }

    @EventListener(EpisodeTitleParseFailedEvent.class)
    @Async
    public void onEpisodeTitleParseFailedEvent(EpisodeTitleParseFailedEvent event) {
        alterMessageComponent.addEpisodeTitleParseFailedMessage(event.getEpisodeId());
    }

    @EventListener(EpisodeTorrentDownloadFailedEvent.class)
    @Async
    public void onEpisodeTorrentDownloadFailedEvent(EpisodeTorrentDownloadFailedEvent event) {
        alterMessageComponent.addTorrentDownloadFailedMessage(event.getEpisodeId());
    }
}
