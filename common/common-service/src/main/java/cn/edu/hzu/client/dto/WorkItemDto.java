package cn.edu.hzu.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * <pre>
 *  返回数据模型
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-07
 */
@Data
@ApiModel(value = "WorkItemDto", description = "返回数据模型")
public class WorkItemDto {

    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private Integer number;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 负责人id
     */
    @ApiModelProperty("负责人id")
    private String principalId;

    @ApiModelProperty("EpicId")
    private String epicId;

    @ApiModelProperty("负责人信息")
    private UserDto principal;

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
     * 流程状态
     */
    private String status;

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
     * 剩余时间，单位：天，因为需要小数点后1位，则定义为String
     */
    @ApiModelProperty("剩余时间，单位：天")
    private String remainingTime;

    /**
     * 估算工时，单位：天，整数
     */
    @ApiModelProperty("估算工时，单位：天")
    private Integer manHour;

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
    private List<FileMetaDto> fileList;

    /**
     * 孩子工作项
     */
    @ApiModelProperty("孩子工作项")
    private List<WorkItemDto> children;

    /**
     * 卡片已经结束状态("已完成", "关闭", "已取消", "未复现"): 0未结束，1结束
     */
    @ApiModelProperty("卡片已经结束状态(已完成, 关闭, 已取消, 未复现): 0未结束，1结束")
    private Integer endFlag;

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