package top.rainine.homebangumi.web.test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @authoer rainine
 * @date 2024/4/28 21:32
 * @desc
 */
public class Test {
    public static void main(String[] args) {
        String pathStr = "/downloads/test/12.mp4";
        Path path = Paths.get(pathStr);
        System.out.println(path.getParent());
        System.out.println(path.getFileName());
    }
}
