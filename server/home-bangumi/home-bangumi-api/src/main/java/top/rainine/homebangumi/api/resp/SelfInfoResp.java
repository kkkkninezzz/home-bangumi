package top.rainine.homebangumi.api.resp;

import lombok.Builder;

/**
 * @param id       用户id
 * @param nickname 昵称
 * @param avatar   头像地址
 *
 * @author rainine
 * @description 当前登录用户的信息
 * @date 2024/3/26 18:38:13
 */
@Builder
public record SelfInfoResp(Long uid, String nickname, String avatar) {

}
