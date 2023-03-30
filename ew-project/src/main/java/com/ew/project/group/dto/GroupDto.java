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
@ApiModel(value = "GroupDto", description = "返回数据模型")
public class GroupDto{

    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 项目组名称
     */
    @ApiModelProperty(value = "项目组名称")
    private String name;

    /**
     * 项目组描述
     */
    @ApiModelProperty(value = "项目组描述")
    private String desc;


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