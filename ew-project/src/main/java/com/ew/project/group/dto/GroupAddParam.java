package com.ew.project.group.dto;


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
 * @date 2023-03-30
 *
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "Group新增", description = "新增参数")
public class GroupAddParam {
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
}