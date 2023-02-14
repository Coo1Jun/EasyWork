package cn.edu.hzu.common.enums;

import cn.edu.hzu.common.api.CustomResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PoiErrorEnum implements CustomResultCode {

    COMMON_POI_EXPORT_ERROR(10001, "{}导出异常：{}", null),
    COMMON_POI_EXPORT_CLOSE_STREAM_ERROR(10002, "poi-{} 导出 {} 流关闭异常！", null),
    COMMON_POI_EXPORT_TYPE_ERROR(10003, "导出类型异常！", null),
    ;
    private int code;
    private String msg;
    private Object[] params;

    public PoiErrorEnum setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public PoiErrorEnum setParams(Object[] params) {
        this.params = params;
        return this;
    }
}
