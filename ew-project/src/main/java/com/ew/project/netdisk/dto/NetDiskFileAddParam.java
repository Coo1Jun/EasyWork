package com.ew.project.netdisk.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  新增参数
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "NetDiskFile新增", description = "新增参数")
public class NetDiskFileAddParam {
    /**
     * 所属类型（0项目 1个人），默认为个人
     */
    @ApiModelProperty("所属类型")
    private Integer belongType;

    @ApiModelProperty("所属id")
    private String belongId;

    @ApiModelProperty("文件名字")
    private String fileName;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("目录id")
    private String dirId;
}