package top.rainine.homebangumi.core.downloader.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.core.downloader.qbittorrent.QbittorrentEpisodeDownloadAdapter;
import top.rainine.homebangumi.core.event.data.QbittorrentDownloaderConfigModifiedEvent;

/**
 * @authoer rainine
 * @date 2024/5/11 00:07
 * @desc 事件监听器
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EpisodeDownloaderEventListener {

    private final QbittorrentEpisodeDownloadAdapter qbittorrentEpisodeDownloadAdapter;

    @EventListener(QbittorrentDownloaderConfigModifiedEvent.class)
    @Async
    public void onQbittorrentDownloaderConfigModifiedEvent(QbittorrentDownloaderConfigModifiedEvent event) {
        qbittorrentEpisodeDownloadAdapter.setDownloaderSettings();
    }
}
