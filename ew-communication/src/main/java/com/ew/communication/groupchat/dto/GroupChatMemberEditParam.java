package com.ew.communication.groupchat.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  编辑参数对象
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-21
 *
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "GroupChatMember编辑", description = "编辑参数")
public class GroupChatMemberEditParam {

    @ApiModelProperty(value = "主键ID")
    private String id;



    @ApiModelProperty(value = "版本号")
    private Integer version;

}
