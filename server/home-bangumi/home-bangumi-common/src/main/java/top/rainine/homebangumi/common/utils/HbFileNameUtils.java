package top.rainine.homebangumi.common.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @authoer rainine
 * @date 2024/10/7 14:02
 * @desc
 */
public class HbFileNameUtils {
    private HbFileNameUtils() {}

    private static final Pattern ILLEGAL_CHARS_PATTERN = Pattern.compile("[:*?\"<>|\0]");

    public static boolean isValidFileName(String fileName) {
        // 检查文件名是否为空
        if (Objects.isNull(fileName) || fileName.isEmpty()) {
            return false;
        }

        // 如果文件名以盘符开头，保留盘符部分
        if (fileName.length() >= 2 && fileName.charAt(1) == ':') {
            String pathPart = fileName.substring(2);
            return !ILLEGAL_CHARS_PATTERN.matcher(pathPart).find();
        } else {
            // 使用正则表达式检查文件名是否包含非法字符
            return !ILLEGAL_CHARS_PATTERN.matcher(fileName).find();
        }
    }

    /**
     * 过滤文件名中的非法字符
     *
     * @param fileName 文件名
     * @return 过滤后的文件名
     */
    public static String filterIllegalChars(String fileName) {
        if (Objects.isNull(fileName) || fileName.isEmpty()) {
            return "";
        }

        // 如果文件名以盘符开头，保留盘符部分
        if (fileName.length() >= 2 && fileName.charAt(1) == ':') {
            char driveLetter = fileName.charAt(0);
            String pathPart = fileName.substring(2);
            return STR."\{driveLetter}:\{ILLEGAL_CHARS_PATTERN.matcher(pathPart).replaceAll("_")}";
        } else {
            return ILLEGAL_CHARS_PATTERN.matcher(fileName).replaceAll("_");
        }
    }

    public static boolean isDir(String pathStr) {
        Path path = Paths.get(pathStr);
        return Files.notExists(path) || Files.isDirectory(path);
    }
}
