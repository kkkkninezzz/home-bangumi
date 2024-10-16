package top.rainine.homebangumi.core.common.file;

import top.rainine.homebangumi.api.req.PreViewFilesReq;
import top.rainine.homebangumi.api.resp.IsEmptyDirResp;
import top.rainine.homebangumi.api.resp.PreViewFilesResp;

/**
 * @authoer rainine
 * @date 2024/10/9 23:22
 * @desc
 */
public interface FilePreViewService {

    PreViewFilesResp preViewFiles(PreViewFilesReq req);

    /**
     * 是否为空目录，如果不存在，也视为空目录
     * */
    IsEmptyDirResp isEmptyDir(PreViewFilesReq req);
}
