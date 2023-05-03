package com.ew.project.workitem.comment.entity;

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
 * @since 2023-05-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_work_item_otm_comment")
public class WorkItemOtmComment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 工作项id
     */
    @TableField("work_item_id")
    private String workItemId;

    /**
     * 评论内容
     */
    @TableField("content")
    private String content;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
