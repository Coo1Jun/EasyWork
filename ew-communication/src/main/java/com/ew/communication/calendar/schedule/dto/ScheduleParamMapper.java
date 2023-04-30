package com.ew.communication.calendar.schedule.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.communication.calendar.schedule.entity.Schedule;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-30
 */
@Mapper(componentModel = "spring", imports = {BaseEntity.class})
public interface ScheduleParamMapper {

    /**
     * 新增参数转换为实体类
     *
     * @param scheduleAddParam 新增参数
     * @return Schedule 实体类
     * @date 2023-04-30
     */
    Schedule addParam2Entity(ScheduleAddParam scheduleAddParam);

    /**
     * 修改参数转换为实体类
     *
     * @param scheduleEditParam 修改参数
     * @return Schedule 实体类
     * @date 2023-04-30
     */
    Schedule editParam2Entity(ScheduleEditParam scheduleEditParam);

    /**
     * 实体类换为Dto
     *
     * @param schedule 实体类
     * @return ScheduleDto DTO
     * @date 2023-04-30
     */
    ScheduleDto entity2Dto(Schedule schedule);

    /**
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<ScheduleDto> 分页DTO
     * @date 2023-04-30
     */
    PageResult<ScheduleDto> pageEntity2Dto(PageResult<Schedule> page);


    /**
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<Schedule> entity列表
     * @date 2023-04-30
     */
    List<Schedule> dtoList2Entity(List<ScheduleDto> rows);

}
