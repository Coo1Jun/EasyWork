package com.ew.communication.calendar.dto;

import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.communication.calendar.CalendarType;
import com.ew.communication.calendar.schedule.entity.Schedule;
import com.ew.communication.calendar.todolist.entity.TodoList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 日历模块实体类映射
 */
@Mapper(componentModel = "spring", imports = {BaseEntity.class})
public interface CalendarParamMapper {

    /**
     * 日程实体转换为日历实体
     * @param schedule
     * @return
     */
    @Mapping(target = "type", constant = CalendarType.SCHEDULE)
    CalendarDto schedule2Calendar(Schedule schedule);

    /**
     * list转换：日程实体转换为日历实体
     * @param rows
     * @return
     */
    List<CalendarDto> scheduleList2CalendarList(List<Schedule> rows);

    /**
     * 待办实体转换为日历实体
     * @param todoList
     * @return
     */
    @Mapping(target = "type", constant = CalendarType.TODO)
    CalendarDto todoList2Calendar(TodoList todoList);
    /**
     * list转换：待办实体转换为日历实体
     */
    List<CalendarDto> todoList2CalendarList(List<TodoList> todoLists);
}
