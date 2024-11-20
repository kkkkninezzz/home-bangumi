package top.rainine.homebangumi.core.rss.impl;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author rainine
 * @description 在下载器中，未找到的计数器
 * @date 2024/11/20 14:44:05
 */
@Component
public class NotFoundInDownloaderCounter {

    private final ConcurrentMap<Long, AtomicInteger> episodeId2countMap = new ConcurrentHashMap<>();


    /**
     * 增加一次
     * */
    public void inc(Long episodeId) {
        episodeId2countMap.computeIfAbsent(episodeId, k -> new AtomicInteger())
                .incrementAndGet();
    }

    /**
     * 获取次数
     * */
    public int getCount(Long episodeId) {
        return Optional.ofNullable(episodeId2countMap.get(episodeId))
                .map(AtomicInteger::get)
                .orElse(0);
    }

    /**
     * 移除
     * */
    public void remove(Long episodeId) {
        episodeId2countMap.remove(episodeId);
    }
}
