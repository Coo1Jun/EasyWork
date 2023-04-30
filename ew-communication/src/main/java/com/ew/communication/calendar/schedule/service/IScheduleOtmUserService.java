package com.ew.communication.calendar.schedule.service;

import com.ew.communication.calendar.schedule.entity.ScheduleOtmUser;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.communication.calendar.schedule.dto.ScheduleOtmUserQueryParam;
import com.ew.communication.calendar.schedule.dto.ScheduleOtmUserAddParam;
import com.ew.communication.calendar.schedule.dto.ScheduleOtmUserEditParam;
import com.ew.communication.calendar.schedule.dto.ScheduleOtmUserDto;
import cn.edu.hzu.common.api.PageResult;

import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-30
 */
public interface IScheduleOtmUserService extends IBaseService<ScheduleOtmUser> {

    /**
     * 分页查询，返回Dto
     *
     * @param scheduleOtmUserQueryParam 查询参数
     * @return ScheduleOtmUserDto 查询返回列表实体
     * @since 2023-04-30
     */
    PageResult<ScheduleOtmUserDto> pageDto(ScheduleOtmUserQueryParam scheduleOtmUserQueryParam);

    /**
     * 新增
     *
     * @param scheduleOtmUserAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-30
     */
    boolean saveByParam(ScheduleOtmUserAddParam scheduleOtmUserAddParam);

    /**
     * 根据id查询，转dto
     *
     * @param id 日程和用户关系实体信息id
     * @return ScheduleOtmUserDto
     * @since 2023-04-30
     */
    ScheduleOtmUserDto getDtoById(String id);

    /**
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-30
     */
    boolean saveDtoBatch(List<ScheduleOtmUserDto> rows);

    /**
     * 更新
     *
     * @param scheduleOtmUserEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-30
     */
    boolean updateByParam(ScheduleOtmUserEditParam scheduleOtmUserEditParam);
}