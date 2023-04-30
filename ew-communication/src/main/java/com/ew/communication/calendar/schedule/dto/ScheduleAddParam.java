package com.ew.communication.calendar.schedule.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 *  新增参数
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-30
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "Schedule新增", description = "新增参数")
public class ScheduleAddParam {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone ="GMT+8" )
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone ="GMT+8" )
    private Date endTime;

    @ApiModelProperty("是否邮箱提醒")
    private Integer emailReminder;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("参数人ids")
    private List<String> participants;
}