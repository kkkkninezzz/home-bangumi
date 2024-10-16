package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @authoer rainine
 * @date 2024/10/14 21:21
 * @desc 是否为空目录的返回
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class IsEmptyDirResp {

    /**
     * 如果路径是文件，那么为true
     * */
    private Boolean isFile;

    /**
     * {@link IsEmptyDirResp#isFile == false}
     * 表明当前目录是否为空
     * */
    private Boolean isEmpty;

}
