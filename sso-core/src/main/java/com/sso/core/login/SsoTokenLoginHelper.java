package com.sso.core.login;

import cn.edu.hzu.common.constant.Constant;
import cn.edu.hzu.common.entity.SsoUser;
import com.sso.core.store.SsoLoginStore;
import com.sso.core.store.SsoSessionIdHelper;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public class SsoTokenLoginHelper {

    /**
     * client login
     *
     * @param sessionId
     * @param ssoUser
     */
    public static void login(String sessionId, SsoUser ssoUser) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
        }

        SsoLoginStore.put(storeKey, ssoUser);
    }

    /**
     * client logout
     *
     * @param sessionId
     */
    public static void logout(String sessionId) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return;
        }

        SsoLoginStore.remove(storeKey);
    }

    /**
     * client logout
     *
     * @param request
     */
    public static void logout(HttpServletRequest request) {
        String headerSessionId = request.getHeader(Constant.SSO_SESSIONID);
        logout(headerSessionId);
    }


    /**
     * login check
     *
     * @param cookieValue
     * @return
     */
    public static SsoUser loginCheck(String cookieValue) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(cookieValue);
        if (storeKey == null) {
            return null;
        }

        SsoUser user = SsoLoginStore.get(storeKey);
        if (user != null) {
            String version = SsoSessionIdHelper.parseVersion(cookieValue);
            // 比较版本号，版本号不同，说明用户信息修改了
            if (user.getVersion().equals(version)) {

                // 过期时间过半后，自动刷新
                if ((System.currentTimeMillis() - user.getFreshTime()) > (long) user.getExpireMinute() * 60 * 1000 / 2) {
                    user.setFreshTime(System.currentTimeMillis());
                    SsoLoginStore.put(storeKey, user);
                }

                return user;
            }
        }
        return null;
    }


    /**
     * login check
     *
     * @param request
     * @return
     */
    public static SsoUser loginCheck(HttpServletRequest request) {
        String headerSessionId = request.getHeader(Constant.SSO_SESSIONID);
        return loginCheck(headerSessionId);
    }


}
