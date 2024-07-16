package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.MessageCategoryEnum;
import top.rainine.homebangumi.def.enums.MessageTypeEnum;

/**
 * @authoer rainine
 * @date 2024/5/19 14:48
 * @desc 消息dto
 */
@Getter
@Setter
@ToString
public class MessageDto {
    private Long id;

    /**
     * 消息分类
     * @see MessageCategoryEnum#getCategory()
     * */
    private Integer category;

    /**
     * 消息类型
     * @see MessageTypeEnum#getType()
     * */
    private Integer type;

    /**
     * 是否已读
     * */
    private Boolean read;

    /**
     * 消息标题
     * */
    private String title;

    /**
     * 消息内容
     * */
    private String content;

    /**
     * 主体id
     * */
    private String subjectId;

    /**
     * 创建时间
     * */
    private Long createdTime;
}
