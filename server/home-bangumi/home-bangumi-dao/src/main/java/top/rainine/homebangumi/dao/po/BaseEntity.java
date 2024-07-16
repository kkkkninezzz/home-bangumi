package top.rainine.homebangumi.dao.po;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author rainine
 * @description 基础对象
 * @date 2024/3/14 11:15:11
 */
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(name = "last_modified_time", nullable = false)
    private Long lastModifiedTime;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_time", updatable = false, nullable = false)
    private Long createdTime;
}
