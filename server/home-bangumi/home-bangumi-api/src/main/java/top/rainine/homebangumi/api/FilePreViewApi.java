package top.rainine.homebangumi.api;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import top.rainine.homebangumi.api.req.PreViewFilesReq;
import top.rainine.homebangumi.api.resp.IsEmptyDirResp;
import top.rainine.homebangumi.api.resp.PreViewFilesResp;
import top.rainine.homebangumi.api.resp.Result;

/**
 * @authoer rainine
 * @date 2024/10/7 21:20
 * @desc 文件预览api
 */
@RequestMapping("/api/v1/file/pre-view")
public interface FilePreViewApi {

    /**
     * 预览文件
     * */
    @PostMapping(value = "/files", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<PreViewFilesResp> preViewFiles(@Valid @RequestBody PreViewFilesReq req);

    /**
     * 是否为空目录
     * */
    @PostMapping(value = "/is-empty-dir", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<IsEmptyDirResp> isEmptyDir(@Valid @RequestBody PreViewFilesReq req);
}
