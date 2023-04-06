package com.ew.project.project.enums;

import cn.edu.hzu.common.api.CustomResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectErrorEnum implements CustomResultCode {

    /**
     * 参数为空
     */
    PARAMETER_EMPTY(10001, "{}不能为空", null),
    /**
     * 项目标识已经被使用
     */
    PROJECT_TAB_EXIST(10002, "该标识已被使用，请改用其他标识", null),
    ;

    private int code;
    private String msg;
    private Object[] params;

    public ProjectErrorEnum setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public ProjectErrorEnum setParams(Object[] params) {
        this.params = params;
        return this;
    }
}
