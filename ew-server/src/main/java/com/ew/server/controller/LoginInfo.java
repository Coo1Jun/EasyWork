package com.ew.server.controller;

import lombok.Data;

/**
 * @author lzf
 * @create 2023/02/13
 * @description 登录信息
 */
@Data
public class LoginInfo {
    private String username;
    private String password;
    private String ifRemember;
}
