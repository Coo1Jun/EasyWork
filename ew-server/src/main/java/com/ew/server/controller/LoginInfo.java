package com.ew.server.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lzf
 * @create 2023/02/13
 * @description 登录信息
 */
@Data
@ApiModel("登录信息")
public class LoginInfo {
    @ApiModelProperty("用户名：邮箱")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("记住我")
    private String ifRemember;
}
