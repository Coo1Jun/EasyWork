package com.ew.communication.notification.dto;

import cn.edu.hzu.client.dto.GroupDto;
import cn.edu.hzu.client.dto.WorkItemDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.checkerframework.checker.units.qual.A;

import java.util.Date;


/**
 * <pre>
 *  返回数据模型
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-25
 */
@Data
@ApiModel(value = "NotificationDto", description = "返回数据模型")
public class NotificationDto {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("是否已读（0未读 1已读）")
    private int isRead;

    @ApiModelProperty("通知类型")
    private String type;

    @ApiModelProperty("操作id\n" +
            "①type为work：此id为工作项work_item_id\n" +
            "②type为group：此id为项目组id")
    private String operationId;

    @ApiModelProperty("通知来源的用户id")
    private String fromId;
    @ApiModelProperty("通知来源的用户名称")
    private String fromName;
    @ApiModelProperty("通知来源的用户头像")
    private String fromAvatar;
    @ApiModelProperty("通知来源的用户邮箱")
    private String fromEmail;

    @ApiModelProperty("是否已经除了（0未处理 1拒绝处理 2同意处理）")
    private int isHandle;

    @ApiModelProperty("工作项信息")
    private WorkItemDto workItem;

    @ApiModelProperty("项目组信息")
    private GroupDto group;



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