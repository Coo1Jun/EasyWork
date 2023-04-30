package com.ew.communication.calendar.schedule.entity;

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
@TableName("t_schedule")
public class Schedule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 是否邮箱提醒（0否 1是）
     */
    @TableField("email_reminder")
    private Integer emailReminder;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
