package cn.edu.hzu.common.enums;

import cn.edu.hzu.common.api.CustomResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用错误枚举
 *
 * @author lzf
 * @date 2022-09-15 22:10:33
 */
@Getter
@AllArgsConstructor
public enum CommonErrorEnum implements CustomResultCode {

    /**
     * 导入数据为空
     */
    IMPORT_DATA_EMPTY(10000, "导入数据为空！", null),

    /**
     * 没有大于0
     */
    NUM_NOT_GT_ZERO(11001, "数值应大于0！", null),
    /**
     * 没有大于等于0
     */
    NUM_NOT_GE_ZERO(11002, "数值应大于或等于0！", null),
    /**
     * 没有大于目标值
     */
    NUM_NOT_GT_TARGET(11003, "数值应大于{}！", null),
    /**
     * 没有大于或等于目标值
     */
    NUM_NOT_GE_TARGET(11004, "数值应大于或等于{}！", null),
    /**
     * 没有与目标值相等
     */
    NUM_NOT_EQ_TARGET(11005, "数值应与{}相等！", null),

    /**
     * 对象需要为空，但是没有为空
     */
    OBJ_IS_NOT_NULL(12001, "对象需要为空！", null),
    /**
     * 对象不能为空，但是为空
     */
    OBJ_IS_NULL(12002, "对象不能为空！", null),
    // ==================================== 服务调用错误码
    /**
     * 服务间调用失败
     */
    COMMON_CLIENT_SERVICE_ERROR(13001, "服务[{}]调用失败: {}", null),
    ;

    private final int code;
    private String msg;
    private Object[] params;

    public CommonErrorEnum setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public CommonErrorEnum setParams(Object[] params) {
        this.params = params;
        return this;
    }
}
