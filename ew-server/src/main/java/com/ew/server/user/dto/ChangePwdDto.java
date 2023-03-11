package com.ew.server.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lzf
 * @create 2023/03/10
 * @description 修改密码实体
 */
@Data
@ApiModel("修改密码实体")
public class ChangePwdDto {

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("旧密码")
    private String oldPassword;

    @ApiModelProperty("新密码")
    private String newPassword;
}
