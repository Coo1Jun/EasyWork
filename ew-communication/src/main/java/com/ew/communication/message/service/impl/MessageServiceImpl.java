package com.ew.communication.message.service.impl;

import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.client.server.service.IServerClientService;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.api.utils.UserUtils;
import cn.edu.hzu.common.entity.SsoUser;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ew.communication.contact.constants.ContactType;
import com.ew.communication.message.constants.MessageType;
import com.ew.communication.message.dto.*;
import com.ew.communication.message.entity.Message;
import com.ew.communication.message.mapper.MessageMapper;
import com.ew.communication.message.service.IMessageService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import cn.edu.hzu.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.edu.hzu.common.api.PageResult;

import java.util.*;

import org.springframework.transaction.annotation.Transactional;


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
    @Autowired
    private IServerClientService serverClientService;

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
    public List<MessageDto> historyList(MessageQueryParam queryParam) {
        if (StringUtils.isEmpty(queryParam.getToContactId())) {
            throw CommonException.builder().resultCode(CommonErrorEnum.PARAM_IS_EMPTY.setParams(new Object[]{"窗口id"})).build();
        }
        if (ContactType.GROUP.equals(queryParam.getContactType())) {
            return getGroupChatHistory(queryParam);
        }
        SsoUser curUser = UserUtils.getCurrentUser();
        LambdaQueryWrapper<Message> wrapper = Wrappers.<Message>lambdaQuery();
        wrapper.eq(StringUtils.isNotEmpty(queryParam.getToContactId()),
                Message::getToContactId, queryParam.getToContactId())
                .eq(Message::getFromUserId, curUser.getUserid())
                .or(q -> q.eq(Message::getToContactId, curUser.getUserid())
                        .eq(StringUtils.isNotEmpty(queryParam.getToContactId()),
                                Message::getFromUserId, queryParam.getToContactId()))
                .orderByDesc(Message::getSendTime);
        PageResult<Message> page = this.page(queryParam, wrapper);
        List<MessageDto> result = new ArrayList<>();
        if (page != null) {
            List<Message> records = page.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                Map<String, MessageUser> userMap = getUserMap(queryParam.getToContactId(), curUser.getUserid());
                // 倒叙遍历，这样才是按时间正序排列
                for (int i = records.size() - 1; i >= 0; i--) {
                    Message m = records.get(i);
                    MessageDto dto = messageParamMapper.entity2Dto(m);
                    dto.setFromUser(userMap.get(m.getFromUserId()));
                    dto.setToContactId(queryParam.getToContactId());
                    result.add(dto);
                }
                // 异步 更新消息的状态为succeed
                ThreadUtil.execAsync(() -> {
                    log.info("============异步更新start,发送方->【{}】,接收方->【{}】，状态改为succeed",
                            queryParam.getToContactId(), curUser.getUserid());
                    this.update(Wrappers.<Message>lambdaUpdate().
                            set(Message::getStatus, MessageType.SUCCEED) // 设置状态为succeed
                            .eq(Message::getStatus, MessageType.UNREAD) // 条件是状态为unread的
                            .eq(Message::getFromUserId, queryParam.getToContactId()) // 当前窗口发给自己的消息
                            .eq(Message::getToContactId, curUser.getUserid()));
                    log.info("==============异步更新end,发送方->【{}】,接收方->【{}】，状态改为succeed",
                            queryParam.getToContactId(), curUser.getUserid());
                });
            }
        }
        return result;
    }

    private Map<String, MessageUser> getUserMap(String otherUserId, String fromUserId) {
        MessageUser fromUser = getMessageUser(fromUserId);
        MessageUser otherUser = getMessageUser(otherUserId);
        Map<String, MessageUser> map = new HashMap<>();
        map.put(fromUserId, fromUser);
        map.put(otherUserId, otherUser);
        return map;
    }

    /**
     * 根据用户id获取用户信息
     */
    private MessageUser getMessageUser(String userId) {
        MessageUser user = new MessageUser();
        UserDto userDto = serverClientService.getUserDtoById(userId);
        user.setId(userId);
        user.setDisplayName(userDto.getRealName());
        user.setAvatar(userDto.getPortrait());
        return user;
    }

    /**
     * 获取群聊的聊天记录
     * @param queryParam 查询参数
     * @return
     */
    private List<MessageDto> getGroupChatHistory(MessageQueryParam queryParam) {
        LambdaQueryWrapper<Message> wrapper = Wrappers.<Message>lambdaQuery();
        wrapper.eq(Message::getToContactId, queryParam.getToContactId()).orderByDesc(Message::getSendTime);
        PageResult<Message> page = this.page(queryParam, wrapper);
        List<MessageDto> result = new ArrayList<>();
        if (page != null) {
            List<Message> records = page.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                Map<String, MessageUser> userMap = new HashMap<>();
                // 倒叙遍历，这样才是按时间正序排列
                for (int i = records.size() - 1; i >= 0; i--) {
                    Message m = records.get(i);
                    MessageDto dto = messageParamMapper.entity2Dto(m);
                    if (userMap.get(m.getFromUserId()) == null) {
                        userMap.put(m.getFromUserId(), getMessageUser(m.getFromUserId()));
                    }
                    dto.setFromUser(userMap.get(m.getFromUserId()));
                    dto.setToContactId(queryParam.getToContactId());
                    result.add(dto);
                }
            }
        }
        return result;
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
