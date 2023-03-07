package com.ew.server.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzf
 * @create 2023/03/03
 * @description 用户注册实体
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "User注册", description = "注册参数")
public class UserRegisterDto {

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 邮箱（唯一）
     */
    @ApiModelProperty("邮箱（唯一）")
    private String email;

    /**
     * 真实名字
     */
    @ApiModelProperty("真实名字")
    private String realName;

    /**
     * 邮箱验证码
     */
    @ApiModelProperty("邮箱验证码")
    private String code;
}
