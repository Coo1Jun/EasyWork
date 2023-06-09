package cn.edu.hzu.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * <pre>
 *  返回数据模型
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-03-31
 *
 */
@Data
@ApiModel(value = "UserDto", description = "返回数据模型")
public class UserDto {

    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 用户名，昵称
     */
    @ApiModelProperty("用户名，昵称")
    private String nickname;

    /**
     * 真实名字
     */
    @ApiModelProperty("真实名字")
    private String realName;

    /**
     * 英文名称（唯一）
     */
    @ApiModelProperty("英文名称")
    private String engName;

    /**
     * 角色：root、admin、user
     */
    @ApiModelProperty("角色：root、admin、user")
    private String role;

    /**
     * 邮箱（唯一）
     */
    @ApiModelProperty("邮箱（唯一）")
    private String email;

    /**
     * 手机（唯一）
     */
    @ApiModelProperty("手机")
    private String phone;

    /**
     * 出生年月
     */
    @ApiModelProperty("出生年月")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone ="GMT+8" )
    private Date birthDate;

    /**
     * 个人描述
     */
    @ApiModelProperty("个人描述")
    private String description;

    /**
     * 肖像
     */
    @ApiModelProperty("肖像文件id")
    private String portrait;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String sex;

    /**
     * 用户版本（每次修改都会改变）
     */
    @ApiModelProperty("用户版本（每次修改都会改变）")
    private String userVersion;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone ="GMT+8" )
    private Date createTime;

}