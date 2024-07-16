package top.rainine.homebangumi.web.controller;

import org.springframework.web.bind.annotation.RestController;
import top.rainine.homebangumi.api.RssBangumiApi;
import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;
import top.rainine.homebangumi.core.rss.RssBangumiService;

/**
 * @author rainine
 * @description rss bangumi相关的接口
 * @date 2024/4/8 18:21:48
 */
@RestController
public class RssBangumiController implements RssBangumiApi {

    private final RssBangumiService rssBangumiService;

    public RssBangumiController(RssBangumiService rssBangumiService) {
        this.rssBangumiService = rssBangumiService;
    }

    @Override
    public Result<PreviewRssBangumiResp> previewRssBangumi(PreviewRssBangumiReq req) {
        return Result.createSuccess(rssBangumiService.previewAndCreateRssBangumi(req));
    }

    @Override
    public Result<RssBangumiDetailResp> getRssBangumiDetail(Long id) {
        return Result.createSuccess(rssBangumiService.getRssBangumiDetail(id));
    }

    @Override
    public Result<RssBangumiEpisodesResp> getRssBangumiEpisodes(Long id) {
        return Result.createSuccess(rssBangumiService.getRssBangumiEpisodes(id));
    }

    @Override
    public Result<RssBangumiDetailResp> updateRssBangumi(Long id, RssBangumiUpdateReq req) {
        return Result.createSuccess(rssBangumiService.updateRssBangumi(id, req));
    }

    @Override
    public Result<Long> reparseBangumiEpisodes(Long id) {
        return Result.createSuccess(rssBangumiService.reparseRssBangumiEpisodes(id));
    }

    @Override
    public Result<PagedResp<PagedRssbBangumiItemDto>> loadPagedRssBangumis(PagedReq<LoadPagedRssbangumisReqConditionDto> req) {
        return Result.createSuccess(rssBangumiService.loadPagedRssBangumis(req));
    }

    @Override
    public Result<Void> activeRssBangumi(Long id) {
        rssBangumiService.activeRssBangumi(id);
        return Result.createSuccess();
    }

    @Override
    public Result<Void> inactiveRssBangumi(Long id) {
        rssBangumiService.inactiveRssBangumi(id);
        return Result.createSuccess();
    }

    @Override
    public Result<Void> deleteRssBangumi(Long id, DeleteFileFlagDto flag) {
        rssBangumiService.deleteRssBangumi(id, flag.getDeleteFile());
        return Result.createSuccess();
    }

    @Override
    public Result<Void> archiveRssBangumi(Long id, DeleteFileFlagDto flag) {
        rssBangumiService.archiveRssBangumi(id, flag.getDeleteFile());
        return Result.createSuccess();
    }

    @Override
    public Result<Long> incrementalParseBangumiEpisodes(Long id) {
        return Result.createSuccess(rssBangumiService.incrementalParseBangumiEpisodes(id));
    }

    @Override
    public Result<Void> pushToDownloader(Long id) {
        rssBangumiService.pushToDownloader(id);
        return Result.createSuccess();
    }

    @Override
    public Result<Void> renameEpisodeFileName(Long id, Long episodeId, RenameEpisodeFileNameReq req) {
        rssBangumiService.renameEpisodeFileName(id, episodeId, req);
        return Result.createSuccess();
    }

    @Override
    public Result<Void> ignoreEpisode(Long id, Long episodeId, DeleteFileFlagDto flag) {
        rssBangumiService.ignoreEpisode(id, episodeId, flag.getDeleteFile());
        return Result.createSuccess();
    }

    @Override
    public Result<Void> deleteEpisode(Long id, Long episodeId, DeleteFileFlagDto flag) {
        rssBangumiService.deleteEpisode(id, episodeId, flag.getDeleteFile());
        return Result.createSuccess();
    }

    @Override
    public Result<Void> manualParseEpisode(Long id, Long episodeId, ManualParseEpisodeReq req) {
        rssBangumiService.manualParseEpisode(id, episodeId, req);
        return Result.createSuccess();
    }

    @Override
    public Result<RssBangumiStatisticOnHomeResp> statisticOnHome() {
        return Result.createSuccess(rssBangumiService.statisticOnHome());
    }

    @Override
    public Result<UpdatedBangumisResp> getUpdatedBangumisForWeek() {
        return Result.createSuccess(rssBangumiService.getUpdatedBangumisForWeek());
    }
}
