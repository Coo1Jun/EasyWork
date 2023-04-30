package com.ew.communication.calendar.schedule.service;

import com.ew.communication.calendar.schedule.entity.Schedule;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.communication.calendar.schedule.dto.ScheduleQueryParam;
import com.ew.communication.calendar.schedule.dto.ScheduleAddParam;
import com.ew.communication.calendar.schedule.dto.ScheduleEditParam;
import com.ew.communication.calendar.schedule.dto.ScheduleDto;
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
public interface IScheduleService extends IBaseService<Schedule> {

    /**
     * 分页查询，返回Dto
     *
     * @param scheduleQueryParam 查询参数
     * @return ScheduleDto 查询返回列表实体
     * @since 2023-04-30
     */
    PageResult<ScheduleDto> pageDto(ScheduleQueryParam scheduleQueryParam);

    /**
     * 新增
     *
     * @param scheduleAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-30
     */
    boolean saveByParam(ScheduleAddParam scheduleAddParam);

    /**
     * 根据id查询，转dto
     *
     * @param id 日程实体信息id
     * @return ScheduleDto
     * @since 2023-04-30
     */
    ScheduleDto getDtoById(String id);

    /**
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-30
     */
    boolean saveDtoBatch(List<ScheduleDto> rows);

    /**
     * 更新
     *
     * @param scheduleEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-30
     */
    boolean updateByParam(ScheduleEditParam scheduleEditParam);
}