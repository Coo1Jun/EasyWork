package com.ew.project.group.entity;

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
 * @since 2023-03-30
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_group")
public class Group extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 项目组名称
     */
    @TableField("name")
    private String name;

    /**
     * 项目组描述
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
