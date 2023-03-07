package com.ew.server.file.dto;

import cn.edu.hzu.common.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * <pre>
 *  分页查询参数对象
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-03-01
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FileMeta查询", description = "查询参数")
public class FileMetaQueryParam extends PageParam {
    @ApiModelProperty("文件原始名称")
    private String originalFilename;
}
