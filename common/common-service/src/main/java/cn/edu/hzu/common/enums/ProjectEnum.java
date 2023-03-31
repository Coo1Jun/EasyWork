package cn.edu.hzu.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统参数配置
 *
 */
@AllArgsConstructor
@Getter
public enum ProjectEnum {


    EW_SERVER("EW_SERVER","主服务"),
    EW_PROJECT("EW_PROJECT","项目服务"),
    ;

    private String code;
    private String msg;

}
