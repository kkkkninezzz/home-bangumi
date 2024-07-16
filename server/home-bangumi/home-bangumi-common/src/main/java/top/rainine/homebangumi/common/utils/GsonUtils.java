package top.rainine.homebangumi.common.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * @author rainine
 * @description gson工具类
 * @date 2024/3/19 16:30:34
 */
public class GsonUtils {

    public static final Gson GSON = new Gson();

    public static <T> String toJson(List<T> list) {
        if (Objects.isNull(list)) {
            return "[]";
        }

        return GSON.toJson(list);
    }

    private static Type getGenericTypeForList(Class<?> elementClass) {
        return TypeToken.getParameterized(List.class, elementClass).getType();
    }

    /**
     * 将json array的字符串转换为数组
     * */
    public static <T> List<T> toList(String jsonArrStr, Class<T> clazz) {
        return GSON.fromJson(jsonArrStr, getGenericTypeForList(clazz));
    }

    public static <T> T toJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }
}
