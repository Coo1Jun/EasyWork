package com.ew.project.workitem.comment.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  编辑参数对象
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-05-03
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "WorkItemOtmComment编辑", description = "编辑参数")
public class WorkItemOtmCommentEditParam {

    @ApiModelProperty(value = "主键ID")
    private String id;


    @ApiModelProperty(value = "版本号")
    private Integer version;

}
