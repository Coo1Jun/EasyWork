package com.ew.chat.message.service.impl;

import com.ew.chat.message.entity.Message;
import com.ew.chat.message.mapper.MessageMapper;
import com.ew.chat.message.service.IMessageService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.chat.message.dto.MessageQueryParam;
import com.ew.chat.message.dto.MessageAddParam;
import com.ew.chat.message.dto.MessageEditParam;
import com.ew.chat.message.dto.MessageParamMapper;
import com.ew.chat.message.dto.MessageDto;
import cn.edu.hzu.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.ResultCode;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-19
 */
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = {Exception.class, Error.class})
public class MessageServiceImpl extends BaseServiceImpl<MessageMapper, Message> implements IMessageService {

    @Autowired
    private MessageParamMapper messageParamMapper;

    @Override
    public PageResult<MessageDto> pageDto(MessageQueryParam messageQueryParam) {
        Wrapper<Message> wrapper = getPageSearchWrapper(messageQueryParam);
        PageResult<MessageDto> result = messageParamMapper.pageEntity2Dto(page(messageQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(MessageAddParam messageAddParam) {
        Message message = messageParamMapper.addParam2Entity(messageAddParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,message);
        return save(message);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(MessageEditParam messageEditParam) {
        Message message = messageParamMapper.editParam2Entity(messageEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,message);
        return updateById(message);
    }

    @Override
    public MessageDto getDtoById(String id) {
        return messageParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<MessageDto> rows) {
        return saveBatch(messageParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<Message> getPageSearchWrapper(MessageQueryParam messageQueryParam) {
        LambdaQueryWrapper<Message> wrapper = Wrappers.<Message>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(Message.class)) {
            wrapper.orderByDesc(Message::getUpdateTime, Message::getCreateTime);
        }
        return wrapper;
    }
}
