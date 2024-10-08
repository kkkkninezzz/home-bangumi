package top.rainine.homebangumi.core.message.data.convertor;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.rainine.homebangumi.api.resp.MessageDto;
import top.rainine.homebangumi.dao.po.HbMessage;

/**
 * @authoer rainine
 * @date 2024/5/19 14:54
 * @desc
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageConvertor {

    MessageDto toMessageDto(HbMessage entity);
}
