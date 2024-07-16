package top.rainine.homebangumi.core.downloader.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.downloader.EpisodeDownloadAdapter;
import top.rainine.homebangumi.core.downloader.EpisodeDownloaderManager;
import top.rainine.homebangumi.core.downloader.exception.NotSupportedDownloaderCategoryException;
import top.rainine.homebangumi.def.enums.DownloaderCategoryEnum;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @authoer rainine
 * @date 2024/5/10 23:39
 * @desc
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EpisodeDownloaderManagerImpl implements EpisodeDownloaderManager, InitializingBean {

    private final ApplicationContext applicationContext;

    private final Map<DownloaderCategoryEnum, EpisodeDownloadAdapter> adapterMap = new ConcurrentHashMap<>();

    @Override
    public EpisodeDownloadAdapter getDownloaderAdapter(DownloaderCategoryEnum downloaderCategory) {
        EpisodeDownloadAdapter episodeDownloadAdapter = adapterMap.get(downloaderCategory);
        if (Objects.isNull(episodeDownloadAdapter)) {
            log.error("[EpisodeDownloaderManager]not support downloaderCategory: {}", downloaderCategory);
            throw new NotSupportedDownloaderCategoryException();
        }
        return episodeDownloadAdapter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EpisodeDownloadAdapter> map = applicationContext.getBeansOfType(EpisodeDownloadAdapter.class);
        if (MapUtils.isEmpty(map)) {
            return;
        }

        map.values().forEach(adapter -> adapterMap.put(adapter.getCategory(), adapter));
    }
}
