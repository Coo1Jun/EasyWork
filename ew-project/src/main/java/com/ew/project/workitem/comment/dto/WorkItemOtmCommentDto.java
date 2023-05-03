package com.ew.project.workitem.comment.dto;

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
 * @date 2023-05-03
 */
@Data
@ApiModel(value = "WorkItemOtmCommentDto", description = "返回数据模型")
public class WorkItemOtmCommentDto {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty("工作项id")
    private String workItemId;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 创建人ID
     */
    private String createId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人ID
     */
    private String updateId;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /** 备注 */
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String remark;
}