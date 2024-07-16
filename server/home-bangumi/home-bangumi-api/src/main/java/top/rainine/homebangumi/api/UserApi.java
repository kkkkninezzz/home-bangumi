package top.rainine.homebangumi.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.rainine.homebangumi.api.resp.Result;
import top.rainine.homebangumi.api.resp.SelfInfoResp;

/**
 * @author rainine
 * @description 用户个人信息api
 * @date 2024/3/26 18:36:20
 */
@RequestMapping("/api/v1/user")
public interface UserApi {

    /**
     * 获取当前登录用户的信息
     * */
    @GetMapping(value = "/self", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<SelfInfoResp> getSelfInfo();

}
