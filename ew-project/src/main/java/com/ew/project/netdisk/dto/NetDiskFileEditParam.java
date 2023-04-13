package com.ew.project.netdisk.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  编辑参数对象
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "NetDiskFile编辑", description = "编辑参数")
public class NetDiskFileEditParam {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty("文件名字")
    private String fileName;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("是否为文件夹")
    private boolean isDir;

    @ApiModelProperty("目录id")
    private String dirId;

    @ApiModelProperty(value = "版本号")
    private Integer version;

}
