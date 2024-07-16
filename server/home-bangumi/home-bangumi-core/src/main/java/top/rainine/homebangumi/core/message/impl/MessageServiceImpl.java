package top.rainine.homebangumi.core.message.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.api.resp.MessagesResp;
import top.rainine.homebangumi.common.utils.HbDateUtils;
import top.rainine.homebangumi.core.message.MessageService;
import top.rainine.homebangumi.core.message.data.convertor.MessageConvertor;
import top.rainine.homebangumi.dao.po.HbMessageEntity;
import top.rainine.homebangumi.dao.po.QHbMessageEntity;
import top.rainine.homebangumi.dao.repository.HbMessageRepository;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.enums.MessageCategoryEnum;
import top.rainine.homebangumi.def.enums.MessageTypeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @authoer rainine
 * @date 2024/5/19 14:45
 * @desc
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final HbMessageRepository messageRepository;

    private final JPAQueryFactory jpaQueryFactory;

    private final MessageConvertor messageConvertor;

    @Override
    public void addMessage(MessageCategoryEnum category, MessageTypeEnum type, String title, String content, String subjectId) {
        HbMessageEntity entity = new HbMessageEntity();
        entity.setCategory(category.getCategory());
        entity.setType(type.getType());
        entity.setTitle(title);
        entity.setContent(content);
        entity.setSubjectId(subjectId);
        entity.setRead(false);

        messageRepository.save(entity);
    }

    @Override
    public MessagesResp loadMessages(Integer daysAgo, Integer limit) {
        LocalDateTime startTime = HbDateUtils.now().plusDays(-daysAgo);
        List<HbMessageEntity> entities = messageRepository.findAllByReadAndCreatedTimeGreaterThanOrderByCreatedTimeDesc(Boolean.FALSE, HbDateUtils.toMills(startTime), PageRequest.of(0, limit));

        MessagesResp resp = new MessagesResp();resp.setMessages(entities.stream().map(messageConvertor::toMessageDto).toList());


        return resp;
    }

    @Override
    public void readMessage(Long id) {
        HbMessageEntity messageEntity = messageRepository.findById(id).orElseThrow(() -> new HbBizException(HbCodeEnum.MESSAGE_NOT_EXISTS));
        if (messageEntity.getRead()) {
            return;
        }

        messageEntity.setRead(true);
        messageRepository.save(messageEntity);
    }

    @Override
    public void readAllMessages() {
        QHbMessageEntity qHbMessageEntity = QHbMessageEntity.hbMessageEntity;
        jpaQueryFactory.update(qHbMessageEntity).set(qHbMessageEntity.read, true)
                .where(qHbMessageEntity.read.eq(false))
                .execute();
    }
}


























