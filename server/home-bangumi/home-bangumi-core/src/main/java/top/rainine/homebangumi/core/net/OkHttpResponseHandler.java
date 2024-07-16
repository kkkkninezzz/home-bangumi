package top.rainine.homebangumi.core.net;

import okhttp3.Response;

import java.io.IOException;

/**
 * @author rainine
 * @description okhttp的返回值处理
 * @date 2024/3/15 12:24:30
 */
@FunctionalInterface
public interface OkHttpResponseHandler<T> {

    /**
     * 处理返回值
     * */
    T handle(Response response) throws IOException;
}
