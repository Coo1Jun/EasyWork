package com.ew.communication.calendar.schedule.service.impl;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.entity.BaseEntity;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
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
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(ScheduleEditParam scheduleEditParam) {
        Schedule schedule = scheduleParamMapper.editParam2Entity(scheduleEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,schedule);
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
