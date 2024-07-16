package top.rainine.homebangumi.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.rainine.homebangumi.api.resp.MessagesResp;
import top.rainine.homebangumi.api.resp.Result;

/**
 * @authoer rainine
 * @date 2024/5/19 15:03
 * @desc
 */
@RequestMapping("/api/v1/message")
public interface MessageApi {
    /**
     * 获取消息列表
     * */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<MessagesResp> getMessages();

    /**
     * 读消息
     * */
    @PutMapping(value = "/read/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> readMessage(@PathVariable("id") Long id);

    /**
     * 读所有的消息
     * */
    @PutMapping(value = "/read/all", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> readAllMessages();
}
