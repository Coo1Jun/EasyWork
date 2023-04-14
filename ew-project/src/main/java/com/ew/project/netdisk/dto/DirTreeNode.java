package com.ew.project.netdisk.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzf
 * @create 2023/04/14
 * @description 文件夹树节点
 */
@Data
public class DirTreeNode {
    /**
     * 节点id
     */
    @ApiModelProperty("节点id")
    private String id;
    /**
     * 节点名
     */
    @ApiModelProperty("节点名")
    private String fileName;

    @ApiModelProperty("文件名称编号")
    private Integer fileNameNum;

    @ApiModelProperty("文件路径")
    private String filePath = "/";

    /**
     * 所属类型0项目，1个人
     */
    @ApiModelProperty("所属类型")
    private Integer BelongType;

    @ApiModelProperty("所属目录id")
    private String dirId;

    /**
     * 子节点列表
     */
    @ApiModelProperty("子节点列表")
    private List<DirTreeNode> children = new ArrayList<>();
}
