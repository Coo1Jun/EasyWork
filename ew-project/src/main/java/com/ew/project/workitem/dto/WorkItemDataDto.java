package com.ew.project.workitem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author lzf
 * @create 2023/04/08
 * @description 统计数据
 */
@Data
@ApiModel(value = "WorkItemDataDto", description = "统计数据")
public class WorkItemDataDto {

    @ApiModelProperty("人均卡片数量")
    private String averageTasks;

    @ApiModelProperty("卡片总数")
    private Integer allTaskCount;

    @ApiModelProperty("已经完成的卡片数量")
    private Integer allCompletedTasks;

    @ApiModelProperty("剩余卡片数量")
    private Integer remainingTasks;

    @ApiModelProperty("剩余时间，单位：天")
    private String remainingTime;

    @ApiModelProperty("延期的卡片数量")
    private Integer delayedTasks;

    @ApiModelProperty("用户数量")
    private Integer userCount;

    @ApiModelProperty("完成百分比")
    private Integer percentage;
}
