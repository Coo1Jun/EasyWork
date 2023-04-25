package cn.edu.hzu.client.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  编辑参数对象
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-25
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "Notification编辑", description = "编辑参数")
public class NotificationEditParam {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty("是否已读（0未读 1已读）")
    private int isRead;

    @ApiModelProperty("是否已经除了（0未处理 1拒绝处理 2同意处理）")
    private int isHandle;

    @ApiModelProperty(value = "版本号")
    private Integer version;

}
