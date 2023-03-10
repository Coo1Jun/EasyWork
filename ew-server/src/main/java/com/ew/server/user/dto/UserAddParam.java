package com.ew.server.user.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <pre>
 *  新增参数
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-02-13
 *
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "User新增", description = "新增参数")
public class UserAddParam {
    /**
     * 用户名，昵称
     */
    @ApiModelProperty("用户名，昵称")
    private String nickname;

    /**
     * 真实名字
     */
    @ApiModelProperty("真实名字")
    private String realName;

    /**
     * 英文名称（唯一）
     */
    @ApiModelProperty("英文名称")
    private String engName;

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
     * 手机（唯一）
     */
    @ApiModelProperty("手机")
    private String phone;

    /**
     * 出生年月
     */
    @ApiModelProperty("出生年月")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone ="GMT+8" )
    private Date birthDate;

    /**
     * 个人描述
     */
    @ApiModelProperty("个人描述")
    private String description;

    /**
     * 肖像
     */
    @ApiModelProperty("肖像文件id")
    private String portrait;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String sex;

    /**
     * 用户版本（每次修改都会改变）
     */
    @ApiModelProperty("用户版本（每次修改都会改变）")
    private String userVersion;

}