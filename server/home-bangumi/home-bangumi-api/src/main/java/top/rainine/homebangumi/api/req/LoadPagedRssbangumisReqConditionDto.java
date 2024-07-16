package top.rainine.homebangumi.api.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rainine.homebangumi.api.annotation.valid.ValidEnumValue;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.enums.RssBangumiStatusEnum;
import top.rainine.homebangumi.def.enums.RssCategoryEnum;
import top.rainine.homebangumi.def.enums.RssHandleMethodEnum;

/**
 * @authoer rainine
 * @date 2024/4/29 22:59
 * @desc 获取分页的rss bangeumi请求
 */
@Getter
@Setter
@ToString
public class LoadPagedRssbangumisReqConditionDto {

    /**
     * rss链接名，支持模糊查询
     * */
    private String rssName;

    /**
     * 番剧标题，支持模糊查询
     * */
    private String bangumiTitle;

    /**
     * rss链接分类
     * */
    @ValidEnumValue(enumClass = RssCategoryEnum.class, message = HbCodeEnum.ErrorCode.RSS_CATEGORY_INVALID)
    private Integer rssCategory;

    /**
     * rss链接的收集方式
     * */
    @ValidEnumValue(enumClass = RssHandleMethodEnum.class, message = HbCodeEnum.ErrorCode.RSS_HANDLE_METHOD_INVALID)
    private Integer handleMethod;

    /**
     * rss bangumi状态
     * */
    @ValidEnumValue(enumClass = RssBangumiStatusEnum.class, message = HbCodeEnum.ErrorCode.RSS_HANDLE_METHOD_INVALID)
    private Integer status;
}
