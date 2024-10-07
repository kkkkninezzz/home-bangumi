package top.rainine.homebangumi.core.rename.episode.rename.data.convertor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import top.rainine.homebangumi.api.req.CreateEpisodeRenameTaskReq;
import top.rainine.homebangumi.api.req.UpdateEpisodeRenameTaskReq;
import top.rainine.homebangumi.api.resp.EpisodeRenameTaskDetailResp;
import top.rainine.homebangumi.api.resp.EpisodeRenameTaskItemDto;
import top.rainine.homebangumi.api.resp.PagedEpisodeRenameTaskItemDto;
import top.rainine.homebangumi.core.rename.episode.rename.data.EpisodeRenameTaskItemParsedInfo;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTask;
import top.rainine.homebangumi.dao.po.HbEpisodeRenameTaskItem;

/**
 * @author rainine
 * @description
 * @date 2024/7/19 15:40:11
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EpisodeRenameTaskConvertor {

    @Mapping(target = "filteredOutRules", expression = "java(top.rainine.homebangumi.common.utils.GsonUtils.toJson(req.getFilteredOutRules()))")
    @Mapping(target = "taskStatus", expression = "java(top.rainine.homebangumi.def.enums.EpisodeRenameTaskStatusEnum.NONE.getStatus())")
    @Mapping(target = "lastModifiedTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    HbEpisodeRenameTask toHbEpisodeRenameTask(CreateEpisodeRenameTaskReq req);


    @Mapping(target = "status", expression = "java(info.status().getStatus())")
    @Mapping(target = "renamedEpisodeOutputPath", ignore = true)
    @Mapping(target = "lastModifiedTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "episodePath", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    HbEpisodeRenameTaskItem toHbEpisodeRenameTaskItem(Long taskId, EpisodeRenameTaskItemParsedInfo info);

    @Mapping(target = "filteredOutRules", expression = "java(top.rainine.homebangumi.common.utils.GsonUtils.toList(renameTask.getFilteredOutRules(), String.class))")
    EpisodeRenameTaskDetailResp toEpisodeRenameTaskDetailResp(HbEpisodeRenameTask renameTask);

    EpisodeRenameTaskItemDto toEpisodeRenameTaskItemDto(HbEpisodeRenameTaskItem taskItem);

    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "lastModifiedTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "filteredOutRules", expression = "java(top.rainine.homebangumi.common.utils.GsonUtils.toJson(req.getFilteredOutRules()))")
    void updateHbEpisodeRenameTask(@MappingTarget HbEpisodeRenameTask task, UpdateEpisodeRenameTaskReq req);

    PagedEpisodeRenameTaskItemDto toPagedEpisodeRenameTaskItemDto(HbEpisodeRenameTask task, long totalCount, long pendingCount, long successCount, long failedCount);
}
