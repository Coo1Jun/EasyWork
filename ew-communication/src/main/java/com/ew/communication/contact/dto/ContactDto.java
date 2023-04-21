package com.ew.communication.contact.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


/**
 * <pre>
 *  返回数据模型
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-20
 */
@Data
@ApiModel(value = "ContactDto", description = "返回数据模型")
public class ContactDto {

    @ApiModelProperty("contactId")
    private String id; // 这不是数据库的主键id，而是字段为contact_id的值，为了迎合前端而做的修改

    @ApiModelProperty("主键id")
    private String keyId; // 这才是数据库中的主键id

    /**
     * 这是谁保存的联系人
     */
    @ApiModelProperty("这是谁保存的联系人")
    private String fromId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String displayName;

    /**
     * 备注的名称
     */
    @ApiModelProperty("备注的名称")
    private String remarkName;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("未读消息数")
    private Integer unread;

    @ApiModelProperty("最近一条消息的时间戳，13位毫秒")
    private Long lastSendTime;

    @ApiModelProperty("最近一条消息的内容")
    private String lastContent;

    /**
     * 类型，group群聊；person个人
     */
    @ApiModelProperty("类型")
    private String type;


    /**
     * 创建人ID
     */
    private String createId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人ID
     */
    private String updateId;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /** 备注 */
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String remark;
}