package top.rainine.homebangumi.def.utils;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * @author rainine
 * @description 枚举的工具类
 * @date 2023/9/13 14:38:24
 */
public class EnumUtils {

    /**
     * 判断是否包含目标code，code类型为int
     * @param enums 枚举数组
     * @param <T> 枚举类型
     * @param codeGetter 获取枚举code的方法
     * @param code 目标枚举值
     *
     * @return 如果包含返回true
     * */
    public static <T> boolean containsInt(T[] enums, ToIntFunction<T> codeGetter, int code) {
        for (T e: enums) {
            if (codeGetter.applyAsInt(e) == code) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否包含目标code，code类型为integer
     * @param enums 枚举数组
     * @param <T> 枚举类型
     * @param codeGetter 获取枚举code的方法
     * @param code 目标枚举值
     *
     * @return 如果包含返回true
     * */
    public static <T> boolean containsInteger(T[] enums, ToIntFunction<T> codeGetter, Integer code) {
        if (Objects.isNull(code)) {
            return false;
        }

        for (T e: enums) {
            if (codeGetter.applyAsInt(e) == code) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否包含目标code，code类型为Object
     * @param enums 枚举数组
     * @param <T> 枚举类型
     * @param <Code> 枚举code的类型
     * @param codeGetter 获取枚举code的方法
     * @param code 目标枚举值
     *
     * @return 如果包含返回true
     * */
    public static <T, Code> boolean contains(T[] enums, Function<T, Code> codeGetter, Code code) {
        if (Objects.isNull(code)) {
            return false;
        }

        for (T e: enums) {
            if (Objects.equals(codeGetter.apply(e), code)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 基于integer类型的code 获取对应的枚举类
     * @param enums 枚举数组
     * @param <T> 枚举类型
     * @param codeGetter 获取枚举code的方法
     * @param code 目标枚举值
     * @param defaultValue 默认值
     *
     * @return 返回枚举对象
     * */
    public static <T> T ofInteger(T[] enums, ToIntFunction<T> codeGetter, Integer code, T defaultValue) {
        if (Objects.isNull(code)) {
            return defaultValue;
        }

        for (T e: enums) {
            if (codeGetter.applyAsInt(e) == code) {
                return e;
            }
        }

        return defaultValue;
    }

    /**
     * 基于int类型的code 获取对应的枚举类
     * @param enums 枚举数组
     * @param <T> 枚举类型
     * @param codeGetter 获取枚举code的方法
     * @param code 目标枚举值
     * @param defaultValue 默认值
     *
     * @return 枚举对象
     * */
    public static <T> T ofInt(T[] enums, ToIntFunction<T> codeGetter, int code, T defaultValue) {
        for (T e: enums) {
            if (codeGetter.applyAsInt(e) == code) {
                return e;
            }
        }

        return defaultValue;
    }

    /**
     * 判断是否包含目标code，code类型为Object
     * @param enums 枚举数组
     * @param <T> 枚举类型
     * @param <Code> 枚举code的类型
     * @param codeGetter 获取枚举code的方法
     * @param code 目标枚举值
     *
     * @return 枚举对象
     * */
    public static <T, Code> T of(T[] enums, Function<T, Code> codeGetter, Code code, T defaultValue) {
        if (Objects.isNull(code)) {
            return defaultValue;
        }

        for (T e: enums) {
            if (Objects.equals(codeGetter.apply(e), code)) {
                return e;
            }
        }

        return defaultValue;
    }
}




















