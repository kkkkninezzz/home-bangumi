package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author rainine
 * @description 创建剧集重命名任务返回
 * @date 2024/7/17 15:02:36
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CreateEpisodeRenameTaskResp {

    /**
     * 任务id
     * */
    private Long taskId;
}
