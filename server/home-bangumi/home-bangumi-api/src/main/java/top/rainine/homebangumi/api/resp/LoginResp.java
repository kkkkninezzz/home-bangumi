package top.rainine.homebangumi.api.resp;

import lombok.Builder;

/**
 * @param uid   用户id
 * @param accessToken 登录token
 * @author rainine
 * @description 登录结果
 * @date 2024/3/21 18:56:59
 */
@Builder
public record LoginResp(Long uid, String username, String accessToken) {
}
