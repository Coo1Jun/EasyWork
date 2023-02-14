package cn.edu.hzu.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lzf
 * @createTime 2022/09/03
 * @description 系统文件导入导出记录表 返回数据模型
 */
@Data
@ApiModel(value = "SystemFileLogDto", description = "系统文件导入导出记录表返回数据模型")
public class SystemFileLogDto {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "乐观锁版本号")
    private Integer version;

    /**
     * 外键id
     */
    @ApiModelProperty("外键id")
    private String parentId;

    /**
     * 附件名称
     */
    @ApiModelProperty("附件名称")
    private String fileName;

    /**
     * 文件导出类型:excel导出/附件下载
     */
    @ApiModelProperty("文件导出类型:excel导出/附件下载")
    private String fileType;

    /**
     * excel导出数据记录
     */
    @ApiModelProperty("excel导出数据记录")
    private String fileExportNum;

    /**
     * 附件地址
     */
    @ApiModelProperty("附件地址")
    private String fileUrl;

    /**
     * 附件上传返回情况
     */
    @ApiModelProperty("附件上传返回情况")
    private String response;

    /**
     * 附件上传响应时间
     */
    @ApiModelProperty("附件上传响应时间")
    private String responseDate;

    /**
     * 系统标识
     */
    @ApiModelProperty("系统标识")
    private String sysCode;

    /**
     * 模块标识
     */
    @ApiModelProperty("模块标识")
    private String modelCode;

    /**
     * 业务标识
     */
    @ApiModelProperty("业务标识")
    private String businessCode;

    /**
     * 客户端IP
     */
    @ApiModelProperty("客户端IP")
    private String clientIp;

    /**
     * 预览图URL
     */
    @ApiModelProperty("预览图URL")
    private String previewPicture;
}