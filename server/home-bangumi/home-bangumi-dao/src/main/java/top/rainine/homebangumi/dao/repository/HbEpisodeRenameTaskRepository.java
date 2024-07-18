package top.rainine.homebangumi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTask;

/**
 * @author rainine
 * @description
 * @date 2024/7/18 17:04:04
 */
@Repository
public interface HbEpisodeRenameTaskRepository extends JpaRepository<HbEpisodeRenameTask, Long> {
}
