package top.rainine.homebangumi.core.metadata.tmdb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.common.utils.GsonUtils;
import top.rainine.homebangumi.core.common.net.OkHttpService;
import top.rainine.homebangumi.core.metadata.tmdb.data.TmdbTvSeriesResultsPage;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.io.IOException;
import java.util.Objects;

/**
 * @author zhaoxin
 * @description tmdb组件
 * @date 2024/11/8 18:10:01
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TmdbComponent {

    private final OkHttpService okHttpService;


    private <T> T sendRequest(HttpUrl url, String apiToken, Class<T> responseType) {
        // 构造请求
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", STR."Bearer \{apiToken}")
                .get()
                .build();

        try {
            return okHttpService.sendRequestByProxy(request, response -> {
                if (response.isSuccessful() && Objects.nonNull(response.body())) {
                    return GsonUtils.fromJson(response.body().string(), responseType);
                }

                if (Objects.nonNull(response.body())) {
                    log.warn("[TmdbComponent]request failed, responseBody: {}", response.body().string());
                } else {
                    log.warn("[TmdbComponent]request failed, response body is empty, responseCode: {}", response.code());
                }

                throw new HbBizException(HbCodeEnum.REQUEST_TMDB_FAILED);
            });
        } catch (IOException e) {
            log.error("[TmdbComponent]request failed, url: {}", url, e);
            throw new HbBizException(HbCodeEnum.REQUEST_TMDB_FAILED);
        }
    }

    /**
     * 搜索tv
     * */
    public TmdbTvSeriesResultsPage searchTv(String domain, String apiToken, String query) {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(STR."\{domain}" + "/3/search/tv"))
                .newBuilder()
                .addQueryParameter("query", query)
                .build();

        return sendRequest(url, apiToken, TmdbTvSeriesResultsPage.class);
    }
}
