package top.rainine.homebangumi.core.common.file;

import top.rainine.homebangumi.api.req.PreViewFilesReq;
import top.rainine.homebangumi.api.resp.PreViewFilesResp;

/**
 * @authoer rainine
 * @date 2024/10/9 23:22
 * @desc
 */
public interface FilePreViewService {

    PreViewFilesResp preViewFiles(PreViewFilesReq req);
}
