package com.ew.communication.calendar.schedule.mapper;

import com.ew.communication.calendar.schedule.entity.ScheduleOtmUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-30
 */
public interface ScheduleOtmUserMapper extends BaseMapper<ScheduleOtmUser> {

    List<String> getUserEmails(String scheduleId);
}
