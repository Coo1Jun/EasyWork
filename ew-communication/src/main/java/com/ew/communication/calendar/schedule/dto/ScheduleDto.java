package com.ew.communication.calendar.schedule.dto;

import cn.edu.hzu.client.dto.UserDto;
import com.ew.communication.address.dto.AddressBookDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;


/**
 * <pre>
 *  返回数据模型
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-30
 */
@Data
@ApiModel(value = "ScheduleDto", description = "返回数据模型")
public class ScheduleDto {

    @ApiModelProperty(value = "主键ID")
    private String id;

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

    /** 备注 */
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String remark;
}