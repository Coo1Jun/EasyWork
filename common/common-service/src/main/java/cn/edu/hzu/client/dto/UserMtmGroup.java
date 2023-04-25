package cn.edu.hzu.client.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-03-30
 *
 */
@Data
public class UserMtmGroup {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 项目组id
     */
    @TableField("group_id")
    private String groupId;

    /**
     * 角色
     */
    @TableField("role")
    private String role;

}
