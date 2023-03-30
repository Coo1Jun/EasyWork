package com.ew.project.group.enums;

import cn.edu.hzu.common.api.CustomResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupErrorEnum implements CustomResultCode {
    GROUP_NAME_IS_EMPTY(10001, "项目组名称不能为空", null),
    GROUP_NAME_EXIST(10002, "项目组名称【{}】已经存在，不能重复", null),
    NO_PERMISSION(10003, "无权限操作", null),
    ;

    private int code;
    private String msg;
    private Object[] params;

    public GroupErrorEnum setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public GroupErrorEnum setParams(Object[] params) {
        this.params = params;
        return this;
    }
}
