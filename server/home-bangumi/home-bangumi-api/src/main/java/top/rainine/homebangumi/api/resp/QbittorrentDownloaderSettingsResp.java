package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @authoer rainine
 * @date 2024/5/11 23:59
 * @desc
 */
@Getter
@Setter
@ToString
public class QbittorrentDownloaderSettingsResp {

    /**
     * 不需要用 / 结尾
     * */
    private String baseUrl;

    private String username;

    private String password;

    private String downloadDir;
}
