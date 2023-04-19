package com.ew.chat.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;


/**
 * <pre>
 *  返回数据模型
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-19
 */
@Data
@ApiModel(value = "MessageDto", description = "返回数据模型")
public class MessageDto {

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("消息发送人的信息")
    private MessageUser fromUser;

    @ApiModelProperty("消息发送的状态：going | failed | succeed")
    private String status;

    @ApiModelProperty("消息类型：file | image | text | event")
    private String type;

    @ApiModelProperty("消息发送时间，13位毫秒")
    private Long sendTime;

    @ApiModelProperty("消息内容，如果type=file，此属性表示文件的URL地址\t")
    private String content;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("接收消息的联系人ID")
    private String toContactId;

}