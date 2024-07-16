package top.rainine.homebangumi.web.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import top.rainine.homebangumi.api.UserApi;
import top.rainine.homebangumi.api.resp.Result;
import top.rainine.homebangumi.api.resp.SelfInfoResp;
import top.rainine.homebangumi.core.user.UserService;

/**
 * @author rainine
 * @description 用户控制器
 * @date 2024/3/26 18:41:02
 */
@RestController
public class UserController implements UserApi {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Result<SelfInfoResp> getSelfInfo() {
        return Result.createSuccess(userService.getSelfInfo(StpUtil.getLoginIdAsLong()));
    }
}
