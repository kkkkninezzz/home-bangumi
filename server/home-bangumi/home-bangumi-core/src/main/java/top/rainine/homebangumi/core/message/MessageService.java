package top.rainine.homebangumi.core.message;

import top.rainine.homebangumi.api.resp.MessagesResp;
import top.rainine.homebangumi.core.message.data.AddMessageInfo;
import top.rainine.homebangumi.def.enums.MessageCategoryEnum;
import top.rainine.homebangumi.def.enums.MessageTypeEnum;

/**
 * @authoer rainine
 * @date 2024/5/19 14:19
 * @desc 消息类
 */
public interface MessageService {

    /**
     * 新增消息
     * */
    void addMessage(MessageCategoryEnum category, MessageTypeEnum type,
                    String title, String content, String subjectId);

    /**
     * 推送消息
     * */
    void pushMessage(MessageCategoryEnum category, MessageTypeEnum type,
                     String title, String content);

    /**
     * 新增消息
     * */
    void addMessage(AddMessageInfo info);

    /**
     * 获取消息
     * @param daysAgo 最多多少天以前的
     * @param limit 返回消息的条数
     * */
    MessagesResp loadMessages(Integer daysAgo, Integer limit);

    /**
     * 读消息
     * */
    void readMessage(Long id);

    /**
     * 读所有的消息
     * */
    void readAllMessages();
}
