package com.ew.server.email.enums;

import cn.edu.hzu.common.api.CustomResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lzf
 * @create 2023/03/03
 * @description 邮箱服务错误信息
 */
@Getter
@AllArgsConstructor
public enum EmailErrorEnum implements CustomResultCode {

    /**
     * 邮箱为空
     */
    EMAIL_EMPTY(10001, "邮箱不能为空", null),
    /**
     * 邮箱格式错误
     */
    EMAIL_PATTERN_ERROR(10002, "邮箱{}格式不正确", null),
    /**
     * 邮件发送异常，请稍后重试
     */
    EMAIL_SEND_ERROR(10003, "邮件发送异常，请稍后重试", null),

    ;
    private int code;
    private String msg;
    private Object[] params;

    public EmailErrorEnum setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public EmailErrorEnum setParams(Object[] params) {
        this.params = params;
        return this;
    }
}
