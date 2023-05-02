package com.ew.project.workitem.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 *  编辑参数对象
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-07
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "WorkItem编辑", description = "编辑参数")
public class WorkItemEditParam {

    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 流程状态
     */
    @ApiModelProperty("流程状态")
    private String status;

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
     * EpicId
     */
    @ApiModelProperty("EpicId")
    private String epicId;

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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone ="GMT+8" )
    private Date startTime;

    /**
     * 任务结束时间
     */
    @ApiModelProperty("任务结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone ="GMT+8" )
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

    @ApiModelProperty(value = "版本号")
    private Integer version;

    /**
     * 修改类型
     */
    @ApiModelProperty("修改类型")
    private String editType;

    @ApiModelProperty("是否更新文件列表0否 1是")
    private Integer updateFileList;

}
