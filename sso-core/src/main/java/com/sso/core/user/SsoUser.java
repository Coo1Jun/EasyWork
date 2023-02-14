package com.sso.core.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * sso user
 */
@Data
public class SsoUser implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * 用户id
     */
    private String userid;


    private String version;
    /**
     * 角色：root、admin、user
     */
    private String role;

    /**
     * 性别
     */
    private String sex;

    /**
     * 用户名，昵称
     */
    private String nickname;

    /**
     * 真实名字
     */
    private String realName;

    /**
     * 英文名称（唯一）
     */
    private String engName;


    /**
     * 邮箱（唯一）
     */
    private String email;

    /**
     * 手机（唯一）
     */
    private String phone;

    /**
     * 出生年月
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date birthDate;

    /**
     * 个人描述
     */
    private String description;

    /**
     * 肖像
     */
    private String portrait;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 设定过期的时间
     */
    private int expireMinute;

    /**
     * 记录创建cookie的时间戳
     */
    private long freshTime;
    private Map<String, String> pluginInfo;

}
