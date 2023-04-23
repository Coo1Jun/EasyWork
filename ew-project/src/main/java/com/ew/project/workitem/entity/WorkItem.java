package com.ew.project.workitem.entity;

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
 * @since 2023-04-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_work_item")
public class WorkItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableField("number")
    private Integer number;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 工作项描述
     */
    @TableField("description")
    private String description;

    /**
     * 所属项目id
     */
    @TableField("project_id")
    private String projectId;

    /**
     * 所属计划集id
     */
    @TableField("plans_id")
    private String plansId;

    /**
     * 父工作项id
     */
    @TableField("parent_work_item_id")
    private String parentWorkItemId;
    /**
     * EpicId
     */
    @TableField("epic_id")
    private String epicId;

    /**
     * 工作项类型（Epic\Feature\Story\Task\Bug）	计划集类型：Plans
     */
    @TableField("work_type")
    private String workType;

    /**
     * 负责人id
     */
    @TableField("principal_id")
    private String principalId;

    /**
     * 任务开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 任务结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 优先级（整数：0~5）
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 风险等级（整数：0~3）
     */
    @TableField("risk")
    private Integer risk;

    /**
     * 严重程度（整数：0~5）
     */
    @TableField("severity")
    private Integer severity;

    /**
     * 流程状态
     */
    @TableField("status")
    private String status;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
