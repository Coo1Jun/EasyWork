package com.ew.project.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;


/**
 * <pre>
 *  返回数据模型
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-03-30
 *
 */
@Data
@ApiModel(value = "UserMtmGroupDto", description = "返回数据模型")
public class UserMtmGroupDto{

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("个性签名")
    private String description;

    @ApiModelProperty(value = "项目组id")
    private String groupId;

    @ApiModelProperty(value = "角色")
    private String role;

    @ApiModelProperty(value = "用户名称")
    private String name;

    @ApiModelProperty(value = "用户邮箱")
    private String email;


    /** 创建人ID */
    private String createId;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone ="GMT+8" )
    private Date createTime;

    /** 更新人ID */
    private String updateId;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone ="GMT+8" )
    private Date updateTime;

    /** 备注 */
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String remark;
}