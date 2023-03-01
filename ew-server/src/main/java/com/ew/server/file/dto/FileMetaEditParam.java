package com.ew.server.file.dto;


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
 * @date 2023-03-01
 *
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "FileMeta编辑", description = "编辑参数")
public class FileMetaEditParam {

    @ApiModelProperty(value = "主键ID")
    private String id;



    @ApiModelProperty(value = "版本号")
    private Integer version;

}
