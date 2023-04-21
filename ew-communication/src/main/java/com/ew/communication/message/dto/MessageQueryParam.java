package com.ew.communication.message.dto;

import cn.edu.hzu.common.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  分页查询参数对象
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Message查询", description = "查询参数")
public class MessageQueryParam extends PageParam {
    @ApiModelProperty("接收消息的联系人ID")
    private String toContactId;

}
