package top.rainine.homebangumi.core.rss.exception;

import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

/**
 * @author rainine
 * @description rss番剧信息解析失败异常
 * @date 2024/3/14 18:56:55
 */
public class RssBangumiParseFailedException extends HbBizException {

    public RssBangumiParseFailedException() {
        super(HbCodeEnum.RSS_BANGUMI_PARSE_FAILED);
    }
}
