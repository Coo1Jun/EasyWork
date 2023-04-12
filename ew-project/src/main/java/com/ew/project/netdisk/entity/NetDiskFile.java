package com.ew.project.netdisk.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import cn.edu.hzu.common.entity.BaseEntity;

/**
 * <p>
 *
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_net_disk_file")
public class NetDiskFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    @TableField("file_id")
    private String fileId;

    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件名编号（仅当文件出现重名时有值，如hello(1).png ）
     */
    @TableField("file_name_num")
    private Integer fileNameNum;

    /**
     * 文件扩展名
     */
    @TableField("extend_name")
    private String extendName;

    /**
     * 文件路径，如：/全部文件/文件夹
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 是否为目录（0否 1是）
     */
    @TableField("is_dir")
    private Boolean isDir;

    /**
     * 所属类型（0项目 1个人）
     */
    @TableField("belong_type")
    private Integer belongType;

    /**
     * 所属id（项目id或用户id）
     */
    @TableField("belong_id")
    private String belongId;

    /**
     * 删除时间
     */
    @TableField("delete_time")
    private Date deleteTime;

    /**
     * 文件删除的标志，与delete_flag不同
     */
    @TableField("deleted")
    private Integer deleted;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
