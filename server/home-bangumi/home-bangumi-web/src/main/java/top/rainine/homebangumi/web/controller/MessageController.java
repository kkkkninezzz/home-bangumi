package top.rainine.homebangumi.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import top.rainine.homebangumi.api.MessageApi;
import top.rainine.homebangumi.api.resp.MessagesResp;
import top.rainine.homebangumi.api.resp.Result;
import top.rainine.homebangumi.core.message.MessageService;

/**
 * @authoer rainine
 * @date 2024/5/19 15:12
 * @desc
 */
@RestController
@RequiredArgsConstructor
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @Override
    public Result<MessagesResp> getMessages() {
        // TODO 默认15天之内，最多30条
        return Result.createSuccess(messageService.loadMessages(15, 30));
    }

    @Override
    public Result<Void> readMessage(Long id) {
        messageService.readMessage(id);
        return Result.createSuccess();
    }

    @Override
    public Result<Void> readAllMessages() {
        messageService.readAllMessages();
        return Result.createSuccess();
    }
}
