package top.rainine.homebangumi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.rainine.homebangumi.dao.po.HbRssBangumi;
import top.rainine.homebangumi.def.enums.RssBangumiStatusEnum;
import top.rainine.homebangumi.def.enums.RssHandleMethodEnum;

import java.util.List;
import java.util.Optional;

/**
 * @author rainine
 * @description rss番剧的操作类
 * @date 2024/3/14 17:34:10
 */
@Repository
public interface HbRssBangumiRepository extends JpaRepository<HbRssBangumi, Long> {

    /**
     * 根据rss 链接判断是否已经提交过对应的数据
     * */
    Optional<HbRssBangumi> findByRssLink(String rssLink);

    /**
     * 通过状态和收集方式获取所有的rss bangumi信息
     * @param status 状态{@link RssBangumiStatusEnum#getStatus()}
     * @param handleMethod 处理方式{@link RssHandleMethodEnum#getMethod()}
     * */
    List<HbRssBangumi> findAllByStatusAndHandleMethod(Integer status, Integer handleMethod);

    long countByHandleMethodAndCreatedTimeBetween(Integer handleMethod, Long startTime, Long endTime);

    long countByHandleMethod(Integer handleMethod);
}

