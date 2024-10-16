package top.rainine.homebangumi.core.common.net.impl;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.common.net.OkHttpResponseHandler;
import top.rainine.homebangumi.core.common.net.OkHttpService;
import top.rainine.homebangumi.core.settings.NetProxySettingsService;
import top.rainine.homebangumi.core.settings.data.HttpProxySettings;
import top.rainine.homebangumi.core.settings.data.NetProxySettings;
import top.rainine.homebangumi.core.settings.data.Socks5ProxySettings;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * @authoer rainine
 * @date 2024/3/15 00:12
 * @desc oktthp的实现类
 */
@Service
public class OkHttpServiceImpl implements OkHttpService, InitializingBean {

    /**
     * 基础的client
     * */
    private OkHttpClient baseClient;

    /**
     * 实现了代理的client
     * */
    private OkHttpClient proxyClient;

    /**
     * 实现了cooke管理的client
     * */
    private OkHttpClient cookieClient;

    private final NetProxySettingsService netProxySettingsService;


    @Autowired
    public OkHttpServiceImpl(NetProxySettingsService netProxySettingsService) {
        this.netProxySettingsService = netProxySettingsService;
    }

    private void initBaseClient() {
        this.baseClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)      //设置连接超时
                .readTimeout(30, TimeUnit.SECONDS)         //设置读超时
                .writeTimeout(30, TimeUnit.SECONDS)        //设置写超时
                .retryOnConnectionFailure(true)            //是否自动重连
                .build();
    }

    /**
     * 初始化代理的client
     * */
    @Override
    public synchronized void initProxyClient() {
        this.proxyClient = createProxyClient();
    }

    /**
     * 创建代理客户端
     * */
    private OkHttpClient createProxyClient() {
        NetProxySettings proxySettings = netProxySettingsService.getProxySettings();

        OkHttpClient.Builder builder = baseClient.newBuilder();
        // 如果没有启用代理，那么直接复用baseClient
        if (!proxySettings.enable()) {
            return builder.build();
        }

        switch (proxySettings.proxyType()) {
            case HTTP -> {
                HttpProxySettings httpProxySettings = proxySettings.httpProxySettings();
                builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpProxySettings.host(), httpProxySettings.port())));
            }
            case SOCKS5 -> {
                Socks5ProxySettings socks5ProxySettings = proxySettings.socks5ProxySettings();
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(socks5ProxySettings.host(), socks5ProxySettings.port()));
                builder.proxy(proxy);

                // 如果配置了代理的账号名，那么就设置认证项
                if (StringUtils.isNotBlank(socks5ProxySettings.username())) {
                    final String username = socks5ProxySettings.username();
                    final String password = socks5ProxySettings.password();
                    builder.proxyAuthenticator((route, response) -> {
                        String credential = Credentials.basic(username, password);
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    });
                }
            }
        }

        return builder.build();
    }


    /**
     * 创建带cookie管理的客户端
     * */
    private void initCookieClient() {
        OkHttpClient.Builder builder = baseClient.newBuilder();
        this.cookieClient = builder.cookieJar(new HbCookieJar())
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        initBaseClient();
        initProxyClient();
        initCookieClient();
    }

    @Override
    public <T> T sendRequest(Request request, OkHttpResponseHandler<T> handleResponseFunc) throws IOException {
        return sendRequest(this.baseClient, request, handleResponseFunc);
    }

    @Override
    public <T> T sendRequestByProxy(Request request, OkHttpResponseHandler<T> handleResponseFunc) throws IOException {
        return sendRequest(this.proxyClient, request, handleResponseFunc);
    }

    private <T> T sendRequest(OkHttpClient client, Request request, OkHttpResponseHandler<T> handleResponseFunc) throws IOException {
        try (Response response = client.newCall(request).execute()){
            return handleResponseFunc.handle(response);
        }
    }

    @Override
    public <T> T sendRequestByCookie(Request request, OkHttpResponseHandler<T> handleResponseFunc) throws IOException {
        return sendRequest(this.cookieClient, request, handleResponseFunc);
    }
}













