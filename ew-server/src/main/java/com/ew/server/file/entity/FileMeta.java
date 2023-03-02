package com.ew.server.file.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-03-01
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_file_meta")
public class FileMeta extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 描述文件的名称，包括文件扩展名。
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 描述文件的名称，包括文件扩展名。
     */
    @TableField("original_filename")
    private String originalFilename;

    /**
     * 描述文件的大小，以字节为单位。
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 描述文件存储在对象存储中的路径。
     */
    @TableField("location")
    private String location;

    /**
     * 描述文件的详细信息，例如作者、关键字等。
     */
    @TableField("description")
    private String description;

    /**
     * 文件的所有者
     */
    @TableField("owner")
    private String owner;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

}
