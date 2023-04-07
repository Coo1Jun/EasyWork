package com.ew.project.workitem.enums;

import cn.edu.hzu.common.api.CustomResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkItemErrorEnum implements CustomResultCode {

    /**
     * 参数为空
     */
    PARAMETER_EMPTY(10001, "{}不能为空", null),
    /**
     * 计划集标题重复
     */
    PLANS_TITLE_EXIST(10002, "【{}】计划集已经存在", null),
    ;
    private int code;
    private String msg;
    private Object[] params;

    public WorkItemErrorEnum setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public WorkItemErrorEnum setParams(Object[] params) {
        this.params = params;
        return this;
    }
}
