package com.sso.server.user.dto;

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
 * @date 2023-02-13
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "User查询", description = "查询参数")
public class UserQueryParam extends PageParam {

}
