package top.rainine.homebangumi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.rainine.homebangumi.dao.po.HbSystemSetting;

import java.util.List;
import java.util.Optional;

/**
 * @author rainine
 * @description 系统配置的数据库操作对象
 * @date 2024/3/14 17:37:36
 */
@Repository
public interface HbSystemSettingRepository extends JpaRepository<HbSystemSetting, Long> {

    /**
     * 通过settingKey获取一个配置
     * */
    Optional<HbSystemSetting> findBySettingKey(String settingKey);

    /**
     * 通过settingKey获取多个配置
     * */
    List<HbSystemSetting> findAllBySettingKeyIn(List<String> settingKeys);
}
