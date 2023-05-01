package com.ew.communication.calendar.schedule.service.impl;

import cn.edu.hzu.client.dto.NotificationAddParam;
import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.client.server.service.ICommunicationClientService;
import cn.edu.hzu.client.server.service.IServerClientService;
import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.utils.DateUtils;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.constant.NotificationType;
import cn.edu.hzu.common.entity.BaseEntity;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import cn.edu.hzu.common.service.MailService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.communication.calendar.schedule.dto.*;
import com.ew.communication.calendar.schedule.entity.Schedule;
import com.ew.communication.calendar.schedule.entity.ScheduleOtmUser;
import com.ew.communication.calendar.schedule.mapper.ScheduleMapper;
import com.ew.communication.calendar.schedule.service.IScheduleOtmUserService;
import com.ew.communication.calendar.schedule.service.IScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-30
 */
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = {Exception.class, Error.class})
public class ScheduleServiceImpl extends BaseServiceImpl<ScheduleMapper, Schedule> implements IScheduleService {

    @Autowired
    private ScheduleParamMapper scheduleParamMapper;
    @Autowired
    private IScheduleOtmUserService scheduleOtmUserService;
    @Autowired
    private ICommunicationClientService communicationClientService;
    @Autowired
    private IServerClientService serverClientService;
    @Autowired
    private MailService mailService;

    @Override
    public PageResult<ScheduleDto> pageDto(ScheduleQueryParam scheduleQueryParam) {
        Wrapper<Schedule> wrapper = getPageSearchWrapper(scheduleQueryParam);
        PageResult<ScheduleDto> result = scheduleParamMapper.pageEntity2Dto(page(scheduleQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(ScheduleAddParam addParam) {
        // 判断入参
        if (StringUtils.isEmpty(addParam.getTitle())) {
            throw CommonException
                    .builder().resultCode(CommonErrorEnum.PARAM_IS_EMPTY.setParams(new Object[]{"标题"}))
                    .build();
        }
        if (addParam.getStartTime() == null || addParam.getEndTime() == null) {
            throw CommonException
                    .builder().resultCode(CommonErrorEnum.PARAM_IS_EMPTY.setParams(new Object[]{"时间"}))
                    .build();
        }
        if (addParam.getEmailReminder() == null) {
            addParam.setEmailReminder(1); // 默认开启邮箱提醒
        }
        Schedule schedule = scheduleParamMapper.addParam2Entity(addParam);
        // 保存
        this.save(schedule);
        List<String> participants = addParam.getParticipants();
        if (CollectionUtils.isNotEmpty(participants)) {
            // 保存日程参与人信息
            for (String userId : participants) {
                ScheduleOtmUser scheduleOtmUser = new ScheduleOtmUser();
                scheduleOtmUser.setUserId(userId);
                scheduleOtmUser.setScheduleId(schedule.getId());
                scheduleOtmUserService.save(scheduleOtmUser);
            }
        }
        // 发送通知和邮件
        sendNoticeAndEmail(schedule, addParam.getParticipants());
        return true;
    }

    /**
     * 发送通知和邮件
     * @param schedule
     * @param participants
     */
    private void sendNoticeAndEmail(Schedule schedule, List<String> participants) {
        ThreadUtil.execAsync(() -> {
            // 发送通知
            NotificationAddParam notificationAddParam = new NotificationAddParam();
            notificationAddParam.setType(NotificationType.NEW_SCHEDULE);
            notificationAddParam.setOperationId(schedule.getId());
            notificationAddParam.setFromId(schedule.getCreateId());
            if (CollectionUtils.isNotEmpty(participants)) {
                for (String userId : participants) {
                    if (!userId.equals(schedule.getCreateId())) {
                        notificationAddParam.setUserId(userId);
                        communicationClientService.addNotification(notificationAddParam);
                    }
                }
            }
            // 发送邮件
            if (schedule.getEmailReminder() == 1) {
                // 获取用户信息
                List<UserDto> userList = serverClientService.getUserListById(participants);
                List<String> userEmails = new ArrayList<>();
                UserDto createUser = null;
                if (CollectionUtils.isNotEmpty(userList)) {
                    for (UserDto user : userList) {
                        if (!user.getId().equals(schedule.getCreateId())) {
                            userEmails.add(user.getEmail());
                        } else {
                            createUser = user;
                        }
                    }
                }
                if (createUser == null) {
                    createUser = serverClientService.getUserDtoById(schedule.getCreateId());
                }
                // 开始发送邮件
                String subject = "Easy Work 日程提醒";
                String templateName = "new-schedule";
                Context context = new Context();
                context.setVariable("userName", createUser.getRealName());
                context.setVariable("title", schedule.getTitle());
                context.setVariable("startTime", DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", schedule.getStartTime()));
                context.setVariable("endTime", DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", schedule.getEndTime()));
                try {
                    mailService.sendMail(userEmails, subject, templateName, context);
                    log.info(">>>>>>>>>>>邮件预警发送成功，用户邮箱===》【{}】<<<<<<<<<<<<<<<<", userEmails);
                } catch (MessagingException e) {
                    log.warn(">>>>>>>>>>>>>>>>邮件预警发送失败<<<<<<<<<<<<<<<<<");
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(ScheduleEditParam editParam) {
        // 判断入参
        if (StringUtils.isEmpty(editParam.getTitle())) {
            throw CommonException
                    .builder().resultCode(CommonErrorEnum.PARAM_IS_EMPTY.setParams(new Object[]{"标题"}))
                    .build();
        }
        if (editParam.getStartTime() == null || editParam.getEndTime() == null) {
            throw CommonException
                    .builder().resultCode(CommonErrorEnum.PARAM_IS_EMPTY.setParams(new Object[]{"时间"}))
                    .build();
        }
        if (editParam.getEmailReminder() == null) {
            editParam.setEmailReminder(1); // 默认开启邮箱提醒
        }
        Schedule schedule = scheduleParamMapper.editParam2Entity(editParam);
        List<String> participants = editParam.getParticipants();
        // 删除原有的参与人
        scheduleOtmUserService.remove(Wrappers.<ScheduleOtmUser>lambdaQuery()
                .eq(ScheduleOtmUser::getScheduleId, schedule.getId()));
        if (CollectionUtils.isNotEmpty(participants)) {
            // 保存日程参与人信息
            for (String userId : participants) {
                ScheduleOtmUser scheduleOtmUser = new ScheduleOtmUser();
                scheduleOtmUser.setUserId(userId);
                scheduleOtmUser.setScheduleId(schedule.getId());
                scheduleOtmUserService.save(scheduleOtmUser);
            }
        }
        return updateById(schedule);
    }

    @Override
    public ScheduleDto getDtoById(String id) {
        return scheduleParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<ScheduleDto> rows) {
        return saveBatch(scheduleParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<Schedule> getPageSearchWrapper(ScheduleQueryParam scheduleQueryParam) {
        LambdaQueryWrapper<Schedule> wrapper = Wrappers.<Schedule>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(Schedule.class)) {
            wrapper.orderByDesc(Schedule::getUpdateTime, Schedule::getCreateTime);
        }
        return wrapper;
    }
}
