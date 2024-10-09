package top.rainine.homebangumi.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import top.rainine.homebangumi.api.FilePreViewApi;
import top.rainine.homebangumi.api.req.PreViewFilesReq;
import top.rainine.homebangumi.api.resp.PreViewFilesResp;
import top.rainine.homebangumi.api.resp.Result;
import top.rainine.homebangumi.core.common.file.FilePreViewService;

/**
 * @authoer rainine
 * @date 2024/10/9 23:19
 * @desc
 */
@RestController
@RequiredArgsConstructor
public class FilePreViewController implements FilePreViewApi {

    private final FilePreViewService filePreViewService;

    @Override
    public Result<PreViewFilesResp> preViewFiles(PreViewFilesReq req) {
        return Result.createSuccess(filePreViewService.preViewFiles(req));
    }
}
