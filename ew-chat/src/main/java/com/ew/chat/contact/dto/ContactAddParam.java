package com.ew.chat.contact.dto;


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
 * @date 2023-04-20
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "Contact新增", description = "新增参数")
public class ContactAddParam {

    /**
     * 联系人id或者是群聊的id
     */
    @ApiModelProperty("联系人id或者是群聊的id")
    private String contactId;

    /**
     * 这是谁保存的联系人
     */
    @ApiModelProperty("这是谁保存的联系人")
    private String fromId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

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

    /**
     * 类型，group群聊；person个人
     */
    @ApiModelProperty("类型")
    private String type;
}