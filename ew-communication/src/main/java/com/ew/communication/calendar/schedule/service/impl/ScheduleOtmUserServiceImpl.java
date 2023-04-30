package com.ew.communication.calendar.schedule.service.impl;

import com.ew.communication.calendar.schedule.entity.ScheduleOtmUser;
import com.ew.communication.calendar.schedule.mapper.ScheduleOtmUserMapper;
import com.ew.communication.calendar.schedule.service.IScheduleOtmUserService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.communication.calendar.schedule.dto.ScheduleOtmUserQueryParam;
import com.ew.communication.calendar.schedule.dto.ScheduleOtmUserAddParam;
import com.ew.communication.calendar.schedule.dto.ScheduleOtmUserEditParam;
import com.ew.communication.calendar.schedule.dto.ScheduleOtmUserParamMapper;
import com.ew.communication.calendar.schedule.dto.ScheduleOtmUserDto;
import cn.edu.hzu.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.edu.hzu.common.api.PageResult;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
public class ScheduleOtmUserServiceImpl extends BaseServiceImpl<ScheduleOtmUserMapper, ScheduleOtmUser> implements IScheduleOtmUserService {

    @Autowired
    private ScheduleOtmUserParamMapper scheduleOtmUserParamMapper;

    @Override
    public PageResult<ScheduleOtmUserDto> pageDto(ScheduleOtmUserQueryParam scheduleOtmUserQueryParam) {
        Wrapper<ScheduleOtmUser> wrapper = getPageSearchWrapper(scheduleOtmUserQueryParam);
        PageResult<ScheduleOtmUserDto> result = scheduleOtmUserParamMapper.pageEntity2Dto(page(scheduleOtmUserQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(ScheduleOtmUserAddParam scheduleOtmUserAddParam) {
        ScheduleOtmUser scheduleOtmUser = scheduleOtmUserParamMapper.addParam2Entity(scheduleOtmUserAddParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,scheduleOtmUser);
        return save(scheduleOtmUser);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(ScheduleOtmUserEditParam scheduleOtmUserEditParam) {
        ScheduleOtmUser scheduleOtmUser = scheduleOtmUserParamMapper.editParam2Entity(scheduleOtmUserEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,scheduleOtmUser);
        return updateById(scheduleOtmUser);
    }

    @Override
    public ScheduleOtmUserDto getDtoById(String id) {
        return scheduleOtmUserParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<ScheduleOtmUserDto> rows) {
        return saveBatch(scheduleOtmUserParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<ScheduleOtmUser> getPageSearchWrapper(ScheduleOtmUserQueryParam scheduleOtmUserQueryParam) {
        LambdaQueryWrapper<ScheduleOtmUser> wrapper = Wrappers.<ScheduleOtmUser>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(ScheduleOtmUser.class)) {
            wrapper.orderByDesc(ScheduleOtmUser::getUpdateTime, ScheduleOtmUser::getCreateTime);
        }
        return wrapper;
    }
}
