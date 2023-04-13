package com.ew.project.netdisk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;


/**
 * <pre>
 *  返回数据模型
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-12
 */
@Data
@ApiModel(value = "NetDiskFileDto", description = "返回数据模型")
public class NetDiskFileDto {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty("所属类型")
    private Integer belongType;

    @ApiModelProperty("所属id")
    private String belongId;

    @ApiModelProperty("所属名称")
    private String belongName;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件名称编号")
    private Integer fileNameNum;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("是否为文件夹")
    private Integer isDir;

    @ApiModelProperty("目录id")
    private String dirId;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件url")
    private String fileUrl;

    @ApiModelProperty("文件大小，单位：字节")
    private Long fileSize;

    @ApiModelProperty("文件扩展名")
    private String extendName;

    @ApiModelProperty("是否已经删除，0否 1是")
    private Integer deleted;

    @ApiModelProperty("删除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deleteTime;

    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID")
    private String createId;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人ID
     */
    @ApiModelProperty(value = "更新人ID")
    private String updateId;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("删除标志")
    private Integer deleteFlag;
}