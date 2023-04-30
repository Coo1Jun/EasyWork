package com.ew.communication.calendar.schedule.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_schedule_otm_user")
public class ScheduleOtmUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 日程id
     */
    @TableField("schedule_id")
    private String scheduleId;

    /**
     * 用户id，即参与人
     */
    @TableField("user_id")
    private String userId;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
