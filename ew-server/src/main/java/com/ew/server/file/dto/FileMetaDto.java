package com.ew.server.file.dto;

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
 * @date 2023-03-01
 *
 */
@Data
@ApiModel(value = "FileMetaDto", description = "返回数据模型")
public class FileMetaDto{

    @ApiModelProperty(value = "主键ID")
    private String id;

    private String originalFilename;

    private String location;

    private Long fileSize;

    private String fileName;

    private String description;

    /** 创建人ID */
    private String createId;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone ="GMT+8" )
    private Date createTime;



    /** 备注 */
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String remark;
}