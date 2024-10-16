package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author rainine
 * @description 剧集重命名任务的设置
 * @date 2024/8/20 17:56:24
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class EpisodeRenameTaskSettingsResp {

    /**
     * 源目录路径
     * */
    private String sourceDirPath;

    /**
     * 输出目录路径
     * */
    private String outDirPath;

}
