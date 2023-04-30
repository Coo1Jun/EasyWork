package com.ew.communication.calendar.schedule.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.communication.calendar.schedule.entity.ScheduleOtmUser;
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
public interface ScheduleOtmUserParamMapper {

    /**
     * 新增参数转换为实体类
     *
     * @param scheduleOtmUserAddParam 新增参数
     * @return ScheduleOtmUser 实体类
     * @date 2023-04-30
     */
    ScheduleOtmUser addParam2Entity(ScheduleOtmUserAddParam scheduleOtmUserAddParam);

    /**
     * 修改参数转换为实体类
     *
     * @param scheduleOtmUserEditParam 修改参数
     * @return ScheduleOtmUser 实体类
     * @date 2023-04-30
     */
    ScheduleOtmUser editParam2Entity(ScheduleOtmUserEditParam scheduleOtmUserEditParam);

    /**
     * 实体类换为Dto
     *
     * @param scheduleOtmUser 实体类
     * @return ScheduleOtmUserDto DTO
     * @date 2023-04-30
     */
    ScheduleOtmUserDto entity2Dto(ScheduleOtmUser scheduleOtmUser);

    /**
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<ScheduleOtmUserDto> 分页DTO
     * @date 2023-04-30
     */
    PageResult<ScheduleOtmUserDto> pageEntity2Dto(PageResult<ScheduleOtmUser> page);


    /**
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<ScheduleOtmUser> entity列表
     * @date 2023-04-30
     */
    List<ScheduleOtmUser> dtoList2Entity(List<ScheduleOtmUserDto> rows);

}
