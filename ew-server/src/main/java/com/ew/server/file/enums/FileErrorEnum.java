package com.ew.server.file.enums;

import cn.edu.hzu.common.api.CustomResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileErrorEnum implements CustomResultCode {

    FILE_NOT_FOUND(10001, "找不到文件{}", null),
    ;

    private int code;
    private String msg;
    private Object[] params;

    public FileErrorEnum setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public FileErrorEnum setParams(Object[] params) {
        this.params = params;
        return this;
    }
}
