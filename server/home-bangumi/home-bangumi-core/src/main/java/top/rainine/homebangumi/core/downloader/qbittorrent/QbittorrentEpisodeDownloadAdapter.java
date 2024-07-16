package top.rainine.homebangumi.core.downloader.qbittorrent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.common.utils.BencodeUtils;
import top.rainine.homebangumi.common.utils.GsonUtils;
import top.rainine.homebangumi.core.settings.DownloaderSettingsService;
import top.rainine.homebangumi.core.settings.data.QbittorrentDownloaderSettings;
import top.rainine.homebangumi.core.downloader.EpisodeDownloadAdapter;
import top.rainine.homebangumi.core.downloader.data.TorrentDownloadStatusInfo;
import top.rainine.homebangumi.core.downloader.data.TorrentFileRenameResultInfo;
import top.rainine.homebangumi.core.net.OkHttpService;
import top.rainine.homebangumi.def.enums.DownloaderCategoryEnum;
import top.rainine.homebangumi.def.enums.TorrentDownloadStatusEnum;
import top.rainine.homebangumi.def.enums.TorrentFileRenameResultEnum;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author rainine
 * @description qbittorrent的下载器
 * @date 2024/3/14 18:19:26
 *
 * Qbittorrent中对种子的唯一标识为种子的hash值
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class QbittorrentEpisodeDownloadAdapter implements EpisodeDownloadAdapter, InitializingBean {

    private final OkHttpService okHttpService;

    private final DownloaderSettingsService downloaderSettingsService;

    private volatile QbittorrentDownloaderSettings qbittorrentDownloaderSettings;

    /**
     * 登录标记，如果登录成功过，设置为true
     * */
    private volatile boolean loginFlag;

    @Override
    public void afterPropertiesSet() throws Exception {
        setDownloaderSettings();
    }

    public void setDownloaderSettings() {
        qbittorrentDownloaderSettings = downloaderSettingsService.getQbittorrentDownloaderSettings();
    }

    @Override
    public DownloaderCategoryEnum getCategory() {
        return DownloaderCategoryEnum.QBITTORRENT;
    }

    /**
     * 登录逻辑
     * */
    private synchronized boolean login() {
        // 并发时调用登录接口，说明已经登陆过
        if (loginFlag) {
            return true;
        }

        String loginApi = QbittorrentApiUrls.getLoginApi(qbittorrentDownloaderSettings.baseUrl());
        // 构造请求体
        RequestBody body = new FormBody.Builder()
                .add("username", qbittorrentDownloaderSettings.username())
                .add("password", qbittorrentDownloaderSettings.password())
                .build();
        // 构造请求
        Request request = new Request.Builder()
                .url(loginApi)
                .post(body)
                .build();

        try {
            this.loginFlag = okHttpService.sendRequestByCookie(request, response -> {
                if (response.isSuccessful() && Objects.nonNull(response.body())) {
                    // 获取响应体
                    String responseBody = response.body().string();
                    return Objects.equals("Ok.", responseBody.trim());
                }
                return false;
            });
        } catch (IOException e) {
            log.error("[QbittorrentEpisodeDownloadAdapter]login failed, url: {}, username: {}, password: {}",
                    loginApi, qbittorrentDownloaderSettings.username(), qbittorrentDownloaderSettings.password(), e);
            this.loginFlag = false;
        }

        return this.loginFlag;
    }

    /**
     * 判断cookie是否有效
     * */
    private boolean isCookieValid() {
        String versionInfoApi = QbittorrentApiUrls.getVersionInfoApi(qbittorrentDownloaderSettings.baseUrl());
        // 构造请求
        Request request = new Request.Builder()
                .url(versionInfoApi)
                .get()
                .build();
        try {
            this.loginFlag = okHttpService.sendRequestByCookie(request, response -> {
                String body = "";
                if (Objects.nonNull(response.body())) {
                    body = response.body().string();
                }
                if (response.isSuccessful() && Objects.nonNull(response.body())) {
                    // 是否是版本号v开头
                    return body.startsWith("v");
                }
                return false;
            });

        } catch (IOException e) {
            log.error("[QbittorrentEpisodeDownloadAdapter]check cookie valid failed, url: {}", versionInfoApi);
            this.loginFlag = false;

        }

        return this.loginFlag;
    }

    @Override
    public boolean auth() {
        // 如果没有获取到下载器配置，或者未配置下载器url，那么视为失败
        if (Objects.isNull(qbittorrentDownloaderSettings) || StringUtils.isBlank(qbittorrentDownloaderSettings.baseUrl())) {
            return false;
        }

        if (!loginFlag || !isCookieValid()) {
            return login();
        }

        return true;
    }

    @Override
    public String addTorrent(String torrentPath, String savePathStr, boolean paused) {
        String addTorrentApi = QbittorrentApiUrls.getAddTorrentApi(qbittorrentDownloaderSettings.baseUrl());
        try {
            // 读取种子文件内容
            File torrentFile = new File(torrentPath);

            String savedPath = EpisodeDownloadAdapter.getPathInDownloader(Paths.get(savePathStr).getParent().toString());
            // 构造请求体
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("savepath", savedPath)
                    .addFormDataPart("paused", String.valueOf(paused))
                    .addFormDataPart("category", "home-bangumi")
                    .addFormDataPart("torrents", torrentFile.getName(), RequestBody.create(torrentFile, MediaType.parse("application/x-bittorrent")));

            RequestBody requestBody = requestBodyBuilder.build();
            // 构造请求
            Request request = new Request.Builder()
                    .url(addTorrentApi)
                    .post(requestBody)
                    .build();
            boolean addResult = okHttpService.sendRequestByCookie(request, response -> {
                String body = "";
                if (Objects.nonNull(response.body())) {
                    body = response.body().string();
                }
                log.info("[QbittorrentEpisodeDownloadAdapter]addTorrent, response code: {}, body: {}", response.code(), body);
                if (response.isSuccessful() && StringUtils.isNotBlank(body)) {
                    return Objects.equals("Ok.", body.trim());
                }
                return false;
            });
            // 如果添加成功，那么返回种子的hash值作为在qb的唯一标识
            if (addResult) {
                return BencodeUtils.INSTANCE.calTorrentHash(Files.readAllBytes(torrentFile.toPath()));
            }
            return null;
        } catch (Exception e) {
            log.error("[QbittorrentEpisodeDownloadAdapter]addTorrent failed, addTorrentApi: {}, torrentPath: {}",
                    addTorrentApi, torrentPath, e);
            return null;
        }
    }

    @Override
    public boolean removeTorrent(String torrentIdentity, boolean deleteFiles) {
        String deleteTorrentApi = QbittorrentApiUrls.getDeleteTorrentApi(qbittorrentDownloaderSettings.baseUrl());

        // 构造请求体
        RequestBody body = new FormBody.Builder()
                .add("hashes", torrentIdentity)
                .add("deleteFiles", String.valueOf(deleteFiles))
                .build();

        // 构造请求
        Request request = new Request.Builder()
                .url(deleteTorrentApi)
                .post(body)
                .build();
        try {
            return okHttpService.sendRequestByCookie(request, Response::isSuccessful);
        } catch (IOException e) {
            log.error("[QbittorrentEpisodeDownloadAdapter]remove torrent failed, url: {}", deleteTorrentApi, e);
            return false;
        }
    }

    @Override
    public TorrentDownloadStatusInfo getTorrentDownloadStatus(String torrentIdentity) {
        String torrentPropertiesApi = QbittorrentApiUrls.getTorrentPropertiesApi(qbittorrentDownloaderSettings.baseUrl());

        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(torrentPropertiesApi))
                .newBuilder()
                .addQueryParameter("hash", torrentIdentity)
                .build();

        // 构造请求
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            return okHttpService.sendRequestByCookie(request, response -> {
                if (response.isSuccessful() && Objects.nonNull(response.body())) {
                    QbTorrentProperties qbTorrentProperties = GsonUtils.toJson(response.body().string(), QbTorrentProperties.class);
                    TorrentDownloadStatusEnum statusEnum;
                    if (Objects.equals(qbTorrentProperties.getPiecesHave(), qbTorrentProperties.getPiecesNum())) {
                        statusEnum = TorrentDownloadStatusEnum.FINISHED;
                    } else {
                        statusEnum = TorrentDownloadStatusEnum.DOWNLOADING;
                    }
                    float progress = Float.valueOf(qbTorrentProperties.getPiecesHave()) / Float.valueOf(qbTorrentProperties.getPiecesNum());
                    return TorrentDownloadStatusInfo
                            .builder()
                            .status(statusEnum)
                            .progress(progress)
                            .build();
                }
                return TorrentDownloadStatusInfo.NOT_FOUND;
            });
        } catch (IOException e) {
            log.error("[QbittorrentEpisodeDownloadAdapter]getTorrentDownloadStatus failed, url: {}", torrentPropertiesApi, e);
            return TorrentDownloadStatusInfo.DEFAULT_ERROR;
        }
    }

    @Override
    public TorrentFileRenameResultInfo renameFile(String torrentIdentity, String oldPath, String newPath) {

        // qb在做文件重命名时，只需要文件名即可
        String oldFile = Paths.get(oldPath).getFileName().toString();
        String newFile = Paths.get(newPath).getFileName().toString();

        String renameFileApi = QbittorrentApiUrls.getRenameFileApi(qbittorrentDownloaderSettings.baseUrl());
        // 构造请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("hash", torrentIdentity)
                .add("oldPath", oldFile)
                .add("newPath", newFile)
                .build();

        // 构造请求
        Request request = new Request.Builder()
                .url(renameFileApi)
                .post(requestBody)
                .build();

        try {
            return okHttpService.sendRequestByCookie(request, response -> {
                if (response.isSuccessful()) {
                    return TorrentFileRenameResultInfo.SUCCESS;
                }

                // 说明文件已经存在
                if (response.code() == 409) {
                    ResponseBody body = response.body();

                    String responseMsg = "";
                    if (Objects.nonNull(body)) {
                        responseMsg = body.string();
                    }
                    return TorrentFileRenameResultInfo.builder().result(TorrentFileRenameResultEnum.FAILED).errorMessage(responseMsg).build();
                }
                return TorrentFileRenameResultInfo.DEFAULT_ERROR;
            });
        } catch (IOException e) {
            log.error("[QbittorrentEpisodeDownloadAdapter]rename file failed, url: {}", renameFileApi, e);
            return TorrentFileRenameResultInfo.DEFAULT_ERROR;
        }
    }

}
