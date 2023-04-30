package com.ew.communication.calendar.todolist.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.communication.calendar.todolist.entity.TodoList;
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
public interface TodoListParamMapper {

    /**
     * 新增参数转换为实体类
     *
     * @param todoListAddParam 新增参数
     * @return TodoList 实体类
     * @date 2023-04-30
     */
    TodoList addParam2Entity(TodoListAddParam todoListAddParam);

    /**
     * 修改参数转换为实体类
     *
     * @param todoListEditParam 修改参数
     * @return TodoList 实体类
     * @date 2023-04-30
     */
    TodoList editParam2Entity(TodoListEditParam todoListEditParam);

    /**
     * 实体类换为Dto
     *
     * @param todoList 实体类
     * @return TodoListDto DTO
     * @date 2023-04-30
     */
    TodoListDto entity2Dto(TodoList todoList);

    /**
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<TodoListDto> 分页DTO
     * @date 2023-04-30
     */
    PageResult<TodoListDto> pageEntity2Dto(PageResult<TodoList> page);


    /**
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<TodoList> entity列表
     * @date 2023-04-30
     */
    List<TodoList> dtoList2Entity(List<TodoListDto> rows);

}
