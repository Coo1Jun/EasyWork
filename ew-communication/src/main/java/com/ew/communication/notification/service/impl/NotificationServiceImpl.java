package com.ew.communication.notification.service.impl;

import cn.edu.hzu.client.dto.*;
import cn.edu.hzu.client.server.service.IProjectClientService;
import cn.edu.hzu.client.server.service.IServerClientService;
import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.api.utils.UserUtils;
import cn.edu.hzu.common.constant.NotificationConstant;
import cn.edu.hzu.common.constant.NotificationType;
import cn.edu.hzu.common.entity.BaseEntity;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.communication.address.entity.AddressBook;
import com.ew.communication.address.service.IAddressBookService;
import com.ew.communication.calendar.schedule.dto.ScheduleDto;
import com.ew.communication.calendar.schedule.service.IScheduleService;
import com.ew.communication.calendar.todolist.dto.TodoListDto;
import com.ew.communication.calendar.todolist.service.ITodoListService;
import com.ew.communication.notification.dto.*;
import com.ew.communication.notification.dto.NotificationAddParam;
import com.ew.communication.notification.dto.NotificationEditParam;
import com.ew.communication.notification.entity.Notification;
import com.ew.communication.notification.mapper.NotificationMapper;
import com.ew.communication.notification.service.INotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
@Transactional(rollbackFor = {Exception.class, Error.class})
public class NotificationServiceImpl extends BaseServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Autowired
    private NotificationParamMapper notificationParamMapper;
    @Autowired
    private IAddressBookService addressBookService;

    // 服务调用
    @Autowired
    private IServerClientService serverClientService;
    @Autowired
    private IProjectClientService projectClientService;
    @Autowired
    private IScheduleService scheduleService;
    @Autowired
    private ITodoListService todoListService;

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
        Notification notification = this.getById(notificationEditParam.getId());
        if (notificationEditParam.getIsRead() != NotificationConstant.UNREAD) {
            notification.setIsRead(notificationEditParam.getIsRead());
        }
        if (notificationEditParam.getIsHandle() != NotificationConstant.UN_HANDLE) {
            notification.setIsHandle(notificationEditParam.getIsHandle());
        }
        if (NotificationType.FRIEND.equals(notification.getType())) {
            if (notificationEditParam.getIsHandle() == NotificationConstant.AGREE) {
                AddressBook addressBook1 = new AddressBook();
                AddressBook addressBook2 = new AddressBook();
                addressBook1.setFromId(notification.getFromId());
                addressBook1.setUserId(notification.getUserId());
                addressBook2.setUserId(notification.getFromId());
                addressBook2.setFromId(notification.getUserId());
                addressBookService.save(addressBook1);
                addressBookService.save(addressBook2);
            }
        } else if (NotificationType.GROUP.equals(notification.getType())) {
            if (notificationEditParam.getIsHandle() == NotificationConstant.AGREE) {
                UserMtmGroup userMtmGroup = new UserMtmGroup();
                userMtmGroup.setRole("user");
                userMtmGroup.setUserId(notification.getUserId());
                userMtmGroup.setGroupId(notification.getOperationId());
                projectClientService.addGroupMember(userMtmGroup);
            }
        }
        return updateById(notification);
    }

    @Override
    public NotificationResult getList(NotificationQueryParam notificationQueryParam) {
        String curUserId = UserUtils.getCurrentUser().getUserid();
        List<NotificationDto> dtoList = notificationParamMapper.entityList2Dto(
                this.list(Wrappers.<Notification>lambdaQuery()
                        .eq(Notification::getUserId, curUserId)
                        .orderByDesc(Notification::getCreateTime)));
        NotificationResult result = new NotificationResult();
        result.setUnread(0);
        List<NotificationDto> notificationList = new ArrayList<>();
        result.setResult(notificationList);
        if (CollectionUtils.isNotEmpty(dtoList)) {
            int unread = 0;
            for (NotificationDto dto : dtoList) {
                // 获取用户信息
                if (StringUtils.isNotEmpty(dto.getFromId())) {
                    UserDto userDto = serverClientService.getUserDtoById(dto.getFromId());
                    if (userDto != null) {
                        dto.setFromName(userDto.getRealName());
                        dto.setFromAvatar(userDto.getPortrait());
                        dto.setFromEmail(userDto.getEmail());
                    }
                }
                // 获取工作项信息或项目组信息，当查询到的信息为空时，忽略此通知
                if (NotificationType.WARN.equals(dto.getType()) || NotificationType.WORK.equals(dto.getType())) {
                    WorkItemDto workItem = projectClientService.getWorkItemById(dto.getOperationId());
                    if (workItem == null) continue;
                    dto.setWorkItem(workItem);
                    ProjectDto project = projectClientService.getProjectInfoById(workItem.getProjectId());
                    if (project == null) continue;
                    dto.setProjectName(project.getProjectName());
                    dto.setProjectTab(project.getProjectTab());
                } else if (NotificationType.GROUP.equals(dto.getType())) {
                    GroupDto group = projectClientService.getGroupInfoById(dto.getOperationId());
                    if (group == null) continue;
                    dto.setGroup(group);
                } else if (NotificationType.NEW_SCHEDULE.equals(dto.getType()) || NotificationType.SCHEDULE.equals(dto.getType())) {
                    ScheduleDto schedule = scheduleService.getDtoById(dto.getOperationId());
                    if (schedule == null) continue;
                    dto.setSchedule(schedule);
                } else if (NotificationType.TODO.equals(dto.getType())) {
                    TodoListDto todoList = todoListService.getDtoById(dto.getOperationId());
                    if (todoList == null) continue;
                    dto.setTodoList(todoList);
                }
                // 未读通知
                if (dto.getIsRead() == NotificationConstant.UN_HANDLE) {
                    unread++;
                }
                notificationList.add(dto);
            }
            result.setUnread(unread);
        }
        return result;
    }

    @Override
    public boolean groupInvite(GroupInvitation groupInvitation) {
        String userId = UserUtils.getCurrentUser().getUserid();
        List<String> emails = groupInvitation.getEmails();
        if (CollectionUtils.isNotEmpty(emails)) {
            for (String email : emails) {
                if (StringUtils.isNotEmpty(email)) {
                    UserDto user = serverClientService.getUserByEmail(email);
                    if (user != null) {
                        // 发通知
                        Notification notification = new Notification();
                        notification.setType(NotificationType.GROUP);
                        notification.setIsRead(NotificationConstant.UNREAD);
                        notification.setIsHandle(NotificationConstant.UN_HANDLE);
                        notification.setFromId(userId);
                        notification.setUserId(user.getId());
                        notification.setOperationId(groupInvitation.getGroupId());
                        this.save(notification);
                    }
                }
            }
        }
        return true;
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
