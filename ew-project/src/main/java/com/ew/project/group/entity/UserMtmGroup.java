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
@TableName("t_user_mtm_group")
public class UserMtmGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 项目组id
     */
    @TableField("group_id")
    private String groupId;

    /**
     * 角色
     */
    @TableField("role")
    private String role;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
