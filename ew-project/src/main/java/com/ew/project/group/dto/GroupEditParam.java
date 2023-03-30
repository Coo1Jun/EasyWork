package com.ew.project.group.dto;


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
 * @date 2023-03-30
 *
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "Group编辑", description = "编辑参数")
public class GroupEditParam {

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
    private String description;


    @ApiModelProperty(value = "版本号")
    private Integer version;

}
