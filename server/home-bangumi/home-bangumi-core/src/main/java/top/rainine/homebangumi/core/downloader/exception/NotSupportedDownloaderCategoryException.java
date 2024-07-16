package top.rainine.homebangumi.core.downloader.exception;

import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

/**
 * @authoer rainine
 * @date 2024/5/10 23:48
 * @desc 不支持的下载器类型
 */
public class NotSupportedDownloaderCategoryException extends HbBizException {

    public NotSupportedDownloaderCategoryException() {
        super(HbCodeEnum.NOT_SUPPORTED_DOWNLOADER_CATEGORY);
    }
}
