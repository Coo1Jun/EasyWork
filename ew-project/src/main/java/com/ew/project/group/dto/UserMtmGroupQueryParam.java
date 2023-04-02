package com.ew.project.group.dto;

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
 * @date 2023-03-30
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserMtmGroup查询", description = "查询参数")
public class UserMtmGroupQueryParam extends PageParam {

    /**
     * 项目组id
     */
    @ApiModelProperty("项目组id")
    private String groupId;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String name;

    /**
     * 用户邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String email;

    /**
     * 分页查询的偏移量
     */
    @ApiModelProperty("分页查询的偏移量")
    private Integer offset;
}
