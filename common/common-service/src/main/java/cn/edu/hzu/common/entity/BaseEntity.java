package cn.edu.hzu.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类
 *
 * @author lzf
 * @date 2022/08/08
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.NONE)
    private String id;

    /** 创建人ID */
    @TableField(value = "create_id" ,fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private String createId;

    /** 创建人 */
    @TableField(value = "create_by", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone ="GMT+8" )
    @TableField(value = "create_time", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

    /** 更新人ID */
    @TableField(value = "update_Id", fill = FieldFill.INSERT_UPDATE)
    private String updateId;

    /** 更新人 */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

//    /** 组织id */
//    @TableField(value = "org_id",fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
//    private String orgId;
//
//    /** 组织 */
//    @TableField(value = "org_by",fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
//    private String orgBy;

    /** 更新时间 */
    @TableField(value = "update_Time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone ="GMT+8" )
    private Date updateTime;

    /** 备注 */
    @TableField("remark")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String remark;

    @TableField(exist = false)
    private Integer isSubmit;//是否提交 1提交,2保存

    /**
     * 乐观锁
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

}
