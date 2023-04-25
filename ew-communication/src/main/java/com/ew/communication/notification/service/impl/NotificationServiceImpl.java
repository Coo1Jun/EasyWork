package com.ew.communication.notification.service.impl;

import com.ew.communication.notification.constants.NotificationConstant;
import com.ew.communication.notification.entity.Notification;
import com.ew.communication.notification.mapper.NotificationMapper;
import com.ew.communication.notification.service.INotificationService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.communication.notification.dto.NotificationQueryParam;
import com.ew.communication.notification.dto.NotificationAddParam;
import com.ew.communication.notification.dto.NotificationEditParam;
import com.ew.communication.notification.dto.NotificationParamMapper;
import com.ew.communication.notification.dto.NotificationDto;
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
 * @since 2023-04-25
 */
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = {Exception.class, Error.class})
public class NotificationServiceImpl extends BaseServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Autowired
    private NotificationParamMapper notificationParamMapper;

    @Override
    public PageResult<NotificationDto> pageDto(NotificationQueryParam notificationQueryParam) {
        Wrapper<Notification> wrapper = getPageSearchWrapper(notificationQueryParam);
        PageResult<NotificationDto> result = notificationParamMapper.pageEntity2Dto(page(notificationQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(NotificationAddParam notificationAddParam) {
        Notification notification = notificationParamMapper.addParam2Entity(notificationAddParam);
        notification.setIsRead(NotificationConstant.UNREAD);
        notification.setIsHandle(NotificationConstant.UN_HANDLE);
        return save(notification);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(NotificationEditParam notificationEditParam) {
        Notification notification = notificationParamMapper.editParam2Entity(notificationEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,notification);
        return updateById(notification);
    }

    @Override
    public NotificationDto getDtoById(String id) {
        return notificationParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<NotificationDto> rows) {
        return saveBatch(notificationParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<Notification> getPageSearchWrapper(NotificationQueryParam notificationQueryParam) {
        LambdaQueryWrapper<Notification> wrapper = Wrappers.<Notification>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(Notification.class)) {
            wrapper.orderByDesc(Notification::getUpdateTime, Notification::getCreateTime);
        }
        return wrapper;
    }
}
