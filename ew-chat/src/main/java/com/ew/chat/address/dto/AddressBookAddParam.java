package com.ew.chat.address.dto;


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
 * @date 2023-04-21
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "AddressBook新增", description = "新增参数")
public class AddressBookAddParam {
    @ApiModelProperty("对象id")
    private String userId;
}