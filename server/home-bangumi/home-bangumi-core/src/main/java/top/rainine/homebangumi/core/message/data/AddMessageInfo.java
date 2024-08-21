package top.rainine.homebangumi.core.message.data;

import lombok.Builder;
import top.rainine.homebangumi.def.enums.MessageCategoryEnum;
import top.rainine.homebangumi.def.enums.MessageTypeEnum;

/**
 * @author rainine
 * @description 新增消息
 * @date 2024/8/20 18:49:11
 */
@Builder
public record AddMessageInfo(MessageCategoryEnum category,
                             MessageTypeEnum type,
                             String title,
                             String content,
                             String subjectId,
                             boolean addToBox,
                             boolean push) {
}
