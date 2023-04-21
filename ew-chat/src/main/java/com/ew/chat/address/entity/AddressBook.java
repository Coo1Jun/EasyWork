package com.ew.chat.address.entity;

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
 * @since 2023-04-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_address_book")
public class AddressBook extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 通讯录对象的id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 这是谁的通讯录
     */
    @TableField("from_id")
    private String fromId;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
