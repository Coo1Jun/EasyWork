package com.ew.chat.message.entity;

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
 * @since 2023-04-19
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_message")
public class Message extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 消息内容，如果type=file，此属性表示文件的URL地址	
     */
    @TableField("content")
    private String content;

    /**
     * 消息发送时间，13位毫秒
     */
    @TableField("send_time")
    private Long sendTime;

    /**
     * 消息类型：file | image | text | event
     */
    @TableField("type")
    private String type;

    /**
     * 如果消息类型是file，则存在文件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 如果消息类型是file，则存在文件大小
     */
    @TableField("file_size")
    private Integer fileSize;

    /**
     * 消息发送的状态：going | failed | succeed
     */
    @TableField("status")
    private String status;

    /**
     * 接收消息的联系人ID
     */
    @TableField("to_contact_id")
    private String toContactId;

    /**
     * 消息发送人的ID
     */
    @TableField("from_user_id")
    private String fromUserId;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
