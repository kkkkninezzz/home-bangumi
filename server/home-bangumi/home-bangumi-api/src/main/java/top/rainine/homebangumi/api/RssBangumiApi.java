package top.rainine.homebangumi.api;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;

/**
 * @authoer rainine
 * @date 2024/4/7 23:53
 * @desc rss bangumi相关api
 */
@RequestMapping("/api/v1/rss/bangumi")
public interface RssBangumiApi {

    /**
     * 预览 rss bangumi
     * */
    @PostMapping(value = "/preview", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<PreviewRssBangumiResp> previewRssBangumi(@Valid @RequestBody PreviewRssBangumiReq req);

    /**
     * 获取rssbangumi的详情
     * */
    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<RssBangumiDetailResp> getRssBangumiDetail(@PathVariable("id") Long id);

    /**
     * 获取rssbangumi的剧集详情
     * */
    @GetMapping(value = "/detail/{id}/episodes", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<RssBangumiEpisodesResp> getRssBangumiEpisodes(@PathVariable("id") Long id);

    /**
     * 更新rssbangumi，更新成功后返回数据
     * */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<RssBangumiDetailResp> updateRssBangumi(@PathVariable("id") Long id, @Valid @RequestBody RssBangumiUpdateReq req);

    /**
     * 重新解析rss bangumi的剧集信息
     * 注意: 该接口将删除所有已经解析出的剧集信息，并重新进行解析
     * */
    @PostMapping(value = "/{id}/episodes/reparse", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Long> reparseBangumiEpisodes(@PathVariable("id") Long id);


    /**
     * 增量解析rss bangumi的剧集信息
     * 如果番剧状态处于{@link top.rainine.homebangumi.def.enums.RssBangumiStatusEnum#ACTIVE}时，会将解析成功的推送到下载器中
     * */
    @PostMapping(value = "/{id}/episodes/incrementalParse", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Long> incrementalParseBangumiEpisodes(@PathVariable("id") Long id);

    /**
     * 获取分页的rss bangumi 信息
     * */
    @PostMapping(value = "/paged/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<PagedResp<PagedRssbBangumiItemDto>> loadPagedRssBangumis(@Valid @RequestBody PagedReq<LoadPagedRssbangumisReqConditionDto> req);

    /**
     * 启用rssbangumi，同时将解析好的剧集信息推送到下载器
     * */
    @PutMapping(value = "/{id}/active", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> activeRssBangumi(@PathVariable("id") Long id);

    /**
     * 禁用rssbangumi
     * */
    @PutMapping(value = "/{id}/inactive", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> inactiveRssBangumi(@PathVariable("id") Long id);

    /**
     * 删除rssbangumi
     * */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> deleteRssBangumi(@PathVariable("id") Long id, @RequestBody DeleteFileFlagDto flag);

    /**
     * 归档rssbangumi
     * */
    @PutMapping(value = "/{id}/archive", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> archiveRssBangumi(@PathVariable("id") Long id, @RequestBody DeleteFileFlagDto flag);

    /**
     * 推送至下载器
     * */
    @PostMapping(value = "/{id}/download", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> pushToDownloader(@PathVariable("id") Long id);

    /**
     * 重命名剧集标题
     * 只在剧集处理完成后，允许进行重命名
     * */
    @PutMapping(value = "/{id}/episode/{episodeId}/renamed-file-name", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> renameEpisodeFileName(@PathVariable("id") Long id, @PathVariable("episodeId") Long episodeId, @Valid @RequestBody RenameEpisodeFileNameReq req);

    /**
     * 忽略剧集，如果当前剧集被推送至下载器中，那么会尝试从下载器中移除
     * */
    @PutMapping(value = "/{id}/episode/{episodeId}/ignore", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> ignoreEpisode(@PathVariable("id") Long id, @PathVariable("episodeId") Long episodeId, @RequestBody DeleteFileFlagDto flag);

    /**
     * 删除剧集，如果当前剧集被推送至下载器中，那么会尝试从下载器中移除
     * */
    @DeleteMapping(value = "/{id}/episode/{episodeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> deleteEpisode(@PathVariable("id") Long id, @PathVariable("episodeId") Long episodeId, @RequestBody DeleteFileFlagDto flag);

    /**
     * 手动解析剧集，如果手动解析成功，将尝试推送至下载器中
     * */
    @PutMapping(value = "/{id}/episode/{episodeId}/manual-parse", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> manualParseEpisode(@PathVariable("id") Long id, @PathVariable("episodeId") Long episodeId, @RequestBody @Valid ManualParseEpisodeReq req);

    /**
     * 在首页的统计信息
     * */
    @GetMapping(value = "/statistic/home", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<RssBangumiStatisticOnHomeResp> statisticOnHome();


    /**
     * 获取一周内更新的番剧
     * */
    @GetMapping(value = "/updated-bangumis/week", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<UpdatedBangumisResp> getUpdatedBangumisForWeek();
}











