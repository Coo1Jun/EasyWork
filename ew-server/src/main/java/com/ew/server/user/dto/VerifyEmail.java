package com.ew.server.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lzf
 * @create 2023/03/03
 * @description 邮箱实体
 */
@Data
@ApiModel("发送验证码的邮箱实体")
public class VerifyEmail {
    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("类型")
    private String type;
}
