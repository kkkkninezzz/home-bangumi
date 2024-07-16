package top.rainine.homebangumi.common.utils;

import java.net.URI;
import java.nio.file.Paths;

/**
 * @authoer rainine
 * @date 2024/4/10 20:53
 * @desc uri的工具类
 */
public class UriUtils {
    private UriUtils() {}

    /**
     * 将存储路径转换为uri资源路径
     * @param resourceDir 提供给前端访问的资源根目录
     * @param storedPathStr 实际的存储路径
     * @return 将存储路径转换为uri资源路径
     * */
    public static String convertPathToUri(String resourceDir, String storedPathStr) {
        URI resourceDirUri = Paths.get(resourceDir).toAbsolutePath().toUri();
        URI storedPathUri = Paths.get(storedPathStr).toAbsolutePath().toUri();

        return resourceDirUri.relativize(storedPathUri).getPath();
    }
}
