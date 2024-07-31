package top.rainine.homebangumi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTaskItem;

import java.util.List;
import java.util.Optional;

/**
 * @author rainine
 * @description
 * @date 2024/7/18 17:04:45
 */
@Repository
public interface HbEpisodeRenameTaskItemRepository extends JpaRepository<HbEpisodeRenameTaskItem, Long> {

    List<HbEpisodeRenameTaskItem> findAllByTaskId(Long taskId);

    void deleteAllByTaskId(Long taskId);

    Optional<HbEpisodeRenameTaskItem> findByTaskIdAndId(Long taskId, Long id);

    long countByTaskIdAndStatusIn(Long taskId, List<Integer> statusList);

    long countByTaskIdAndStatus(Long taskId, Integer status);
}
