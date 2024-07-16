package top.rainine.homebangumi.core.user;

import top.rainine.homebangumi.api.req.LoginByPasswordReq;
import top.rainine.homebangumi.api.resp.LoginResp;
import top.rainine.homebangumi.api.resp.SelfInfoResp;

/**
 * @authoer rainine
 * @date 2024/3/21 23:28
 * @desc
 */
public interface UserService {

    /**
     * 登录
     * */
    LoginResp login(LoginByPasswordReq req);

    /**
     * 获取当前用户信息
     * */
    SelfInfoResp getSelfInfo(Long uid);
}
