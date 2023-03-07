package com.ew.server.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lzf
 * @create 2023/03/03
 * @description 注册邮箱实体
 */
@Data
@ApiModel("注册邮箱实体")
public class RegisterMail {
    @ApiModelProperty("注册的邮箱")
    private String email;
}
