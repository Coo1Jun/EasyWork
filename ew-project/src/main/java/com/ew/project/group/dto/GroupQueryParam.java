package com.ew.project.group.dto;

import cn.edu.hzu.common.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  分页查询参数对象
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-03-30
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Group查询", description = "查询参数")
public class GroupQueryParam extends PageParam {

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
