package com.ew.server.user.entity;

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
 * @since 2023-02-13
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名，昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 真实名字
     */
    @TableField("real_name")
    private String realName;

    /**
     * 英文名称
     */
    @TableField("eng_name")
    private String engName;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 角色：root、admin、user
     */
    @TableField("role")
    private String role;

    /**
     * 邮箱（唯一）
     */
    @TableField("email")
    private String email;

    /**
     * 手机
     */
    @TableField("phone")
    private String phone;

    /**
     * 出生年月
     */
    @TableField("birth_date")
    private Date birthDate;

    /**
     * 个人描述
     */
    @TableField("description")
    private String description;

    /**
     * 性别
     */
    @TableField("sex")
    private String sex;

    /**
     * 肖像（文件id）
     */
    @TableField("portrait")
    private String portrait;

    /**
     * 用户版本（每次修改都会改变）
     */
    @TableField("user_version")
    private String userVersion;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
