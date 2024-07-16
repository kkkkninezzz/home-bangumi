package top.rainine.homebangumi.dao.po;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.SystemSettingKeyEnum;

/**
 * @author rainine
 * @description 系统配置信息
 * @date 2024/3/14 11:11:21
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "hb_system_setting",
        indexes = {
        @Index(name = "idx_setting_key", columnList = "setting_key", unique = true)
})
public class HbSystemSetting extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 配置key
     * @see SystemSettingKeyEnum#getKey()
     * */
    @Column(name = "setting_key", length = 128, nullable = false)
    private String settingKey;

    /**
     * 配置值
     * */
    @Column(name = "setting_value", length = 512)
    private String settingValue;
}
