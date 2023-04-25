package com.ew.communication.notification.entity;

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
 * @since 2023-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_notification")
public class Notification extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 是否已读（0未读 1已读）
     */
    @TableField("is_read")
    private Integer isRead;

    /**
     * 通知类型	①work：from_id将user_id添加为work_item_id的负责人	②friend：from_id请求添加user_id为好友	③group：from_id邀请user_id进入group_id项目组	④warn：预警，工作项work_item_id即将截止
     */
    @TableField("type")
    private String type;

    /**
     * 操作id	①type为work：此id为工作项work_item_id	②type为group：此id为项目组id
     */
    @TableField("operation_id")
    private Integer operationId;

    /**
     * 来源的用户id
     */
    @TableField("from_id")
    private String fromId;

    /**
     * 是否已经除了（0未处理 1拒绝处理 2同意处理）	以下类型需要处理	friend group
     */
    @TableField("is_handle")
    private Integer isHandle;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
