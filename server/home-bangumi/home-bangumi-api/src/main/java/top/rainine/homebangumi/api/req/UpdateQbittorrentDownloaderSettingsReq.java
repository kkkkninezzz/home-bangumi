package top.rainine.homebangumi.api.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

/**
 * @authoer rainine
 * @date 2024/5/11 23:59
 * @desc
 */
@Getter
@Setter
@ToString
public class UpdateQbittorrentDownloaderSettingsReq {

    /**
     * 不需要用 / 结尾
     * */
    @NotEmpty(message = HbCodeEnum.ErrorCode.QBITTORRENT_BASE_URL_INVALID)
    private String baseUrl;

    @NotEmpty(message = HbCodeEnum.ErrorCode.QBITTORRENT_USERNAME_INVALID)
    private String username;

    @NotEmpty(message = HbCodeEnum.ErrorCode.QBITTORRENT_PASSWORD_INVALID)
    private String password;

    /**
     * 在qb中的路径
     * 如果qb在容器中，那么这个路径也是qb容器的中绝对路径
     * */
    @NotEmpty(message = HbCodeEnum.ErrorCode.QBITTORRENT_DOWNLOAD_DIR_INVALID)
    private String downloadDir;
}
