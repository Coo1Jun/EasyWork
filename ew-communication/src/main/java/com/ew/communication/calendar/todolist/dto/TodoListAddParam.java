package com.ew.communication.calendar.todolist.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <pre>
 *  新增参数
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-30
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "TodoList新增", description = "新增参数")
public class TodoListAddParam {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone ="GMT+8" )
    private Date endTime;

    @ApiModelProperty("截止时间前几分钟提醒")
    private Integer reminderTime;

    @ApiModelProperty("是否邮箱提醒")
    private Integer emailReminder;

    @ApiModelProperty("描述")
    private String description;
}