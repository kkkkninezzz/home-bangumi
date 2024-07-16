package top.rainine.homebangumi.api.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author rainine
 * @description 是否要删除文件的标记对象
 * @date 2024/7/1 18:53:40
 */
@Getter
@Setter
@ToString
public class DeleteFileFlagDto {
    /**
     * 删除文件
     * */
    private Boolean deleteFile = false;
}
