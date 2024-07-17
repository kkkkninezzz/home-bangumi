package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author rainine
 * @description 重命名任务项
 * @date 2024/7/17 16:06:22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class EpisodeRenameTaskItemsResp {

    private List<EpisodeRenameTaskItemDto> items;
}
