package top.rainine.homebangumi.core.common.net;

import okhttp3.Request;

import java.io.IOException;

/**
 * @authoer rainine
 * @date 2024/3/15 00:08
 * @desc
 */
public interface OkHttpService {

    /**
     * 初始化代理客户端
     * */
    void initProxyClient();

    /**
     * 发送请求
     * @param request 请求
     * @param handleResponseFunc 处理response转化为对应的响应
     * @param <T> 调用方需要的响应类型
     * @return 返回调用方需要的响应
     * */
    <T> T sendRequest(Request request, OkHttpResponseHandler<T> handleResponseFunc) throws IOException;

    /**
     * 通过代理发送请求
     * 如果代理未启用，那么将不通过代理
     * @param request 请求
     * @param handleResponseFunc 处理response转化为对应的响应
     * @param <T> 调用方需要的响应类型
     * @return 返回调用方需要的响应
     * */
    <T> T sendRequestByProxy(Request request, OkHttpResponseHandler<T> handleResponseFunc) throws IOException;

    /**
     * 带cooke的发送请求
     * @param request 请求
     * @param handleResponseFunc 处理response转化为对应的响应
     * @param <T> 调用方需要的响应类型
     * @return 返回调用方需要的响应
     * */
    <T> T sendRequestByCookie(Request request, OkHttpResponseHandler<T> handleResponseFunc) throws IOException;
}
