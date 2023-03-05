package com.ew.server.controller;


import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.ResultCode;
import cn.edu.hzu.common.constant.Constant;
import cn.edu.hzu.common.entity.SsoUser;
import com.ew.server.file.dto.FileMetaDto;
import com.ew.server.file.service.IFileMetaService;
import com.ew.server.user.dto.UserDto;
import com.ew.server.user.service.IUserService;
import com.sso.core.login.SsoWebLoginHelper;
import com.sso.core.store.SsoLoginStore;
import com.sso.core.store.SsoSessionIdHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * sso server (for web)
 */
@Controller
public class WebController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IFileMetaService fileMetaService;

    @GetMapping({"/", "/getCurrentUser"})
    @ResponseBody
    public RestResponse index(HttpServletRequest request, HttpServletResponse response) {

        // login check
        SsoUser ssoUser = SsoWebLoginHelper.loginCheck(request, response);

        if (ssoUser == null) {
            return RestResponse.failed(ResultCode.SSO_LOGIN_FAIL_RESULT.getMsg());
        } else {
            FileMetaDto fileMetaDto = fileMetaService.getDtoById(ssoUser.getPortrait());
            ssoUser.setPortrait(Constant.DEFAULT_USER_PORTRAIT);
            if (fileMetaDto != null) {
                ssoUser.setPortrait(fileMetaDto.getLocation());
            }
            return RestResponse.ok(ssoUser, "用户已登录");
        }
    }

    /**
     * Login
     *
     * @return
     */
    @PostMapping("/doLogin")
    @ResponseBody
    public RestResponse doLogin(@RequestBody LoginInfo loginInfo, HttpServletResponse response) {

        boolean ifRem = "on".equals(loginInfo.getIfRemember());

        // valid login
        RestResponse<UserDto> result = userService.findUser(loginInfo.getUsername(), loginInfo.getPassword());

        if (result.getCode() != ResultCode.SUCCESS.getCode()) {
            return RestResponse.failed(result.getMsg());
        }
        UserDto userDto = result.getData();
        // 1、make sso user
        SsoUser ssoUser = new SsoUser();
        ssoUser.setUserid(userDto.getId());
        ssoUser.setVersion(userDto.getUserVersion());
        ssoUser.setExpireMinute(SsoLoginStore.getRedisExpireMinute());
        ssoUser.setFreshTime(System.currentTimeMillis());
        ssoUser.setBirthDate(userDto.getBirthDate());
        ssoUser.setCreateTime(userDto.getCreateTime());
        ssoUser.setDescription(userDto.getDescription());
        ssoUser.setEmail(userDto.getEmail());
        ssoUser.setPhone(userDto.getPhone());
        ssoUser.setEngName(userDto.getEngName());
        ssoUser.setRealName(userDto.getRealName());
        ssoUser.setNickname(userDto.getNickname());
        ssoUser.setRole(userDto.getRole());
        ssoUser.setSex(userDto.getSex());
        ssoUser.setPortrait(userDto.getPortrait());

        // 2、make session id 也就是cookie的value
        String sessionId = SsoSessionIdHelper.makeSessionId(ssoUser);

        // 3、login, store storeKey + cookie sessionId
        String cookieValue = SsoWebLoginHelper.login(response, sessionId, ssoUser, ifRem);

        return RestResponse.ok(cookieValue, "登录成功");
    }

    /**
     * Logout
     *
     * @param request
     * @return
     */
    @GetMapping(Constant.SSO_LOGOUT)
    @ResponseBody
    public RestResponse logout(HttpServletRequest request, HttpServletResponse response) {

        // logout
        SsoWebLoginHelper.logout(request, response);

        return RestResponse.ok("", "用户登出成功");
    }


}