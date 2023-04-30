package com.ew.communication.calendar.dto;

import com.ew.communication.address.dto.AddressBookDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lzf
 * @create 2023/04/30
 * @description 日历模块返回实体类
 */
@Data
@ApiModel(value = "CalendarDto", description = "返回数据模型")
public class CalendarDto {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty("类型：日程或待办")
    private String type; // schedule or 'tod0'

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

    @ApiModelProperty("截止时间前几分钟提醒")
    private Integer reminderTime;

    @ApiModelProperty("待办是否结束")
    private Integer isEnd;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("参数人信息")
    private List<AddressBookDto> participants;


    /**
     * 创建人ID
     */
    private String createId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人ID
     */
    private String updateId;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
