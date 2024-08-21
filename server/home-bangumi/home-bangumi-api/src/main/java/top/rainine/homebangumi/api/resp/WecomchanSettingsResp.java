package top.rainine.homebangumi.api.resp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author rainine
 * @description wecomchan的设置
 * @date 2024/8/20 17:56:24
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class WecomchanSettingsResp {

    /**
     * 是否可用
     * */
    private Boolean enable;

    /**
     * wecomchan的地址
     * */
    private String url;

    /**
     * sendKey
     * */
    private String sendKey;
}
