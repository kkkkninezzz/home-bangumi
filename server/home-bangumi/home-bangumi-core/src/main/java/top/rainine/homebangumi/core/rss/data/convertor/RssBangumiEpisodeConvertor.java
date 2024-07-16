package top.rainine.homebangumi.core.rss.data.convertor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import top.rainine.homebangumi.api.resp.RssBangumiEpisodeDto;
import top.rainine.homebangumi.core.rss.data.RssBangumiEpisodeParsedInfo;
import top.rainine.homebangumi.core.rss.data.RssBangumiEpisodePreviewInfo;
import top.rainine.homebangumi.dao.po.HbRssBangumiEpisode;
import top.rainine.homebangumi.def.enums.RssBangumiEpisodeStatusEnum;

/**
 * @author rainine
 * @description 数据转换类
 * @date 2024/3/19 15:31:26
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RssBangumiEpisodeConvertor {

    @Mapping(target = "bangumiTitle", ignore = true)
    @Mapping(target = "torrentStoredPath", ignore = true)
    @Mapping(target = "episodeFileName", ignore = true)
    @Mapping(target = "season", ignore = true)
    @Mapping(target = "renamedEpisodeFileName", ignore = true)
    @Mapping(target = "episodeNo", ignore = true)
    @Mapping(target = "status", source = "status")
    RssBangumiEpisodePreviewInfo toRssBangumiEpisodePreviewInfo(RssBangumiEpisodeParsedInfo episodeParsedInfo, RssBangumiEpisodeStatusEnum status);

    /**
     * 转换为预览信息
     * */
    RssBangumiEpisodePreviewInfo toRssBangumiEpisodePreviewInfo(RssBangumiEpisodePreviewInfo source);

    /**
     * 转换为预览信息
     * */
    @Mapping(target = "status", source = "status")
    RssBangumiEpisodePreviewInfo toRssBangumiEpisodePreviewInfo(RssBangumiEpisodePreviewInfo source, RssBangumiEpisodeStatusEnum status);

    @Mappings({
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "renamedEpisodeStoredPath", ignore = true),
            @Mapping(target = "episodeStoredPath", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "lastModifiedTime", ignore = true),
            @Mapping(target = "rssBangumiId", ignore = true),
            @Mapping(target = "torrentIdentity", ignore = true),
            @Mapping(target = "status", expression = "java(info.status().getStatus())"),
            @Mapping(target = "errorMessage", ignore = true),
            @Mapping(target = "downloaderCategory", ignore = true)
    })
    HbRssBangumiEpisode toHbRssBangumiEpisode(RssBangumiEpisodePreviewInfo info);

    RssBangumiEpisodeDto toRssBangumiEpisodeDto(HbRssBangumiEpisode episode);
}















