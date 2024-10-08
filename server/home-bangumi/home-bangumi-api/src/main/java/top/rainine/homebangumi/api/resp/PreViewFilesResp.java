package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @authoer rainine
 * @date 2024/10/7 21:24
 * @desc 预览文件结果
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class PreViewFilesResp {

    /**
     * 本次请求的path
     * */
    private String path;

    /**
     * 当前的文件名或者目录名
     * */
    private String name;

    /**
     * 是否为文件
     * */
    private Boolean isFile;

    /**
     * 如果是目录，那么返回目录下的文件列表
     * */
    private List<ChildFileInfo> children;


    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class ChildFileInfo {
        /**
         * 绝对路径
         * */
        private String path;

        /**
         * 当前的文件名或者目录名
         * */
        private String name;

        /**
         * 是否为目录
         * */
        private Boolean isFile;
    }
}
