package com.ew.communication.calendar.todolist.service.impl;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.entity.BaseEntity;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.communication.calendar.todolist.dto.*;
import com.ew.communication.calendar.todolist.entity.TodoList;
import com.ew.communication.calendar.todolist.mapper.TodoListMapper;
import com.ew.communication.calendar.todolist.service.ITodoListService;
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
public class TodoListServiceImpl extends BaseServiceImpl<TodoListMapper, TodoList> implements ITodoListService {

    @Autowired
    private TodoListParamMapper todoListParamMapper;

    @Override
    public PageResult<TodoListDto> pageDto(TodoListQueryParam todoListQueryParam) {
        Wrapper<TodoList> wrapper = getPageSearchWrapper(todoListQueryParam);
        PageResult<TodoListDto> result = todoListParamMapper.pageEntity2Dto(page(todoListQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(TodoListAddParam addParam) {
        // 判断入参
        if (StringUtils.isEmpty(addParam.getTitle())) {
            throw CommonException
                    .builder().resultCode(CommonErrorEnum.PARAM_IS_EMPTY.setParams(new Object[]{"标题"}))
                    .build();
        }
        if (addParam.getEndTime() == null) {
            throw CommonException
                    .builder().resultCode(CommonErrorEnum.PARAM_IS_EMPTY.setParams(new Object[]{"截止时间"}))
                    .build();
        }
        if (addParam.getEmailReminder() == null) {
            addParam.setEmailReminder(1); // 默认开启邮箱提醒
        }
        if (addParam.getEmailReminder() == null) {
            addParam.setEmailReminder(30); // 默认30分钟
        }
        TodoList todoList = todoListParamMapper.addParam2Entity(addParam);
        todoList.setIsEnd(0);
        return save(todoList);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(TodoListEditParam todoListEditParam) {
        TodoList todoList = todoListParamMapper.editParam2Entity(todoListEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,todoList);
        return updateById(todoList);
    }

    @Override
    public TodoListDto getDtoById(String id) {
        return todoListParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<TodoListDto> rows) {
        return saveBatch(todoListParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<TodoList> getPageSearchWrapper(TodoListQueryParam todoListQueryParam) {
        LambdaQueryWrapper<TodoList> wrapper = Wrappers.<TodoList>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(TodoList.class)) {
            wrapper.orderByDesc(TodoList::getUpdateTime, TodoList::getCreateTime);
        }
        return wrapper;
    }
}
