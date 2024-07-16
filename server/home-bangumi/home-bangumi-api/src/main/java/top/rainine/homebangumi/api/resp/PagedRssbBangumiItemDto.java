package top.rainine.homebangumi.api.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import top.rainine.homebangumi.api.common.BangumiInfoDto;
import top.rainine.homebangumi.def.enums.RssBangumiStatusEnum;
import top.rainine.homebangumi.def.enums.RssCategoryEnum;
import top.rainine.homebangumi.def.enums.RssHandleMethodEnum;

/**
 * @authoer rainine
 * @date 2024/4/29 23:17
 * @desc
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PagedRssbBangumiItemDto {

    /**
     * rss bangumi id
     * */
    private Long id;

    /**
     * rss链接名
     * */
    private String rssName;

    /**
     * 链接类型
     * @see RssCategoryEnum#getCategory()
     * */
    private Integer rssCategory;

    /**
     * 处理方式
     * @see RssHandleMethodEnum#getMethod()
     * */
    private Integer handleMethod;

    /**
     * rss bangumi 状态
     * @see RssBangumiStatusEnum#getStatus()
     * */
    private Integer status;

    /**
     * 番剧信息
     * */
    private BangumiInfoDto bangumiInfo;
}
