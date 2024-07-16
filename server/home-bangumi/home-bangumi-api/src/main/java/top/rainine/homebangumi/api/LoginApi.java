package top.rainine.homebangumi.api;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.rainine.homebangumi.api.req.LoginByPasswordReq;
import top.rainine.homebangumi.api.resp.LoginResp;
import top.rainine.homebangumi.api.resp.Result;

/**
 * @author rainine
 * @description 登录api
 * @date 2024/3/21 18:39:08
 */
@RequestMapping("/api/v1/login")
public interface LoginApi {

    /**
     * 通过密码登录
     * */
    @PostMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<LoginResp> loginByPassword(@Valid @RequestBody LoginByPasswordReq req);
}
