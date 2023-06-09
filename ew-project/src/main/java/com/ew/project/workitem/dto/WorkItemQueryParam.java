package com.ew.project.workitem.dto;

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
 * @date 2023-04-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WorkItem查询", description = "查询参数")
public class WorkItemQueryParam extends PageParam {
    @ApiModelProperty("项目id")
    private String projectId;

    @ApiModelProperty("EpicId")
    private String EpicId;

    @ApiModelProperty("工作项标题")
    private String title;

    @ApiModelProperty("流程状态")
    private String status;

    @ApiModelProperty("负责人id")
    private String principalId;

    @ApiModelProperty("工作项类型")
    private String workType;
}
