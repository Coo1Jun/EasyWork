package com.ew.communication.groupchat.entity;

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
@TableName("t_group_chat_member")
public class GroupChatMember extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 群聊id
     */
    @TableField("group_chat_id")
    private String groupChatId;

    /**
     * 未读数量
     */
    @TableField("unread")
    private Integer unread;

    /**
     * 用户在群聊中的身份
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
