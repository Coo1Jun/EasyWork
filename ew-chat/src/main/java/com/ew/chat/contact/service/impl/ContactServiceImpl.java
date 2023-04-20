package com.ew.chat.contact.service.impl;

import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.client.server.service.IServerClientService;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.api.utils.UserUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ew.chat.contact.constants.ContactType;
import com.ew.chat.contact.entity.Contact;
import com.ew.chat.contact.mapper.ContactMapper;
import com.ew.chat.contact.service.IContactService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.chat.contact.dto.ContactQueryParam;
import com.ew.chat.contact.dto.ContactAddParam;
import com.ew.chat.contact.dto.ContactEditParam;
import com.ew.chat.contact.dto.ContactParamMapper;
import com.ew.chat.contact.dto.ContactDto;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.chat.message.constants.MessageType;
import com.ew.chat.message.entity.Message;
import com.ew.chat.message.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.ResultCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-20
 */
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = {Exception.class, Error.class})
public class ContactServiceImpl extends BaseServiceImpl<ContactMapper, Contact> implements IContactService {

    @Autowired
    private ContactParamMapper contactParamMapper;

    @Autowired
    private IServerClientService serverClientService;

    @Autowired
    private IMessageService messageService;

    @Override
    public PageResult<ContactDto> pageDto(ContactQueryParam contactQueryParam) {
        Wrapper<Contact> wrapper = getPageSearchWrapper(contactQueryParam);
        PageResult<ContactDto> result = contactParamMapper.pageEntity2Dto(page(contactQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @Override
    public List<ContactDto> listDto(ContactQueryParam contactQueryParam) {
        String curUserId = UserUtils.getCurrentUser().getUserid();
        List<Contact> list = this.list(Wrappers.<Contact>lambdaQuery().eq(Contact::getFromId, curUserId));
        List<ContactDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Contact c : list) {
                ContactDto dto = contactParamMapper.entity2Dto(c);
                dto.setId(c.getContactId());
                dto.setKeyId(c.getId()); // 主键id
                dto.setDisplayName(c.getName());
                if (ContactType.PERSON.equals(dto.getType())) {
                    // 查询用户
                    UserDto contactUser = serverClientService.getUserDtoById(dto.getId());
                    if (contactUser != null) {
                        dto.setDisplayName(contactUser.getRealName()); // 用户名
                        dto.setAvatar(contactUser.getPortrait()); // 头像
                    }
                }
                // 是否有备注
                if (StringUtils.isNotEmpty(c.getRemarkName())) {
                    dto.setDisplayName(c.getRemarkName());
                }
                // 查询未读的消息(对方发给自己)
                dto.setUnread(getContactUnread(dto.getId(), curUserId));
                // 查询最后一条消息的时间和内容
                Message lastMessage = getLastMessage(dto.getId(), curUserId);
                if (lastMessage != null) {
                    dto.setLastSendTime(lastMessage.getSendTime());
                    dto.setLastContent(lastMessage.getContent());
                    if (MessageType.FILE.equals(lastMessage.getType())) {
                        dto.setLastContent("[文件]");
                    } else if (MessageType.IMAGE.equals(lastMessage.getType())) {
                        dto.setLastContent("[图片]");
                    }
                } else {
                    dto.setLastContent(" "); // 由于前端组件的问题，当该聊天对象没有消息记录时，设置内容为空格，不能设置为空或空串，否则联系人窗口不显示
                }
                result.add(dto);
            }
        }
        return result;
    }

    /**
     * 获取未读消息的数量
     * @param fromId 对方
     * @param toId 发给谁
     * @return
     */
    public int getContactUnread(String fromId, String toId) {
        return messageService.count(Wrappers.<Message>lambdaQuery().eq(Message::getFromUserId, fromId)
                .eq(Message::getToContactId, toId).eq(Message::getStatus, MessageType.UNREAD));
    }

    /**
     * 查询最后一条消息的时间和内容,可以是对方发给自己，也可以是自己发给对方
     * @param fromId 对方
     * @param toId 发给谁
     * @return
     */
    public Message getLastMessage(String fromId, String toId) {
        return messageService.getOne(Wrappers.<Message>lambdaQuery()
                .eq(Message::getFromUserId, fromId)
                .eq(Message::getToContactId, toId)
                .or(q -> q.eq(Message::getFromUserId, toId).eq(Message::getToContactId, fromId))
                .orderByDesc(Message::getSendTime).last("LIMIT 1"));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(ContactAddParam contactAddParam) {
        String userid = UserUtils.getCurrentUser().getUserid();
        Contact contact = contactParamMapper.addParam2Entity(contactAddParam);
        contact.setFromId(UserUtils.getCurrentUser().getUserid());
        int count = this.count(Wrappers.<Contact>lambdaQuery()
                .eq(Contact::getContactId, contactAddParam.getContactId())
                .eq(Contact::getFromId, userid)
                .eq(Contact::getType, contactAddParam.getType()));
        // 已经有该联系人，不需要重复添加
        if (count > 0) {
            return true;
        }
        return save(contact);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(ContactEditParam contactEditParam) {
        Contact contact = contactParamMapper.editParam2Entity(contactEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,contact);
        return updateById(contact);
    }

    @Override
    public ContactDto getDtoById(String id) {
        return contactParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<ContactDto> rows) {
        return saveBatch(contactParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<Contact> getPageSearchWrapper(ContactQueryParam contactQueryParam) {
        LambdaQueryWrapper<Contact> wrapper = Wrappers.<Contact>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(Contact.class)) {
            wrapper.orderByDesc(Contact::getUpdateTime, Contact::getCreateTime);
        }
        return wrapper;
    }
}
