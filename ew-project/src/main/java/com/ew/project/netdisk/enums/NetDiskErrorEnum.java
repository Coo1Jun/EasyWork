package com.ew.project.netdisk.enums;

import cn.edu.hzu.common.api.CustomResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lzf
 * @create 2023/04/12
 * @description 网盘模块错误信息
 */
@Getter
@AllArgsConstructor
public enum NetDiskErrorEnum implements CustomResultCode {

    /**
     * 文件内容不存在
     */
    FILE_NOT_EXIST(10001, "文件内容不存在！", null),
    ;
    private int code;
    private String msg;
    private Object[] params;

    public NetDiskErrorEnum setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public NetDiskErrorEnum setParams(Object[] params) {
        this.params = params;
        return this;
    }
}
