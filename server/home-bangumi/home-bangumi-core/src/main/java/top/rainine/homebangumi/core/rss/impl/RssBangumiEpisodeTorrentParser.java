package top.rainine.homebangumi.core.rss.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import top.rainine.homebangumi.common.data.TorrentInfo;
import top.rainine.homebangumi.common.utils.BencodeUtils;
import top.rainine.homebangumi.core.common.net.OkHttpService;
import top.rainine.homebangumi.core.rss.data.RssBangumiEpisodeParsedInfo;
import top.rainine.homebangumi.core.rss.data.RssBangumiEpisodePreviewInfo;
import top.rainine.homebangumi.core.common.episoderenamer.EpisodeTitleRenameAdapter;
import top.rainine.homebangumi.core.common.titleparser.EpisodeTitleParser;
import top.rainine.homebangumi.core.common.titleparser.data.EpisodeTitleInfo;
import top.rainine.homebangumi.def.enums.RssBangumiEpisodeStatusEnum;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @authoer rainine
 * @date 2024/3/18 23:12
 * @desc rss番剧剧集种子解析
 */
@Slf4j
@RequiredArgsConstructor
public class RssBangumiEpisodeTorrentParser {

    private final OkHttpService okHttpService;

    private final EpisodeTitleParser episodeTitleParser;

    private final EpisodeTitleRenameAdapter episodeTitleRenameAdapter;

    /**
     * 根据种子进行解析
     * @param parsedInfo 根据rss解析出来的信息
     * @param torrentStoredPath 种子的存储路径
     * */
    public RssBangumiEpisodePreviewInfo parse(RssBangumiEpisodeParsedInfo parsedInfo, Path torrentStoredPath, Integer season) {
        RssBangumiEpisodePreviewInfo.RssBangumiEpisodePreviewInfoBuilder builder = RssBangumiEpisodePreviewInfo
                .builder()
                .rawEpisodeTitle(parsedInfo.rawEpisodeTitle())
                .torrentPubDate(parsedInfo.torrentPubDate())
                .torrentLink(parsedInfo.torrentLink());

        byte[] torrentBytes;
        try {
            torrentBytes = downloadTorrent(parsedInfo.torrentLink());
            builder.torrentStoredPath(torrentStoredPath.toString());
        } catch (IOException e) {
            log.error("[RssBangumiEpisodeTorrentParser]download torrent failed, torrentLink: {}", parsedInfo.torrentLink());

            return builder
                    .status(RssBangumiEpisodeStatusEnum.TORRENT_DOWNLOAD_FAILED)
                    .build();
        }

        if (ArrayUtils.isEmpty(torrentBytes)) {
            log.error("[RssBangumiEpisodeTorrentParser]torrent is empty, torrentLink: {}", parsedInfo.torrentLink());

            return builder
                    .status(RssBangumiEpisodeStatusEnum.TORRENT_DOWNLOAD_FAILED)
                    .build();
        }

        // 解析种子信息
        TorrentInfo torrentInfo;
        try {
            torrentInfo = BencodeUtils.INSTANCE.parseTorrent(torrentBytes);
        } catch (Exception e) {
            log.error("[RssBangumiEpisodeTorrentParser]parse torrent failed, torrentLink: {}", parsedInfo.torrentLink());

            return builder
                    .status(RssBangumiEpisodeStatusEnum.TORRENT_PARSE_FAILED)
                    .build();
        }

        // 将种子保存在本地
        try {
            // 如果种子不存在，才实际写入
            if (Files.notExists(torrentStoredPath)) {
                Files.write(torrentStoredPath, torrentBytes);
            }
        } catch (IOException e) {
            log.error("[RssBangumiEpisodeTorrentParser]store torrent failed, torrentLink: {}, storedPath: {}",
                    parsedInfo.torrentLink(), torrentStoredPath);

            return builder
                    .status(RssBangumiEpisodeStatusEnum.TORRENT_PARSE_FAILED)
                    .build();
        }

        String episodeFileName = torrentInfo.getResourceName();
        builder.episodeFileName(episodeFileName);

        EpisodeTitleInfo episodeTitleInfo;
        try {
            episodeTitleInfo = episodeTitleParser.parseTitle(episodeFileName, season);
        } catch (Exception e) {
            log.error("[RssBangumiEpisodeTorrentParser]parse title failed, torrentLink: {}, episodeFileName: {}",
                    parsedInfo.torrentLink(), episodeFileName);
            return builder
                    .status(RssBangumiEpisodeStatusEnum.TITLE_PARSE_FAILED)
                    .build();
        }

        String renamedTitle = episodeTitleRenameAdapter.renameTitle(episodeFileName, episodeTitleInfo);
        String fileExtension = FilenameUtils.getExtension(episodeFileName);
        String renamedTitleFileName;
        if (StringUtils.isNotBlank(fileExtension)) {
            renamedTitleFileName = STR."\{renamedTitle}.\{FilenameUtils.getExtension(episodeFileName)}";
        } else {
            renamedTitleFileName = renamedTitle;
        }

        return builder.episodeNo(episodeTitleInfo.episode())
                .season(episodeTitleInfo.season())
                .bangumiTitle(episodeTitleInfo.title())
                .status(RssBangumiEpisodeStatusEnum.PARSED)
                .renamedEpisodeFileName(renamedTitleFileName)
                .build();

    }

    /**
     * 下载种子
     * */
    private byte[] downloadTorrent(String torrentLink) throws IOException {
        Request request = new Request.Builder()
                .url(torrentLink)
                .get()
                .build();

        return okHttpService.sendRequestByProxy(request, response -> {
            ResponseBody body = response.body();
            if (Objects.isNull(body)) {
                throw new IOException();
            }

            return body.bytes();
        });

    }


}














