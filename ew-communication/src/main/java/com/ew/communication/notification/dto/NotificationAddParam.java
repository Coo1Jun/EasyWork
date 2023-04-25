package com.ew.communication.notification.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  新增参数
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-25
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "Notification新增", description = "新增参数")
public class NotificationAddParam {

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 通知类型	①work：from_id将user_id添加为work_item_id的负责人	②friend：from_id请求添加user_id为好友	③group：from_id邀请user_id进入group_id项目组	④warn：预警，工作项work_item_id即将截止
     */
    @ApiModelProperty("通知类型")
    private String type;

    /**
     * 通知类型	①type为work：此id为工作项work_item_id	②type为group：此id为项目组id
     */
    @ApiModelProperty("通知类型")
    private String operationId;

    /**
     * 通知类型
     */
    @ApiModelProperty("通知类型")
    private String fromId;

}