package com.ew.project.project.dto;


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
 * @date 2023-04-02
 *
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "Project新增", description = "新增参数")
public class ProjectAddParam {
    @ApiModelProperty("项目组id")
    private String groupId;

    @ApiModelProperty("项目名称")
    private String name;

    @ApiModelProperty("项目标识")
    private String tab;

    @ApiModelProperty("项目描述")
    private String description;
}