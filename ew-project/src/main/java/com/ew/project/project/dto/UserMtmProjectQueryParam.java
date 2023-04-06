package com.ew.project.project.dto;

import cn.edu.hzu.common.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * <pre>
 *  分页查询参数对象
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-06
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserMtmProject查询", description = "查询参数")
public class UserMtmProjectQueryParam extends PageParam {

    @ApiModelProperty("项目名称")
    private String name;

    @ApiModelProperty("项目标识")
    private String tab;

    /**
     * 分页查询的偏移量
     */
    @ApiModelProperty("分页查询的偏移量")
    private Integer offset;

}
