package com.sso.server.user.dto;


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
 * @date 2023-02-13
 *
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "User编辑", description = "编辑参数")
public class UserEditParam {

    @ApiModelProperty(value = "主键ID")
    private String id;



    @ApiModelProperty(value = "版本号")
    private Integer version;

}
