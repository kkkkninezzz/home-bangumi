package top.rainine.homebangumi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.rainine.homebangumi.dao.po.HbUserAccount;

import java.util.Optional;

/**
 * @author rainine
 * @description 数据库操作类
 * @date 2024/3/22 11:33:54
 */
@Repository
public interface HbUserAccountRepository extends JpaRepository<HbUserAccount, Long> {

    /**
     * 通过账号查询
     * */
    Optional<HbUserAccount> findByUsername(String username);

}
