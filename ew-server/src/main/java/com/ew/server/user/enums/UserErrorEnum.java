package com.ew.server.user.enums;

import cn.edu.hzu.common.api.CustomResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lzf
 * @create 2023/03/03
 * @description 用户服务错误信息
 */
@Getter
@AllArgsConstructor
public enum UserErrorEnum implements CustomResultCode {
    USER_NAME_EMPTY(10001, "用户名为空", null),
    PASSWORD_EMPTY(10002, "密码为空", null),
    VERIFY_CODE_EMPTY(10003,"验证码为空", null),
    /**
     * 邮箱已经被注册
     */
    EMAIL_REGISTERED(10004, "邮箱已经被注册", null),
    /**
     * 验证码不正确
     */
    VERIFY_CODE_ERROR(10005,"验证码不正确", null),
    /**
     * 找不到用户
     */
    USER_EMPTY(10006, "用户不存在", null),
    ;
    private int code;
    private String msg;
    private Object[] params;

    public UserErrorEnum setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public UserErrorEnum setParams(Object[] params) {
        this.params = params;
        return this;
    }
}
