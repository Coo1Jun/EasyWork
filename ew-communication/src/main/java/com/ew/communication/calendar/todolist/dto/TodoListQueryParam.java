package com.ew.communication.calendar.todolist.dto;

import cn.edu.hzu.common.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <pre>
 *  分页查询参数对象
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TodoList查询", description = "查询参数")
public class TodoListQueryParam extends PageParam {

}
