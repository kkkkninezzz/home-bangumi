package top.rainine.homebangumi.dao.po;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author rainine
 * @description 用户账号
 * @date 2024/3/22 11:25:31
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "hb_user_account", indexes = {
        @Index(name = "idx_username", columnList = "username", unique = true)
})
public class HbUserAccount extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 64, nullable = false)
    private String username;

    @Column(name = "password", length = 64, nullable = false)
    private String password;
}
