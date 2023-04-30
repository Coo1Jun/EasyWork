package com.ew.communication.calendar.todolist.service;

import com.ew.communication.calendar.todolist.entity.TodoList;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.communication.calendar.todolist.dto.TodoListQueryParam;
import com.ew.communication.calendar.todolist.dto.TodoListAddParam;
import com.ew.communication.calendar.todolist.dto.TodoListEditParam;
import com.ew.communication.calendar.todolist.dto.TodoListDto;
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
public interface ITodoListService extends IBaseService<TodoList> {

    /**
     * 分页查询，返回Dto
     *
     * @param todoListQueryParam 查询参数
     * @return TodoListDto 查询返回列表实体
     * @since 2023-04-30
     */
    PageResult<TodoListDto> pageDto(TodoListQueryParam todoListQueryParam);

    /**
     * 新增
     *
     * @param todoListAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-30
     */
    boolean saveByParam(TodoListAddParam todoListAddParam);

    /**
     * 根据id查询，转dto
     *
     * @param id 待办实体信息id
     * @return TodoListDto
     * @since 2023-04-30
     */
    TodoListDto getDtoById(String id);

    /**
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-30
     */
    boolean saveDtoBatch(List<TodoListDto> rows);

    /**
     * 更新
     *
     * @param todoListEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-30
     */
    boolean updateByParam(TodoListEditParam todoListEditParam);
}