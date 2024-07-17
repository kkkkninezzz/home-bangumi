package top.rainine.homebangumi.dao.po;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.MessageCategoryEnum;
import top.rainine.homebangumi.def.enums.MessageTypeEnum;

/**
 * @authoer rainine
 * @date 2024/5/19 13:39
 * @desc
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "hb_message", indexes = {
})
public class HbMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 消息分类
     * @see MessageCategoryEnum#getCategory()
     * */
    @Column(name = "category", nullable = false)
    private Integer category;

    /**
     * 消息类型
     * @see MessageTypeEnum#getType()
     * */
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 是否已读
     * */
    @Column(name = "read", nullable = false)
    private Boolean read;

    /**
     * 消息标题
     * */
    @Column(name = "title", length = 256, nullable = false)
    private String title;

    /**
     * 消息内容
     * */
    @Column(name = "content", length = 512, nullable = false)
    private String content;

    /**
     * 主体id
     * */
    @Column(name = "subject_id", length = 128)
    private String subjectId;
}


















