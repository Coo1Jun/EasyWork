package com.ew.project.workitem.dto;


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
 * @date 2023-04-07
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "WorkItem新增", description = "新增参数")
public class WorkItemAddParam {

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 工作项描述
     */
    @ApiModelProperty("工作项描述")
    private String description;

    /**
     * 所属项目id
     */
    @ApiModelProperty("所属项目id")
    private String projectId;

    /**
     * 所属计划集id
     */
    @ApiModelProperty("所属计划集id")
    private String plansId;

    /**
     * 父工作项id
     */
    @ApiModelProperty("父工作项id")
    private String parentWorkItemId;

    /**
     * 工作项类型（Epic\Feature\Story\Task\Bug）	计划集类型：Plans
     */
    @ApiModelProperty("工作项类型")
    private String workType;

    /**
     * 负责人id
     */
    @ApiModelProperty("负责人id")
    private String principalId;

    /**
     * 任务开始时间
     */
    @ApiModelProperty("任务开始时间")
    private Date startTime;

    /**
     * 任务结束时间
     */
    @ApiModelProperty("任务结束时间")
    private Date endTime;

    /**
     * 优先级（整数：0~5）
     */
    @ApiModelProperty("优先级")
    private Integer priority;

    /**
     * 风险等级（整数：0~3）
     */
    @ApiModelProperty("风险等级")
    private Integer risk;

    /**
     * 严重程度（整数：0~5）
     */
    @ApiModelProperty("严重程度")
    private Integer severity;

    /**
     * 文件列表
     */
    @ApiModelProperty("文件列表")
    private List<String> fileList;

}