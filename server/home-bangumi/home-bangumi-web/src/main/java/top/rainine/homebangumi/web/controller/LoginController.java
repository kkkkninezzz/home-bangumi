package top.rainine.homebangumi.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import top.rainine.homebangumi.api.LoginApi;
import top.rainine.homebangumi.api.req.LoginByPasswordReq;
import top.rainine.homebangumi.api.resp.LoginResp;
import top.rainine.homebangumi.api.resp.Result;
import top.rainine.homebangumi.core.user.UserService;

/**
 * @authoer rainine
 * @date 2024/3/22 00:03
 * @desc 登录接口
 */
@RestController
public class LoginController implements LoginApi {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Result<LoginResp> loginByPassword(LoginByPasswordReq req) {
        return Result.createSuccess(userService.login(req));
    }
}
