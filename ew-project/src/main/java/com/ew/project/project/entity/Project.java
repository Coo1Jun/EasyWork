package com.ew.project.project.entity;

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
 * @since 2023-04-02
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_project")
public class Project extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     *  项目组id
     */
    @TableField("group_id")
    private String groupId;

    /**
     * 项目名称
     */
    @TableField("name")
    private String name;

    /**
     * 项目标识
     */
    @TableField("tab")
    private String tab;

    /**
     * 项目描述
     */
    @TableField("description")
    private String description;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
