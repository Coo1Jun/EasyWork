package com.ew.project.workitem.comment.dto;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  新增参数
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-05-03
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "WorkItemOtmComment新增", description = "新增参数")
public class WorkItemOtmCommentAddParam {
    @ApiModelProperty("工作项id")
    private String workItemId;

    @ApiModelProperty("评论内容")
    private String content;
}