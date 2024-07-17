package top.rainine.homebangumi.dao.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.rainine.homebangumi.dao.po.HbMessage;

import java.util.List;

/**
 * @authoer rainine
 * @date 2024/5/19 14:18
 * @desc
 */
@Repository
public interface HbMessageRepository extends JpaRepository<HbMessage, Long> {

    List<HbMessage> findAllByReadAndCreatedTimeGreaterThanOrderByCreatedTimeDesc(Boolean read, Long createdTimeStart, Pageable pageable);
}
