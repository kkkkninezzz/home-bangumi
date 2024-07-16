package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @authoer rainine
 * @date 2024/5/19 14:47
 * @desc 消息返回
 */
@Getter
@Setter
@ToString
public class MessagesResp {

    private List<MessageDto> messages;
}
