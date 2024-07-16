package top.rainine.homebangumi.core.rss.data.convertor;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import top.rainine.homebangumi.api.common.BangumiInfoDto;
import top.rainine.homebangumi.api.req.PreviewRssBangumiReq;
import top.rainine.homebangumi.api.req.RssBangumiUpdateReq;
import top.rainine.homebangumi.api.resp.PagedRssbBangumiItemDto;
import top.rainine.homebangumi.api.resp.RssBangumiDetailResp;
import top.rainine.homebangumi.core.rss.data.RssBangumiPreviewInfo;
import top.rainine.homebangumi.dao.po.HbBangumi;
import top.rainine.homebangumi.dao.po.HbRssBangumi;

/**
 * @authoer rainine
 * @date 2024/4/9 00:08
 * @desc rss bangumi的转换器
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RssBangumiConvertor {

    @Mappings({
            @Mapping(target = "bangumiId", ignore = true),
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "lastModifiedTime", ignore = true),
            @Mapping(target = "status", expression = "java(top.rainine.homebangumi.def.enums.RssBangumiStatusEnum.INACTIVE.getStatus())"),
            @Mapping(target = "filterRules", expression = "java(top.rainine.homebangumi.common.utils.GsonUtils.toJson(req.getFilterRules()))"),
    })
    HbRssBangumi toHbRssBangumi(PreviewRssBangumiReq req);

    @Mappings({
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "lastModifiedTime", ignore = true)
    })
    HbBangumi toHbBangumi(RssBangumiPreviewInfo previewInfo);

    BangumiInfoDto toBangumiInfoDto(HbBangumi hbBangumi);

    /**
     * 转换为详情rsp
     * */
    @Mappings({
            @Mapping(target = "filterRules", expression = "java(top.rainine.homebangumi.common.utils.GsonUtils.toList(hbRssBangumi.getFilterRules(), String.class))"),
    })
    RssBangumiDetailResp toRssBangumiDetailResp(HbRssBangumi hbRssBangumi, BangumiInfoDto bangumiInfo);

    /**
     * 更新rss bangumi字段信息
     * */
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "rssLink", ignore = true)
    @Mapping(target = "rssCategory", ignore = true)
    @Mapping(target = "lastModifiedTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "handleMethod", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "bangumiId", ignore = true)
    @Mapping(target = "filterRules", expression = "java(top.rainine.homebangumi.common.utils.GsonUtils.toJson(req.getFilterRules()))")
    void updateRssBangumi(@MappingTarget HbRssBangumi hbRssBangumi, RssBangumiUpdateReq req);

    /**
     * 更新bangumi info的信息
     * */
    @Mapping(target = "posterStoredPath", ignore = true)
    @Mapping(target = "lastModifiedTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    void updateBangumi(@MappingTarget HbBangumi hbBangumi, BangumiInfoDto bangumiInfoDto);

    /**
     * 转换为详情rsp
     * */
    PagedRssbBangumiItemDto toPagedRssbBangumiItemDto(HbRssBangumi hbRssBangumi, BangumiInfoDto bangumiInfo);
}














