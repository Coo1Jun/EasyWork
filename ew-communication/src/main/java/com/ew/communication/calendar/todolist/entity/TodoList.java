package com.ew.communication.calendar.todolist.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import cn.edu.hzu.common.entity.BaseEntity;

/**
 * <p>
 *
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_todo_list")
public class TodoList extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 截止时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 是否邮箱提醒（0否 1是）
     */
    @TableField("email_reminder")
    private Integer emailReminder;

    /**
     * 截止时间前多少分钟提醒
     */
    @TableField("reminder_time")
    private Integer reminderTime;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 是否结束（0否 1是）
     */
    @TableField("is_end")
    private Integer isEnd;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
