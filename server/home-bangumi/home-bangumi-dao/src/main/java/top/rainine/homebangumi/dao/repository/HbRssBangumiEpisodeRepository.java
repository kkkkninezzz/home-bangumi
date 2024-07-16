package top.rainine.homebangumi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.rainine.homebangumi.dao.po.HbRssBangumi;
import top.rainine.homebangumi.dao.po.HbRssBangumiEpisode;
import top.rainine.homebangumi.def.enums.RssBangumiEpisodeStatusEnum;

import java.util.List;
import java.util.Optional;

/**
 * @author rainine
 * @description 番剧信息的操作类
 * @date 2024/3/14 17:34:10
 */
@Repository
public interface HbRssBangumiEpisodeRepository extends JpaRepository<HbRssBangumiEpisode, Long> {

    List<HbRssBangumiEpisode> findAllByRssBangumiId(Long rssBangumiId);

    /**
     * 根据rssBangumiId和状态获取所有的记录
     * @param rssBangumiId {@link HbRssBangumi#getId()}
     * @param status 状态{@link RssBangumiEpisodeStatusEnum#getStatus()}
     * */
    List<HbRssBangumiEpisode> findAllByRssBangumiIdAndStatus(Long rssBangumiId, Integer status);

    /**
     * 根据状态获取所有的记录
     * @param status 状态{@link RssBangumiEpisodeStatusEnum#getStatus()}
     * */
    List<HbRssBangumiEpisode> findAllByStatus(Integer status);

    /**
     * 根据rssBangumiId和id获取episode
     * @param rssBangumiId {@link HbRssBangumi#getId()}
     * @param id 当前id
     * */
    Optional<HbRssBangumiEpisode> findByRssBangumiIdAndId(Long rssBangumiId, Long id);

    /**
     * 根据状态统计
     * */
    long countByStatus(Integer status);

    /**
     * 根据多个状态统计
     * */
    long countByStatusIn(List<Integer> statusList);

    /**
     * 根据多个状态统计
     * */
    long countByCreatedTimeGreaterThanEqualAndStatusIn(Long createdTime, List<Integer> statusList);
}
