package top.rainine.homebangumi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.rainine.homebangumi.dao.po.HbBangumi;

/**
 * @author rainine
 * @description 番剧信息的操作类
 * @date 2024/3/14 17:34:10
 */
@Repository
public interface HbBangumiRepository extends JpaRepository<HbBangumi, Long> {
}
