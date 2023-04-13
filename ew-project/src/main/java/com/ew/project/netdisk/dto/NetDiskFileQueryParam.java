package com.ew.project.netdisk.dto;

import cn.edu.hzu.common.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <pre>
 *  分页查询参数对象
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "NetDiskFile查询", description = "查询参数")
public class NetDiskFileQueryParam extends PageParam {

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("目录id")
    private String dirId;

    /**
     * 分页查询的偏移量
     */
    @ApiModelProperty("分页查询的偏移量")
    private Integer offset;
}
