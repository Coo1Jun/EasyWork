package com.ew.communication.contact.entity;

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
 * @since 2023-04-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_contact")
public class Contact extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 联系人id或者是群聊的id
     */
    @TableField("contact_id")
    private String contactId;

    /**
     * 这是谁保存的联系人
     */
    @TableField("from_id")
    private String fromId;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 备注的名称
     */
    @TableField("remark_name")
    private String remarkName;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 类型，group群聊；person个人
     */
    @TableField("type")
    private String type;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
