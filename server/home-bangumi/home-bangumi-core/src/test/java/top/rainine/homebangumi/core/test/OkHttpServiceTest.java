package top.rainine.homebangumi.core.test;

import okhttp3.Request;
import okhttp3.ResponseBody;
import top.rainine.homebangumi.core.settings.NetProxySettingsService;
import top.rainine.homebangumi.core.settings.impl.NetProxySettingsServiceImpl;
import top.rainine.homebangumi.core.net.OkHttpResponseHandler;
import top.rainine.homebangumi.core.net.impl.OkHttpServiceImpl;

import java.io.IOException;

/**
 * @author rainine
 * @description
 * @date 2024/3/15 12:17:28
 */
public class OkHttpServiceTest {
    public static void main(String[] args) throws IOException {
        NetProxySettingsService netProxySettingsService = new NetProxySettingsServiceImpl(null, null);
        OkHttpServiceImpl okHttpService = new OkHttpServiceImpl(netProxySettingsService);

        okHttpService.afterPropertiesSet();

        String url = "https://mikanani.me/RSS/Bangumi?bangumiId=3240&subgroupid=203";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        OkHttpResponseHandler<String> handleResponseFunc = response -> {
            ResponseBody body = response.body();
            return body.string();
        };

//        String body = okHttpService.sendRequest(request, handleResponseFunc);
        String body = okHttpService.sendRequestByProxy(request, handleResponseFunc);
        System.out.println(body);
    }

}
