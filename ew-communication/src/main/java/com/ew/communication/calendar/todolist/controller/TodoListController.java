package com.ew.communication.calendar.todolist.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ew.communication.calendar.todolist.dto.TodoListQueryParam;
import com.ew.communication.calendar.todolist.dto.TodoListAddParam;
import com.ew.communication.calendar.todolist.dto.TodoListEditParam;
import com.ew.communication.calendar.todolist.dto.TodoListDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;

import com.ew.communication.calendar.todolist.service.ITodoListService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 待办实体信息控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-30
 */
@RestController
@RequestMapping("/todolist")
@Api(tags = "待办实体信息服务接口")
public class TodoListController {

    @Autowired
    private ITodoListService todoListService;

    /**
     * 分页获取待办实体信息列表
     *
     * @author LiZhengFan
     * @since 2023-04-30
     */
    @ApiOperation("分页获取待办实体信息列表")
    @GetMapping("/list")
    public RestResponse<PageResult<TodoListDto>> pageList(@Valid TodoListQueryParam todoListQueryParam) {
        return RestResponse.ok(todoListService.pageDto(todoListQueryParam));
    }

    /**
     * 新增待办实体信息
     *
     * @author LiZhengFan
     * @since 2023-04-30
     */
    @ApiOperation("新增待办实体信息")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid TodoListAddParam todoListAddParam) {
        return RestResponse.ok(todoListService.saveByParam(todoListAddParam));
    }

    /**
     * 修改待办实体信息
     *
     * @author LiZhengFan
     * @since 2023-04-30
     */
    @ApiOperation(value = "修改待办实体信息")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid TodoListEditParam todoListEditParam) {
        return RestResponse.ok(todoListService.updateByParam(todoListEditParam));
    }
    /**
     * 修改待办为已结束
     *
     * @author LiZhengFan
     * @since 2023-04-30
     */
    @ApiOperation(value = "修改待办为已结束")
    @PutMapping("/end")
    public RestResponse<Boolean> updateTodoListEnd(@RequestBody @Valid TodoListEditParam todoListEditParam) {
        return RestResponse.ok(todoListService.setEnd(todoListEditParam));
    }

    /**
     * 根据id删除待办实体信息
     *
     * @author LiZhengFan
     * @since 2023-04-30
     */
    @ApiOperation(value = "根据id删除待办实体信息")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(todoListService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 根据id获取待办实体信息
     *
     * @author LiZhengFan
     * @since 2023-04-30
     */
    @ApiOperation(value = "根据id获取待办实体信息")
    @GetMapping(value = "/{id}")
    public RestResponse<TodoListDto> get(@PathVariable String id) {
        return RestResponse.ok(todoListService.getDtoById(id));
    }

}
