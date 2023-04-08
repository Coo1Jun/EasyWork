package com.ew.project.workitem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lzf
 * @create 2023/04/08
 * @description 拥有工作项的用户信息
 */
@Data
@ApiModel(value = "WorkItemUserDto", description = "拥有工作项的用户信息")
public class WorkItemUserDto {

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("用户头像")
    private String portrait;

    @ApiModelProperty("任务总数")
    private Integer taskCount;

    @ApiModelProperty("已经完成的任务数量")
    private Integer completedTasks;

}
