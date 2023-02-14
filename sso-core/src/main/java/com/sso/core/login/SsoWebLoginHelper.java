package com.sso.core.login;

import com.sso.core.conf.Conf;
import com.sso.core.user.SsoUser;
import com.sso.core.util.CookieUtil;
import com.sso.core.store.SsoLoginStore;
import com.sso.core.store.SsoSessionIdHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class SsoWebLoginHelper {

    /**
     * client login
     *
     * @param response
     * @param sessionId
     * @param ifRemember true: cookie not expire, false: expire when browser close （server cookie）
     * @param user
     */
    public static String login(HttpServletResponse response,
                             String sessionId,
                             SsoUser user,
                             boolean ifRemember) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
        }

        SsoLoginStore.put(storeKey, user);
        return CookieUtil.set(response, Conf.SSO_SESSIONID, sessionId, ifRemember);
    }

    /**
     * client logout
     *
     * @param request
     * @param response
     */
    public static void logout(HttpServletRequest request,
                              HttpServletResponse response) {

        String cookieSessionId = CookieUtil.getValue(request, Conf.SSO_SESSIONID);
        if (cookieSessionId == null) {
            return;
        }

        String storeKey = SsoSessionIdHelper.parseStoreKey(cookieSessionId);
        if (storeKey != null) {
            SsoLoginStore.remove(storeKey);
        }

        CookieUtil.remove(request, response, Conf.SSO_SESSIONID);
    }


    /**
     * 登录校验
     *
     * @param request
     * @param response
     * @return
     */
    public static SsoUser loginCheck(HttpServletRequest request, HttpServletResponse response) {
        // 获取cookie的值
        String cookieValue = CookieUtil.getValue(request, Conf.SSO_SESSIONID);

        // cookie user
        SsoUser ssoUser = SsoTokenLoginHelper.loginCheck(cookieValue);
        if (ssoUser != null) {
            return ssoUser;
        }

        // redirect user

        // remove old cookie
        SsoWebLoginHelper.removeSessionIdByCookie(request, response);

        // set new cookie
        String paramSessionId = request.getParameter(Conf.SSO_SESSIONID);
        ssoUser = SsoTokenLoginHelper.loginCheck(paramSessionId);
        if (ssoUser != null) {
            CookieUtil.set(response, Conf.SSO_SESSIONID, paramSessionId, false);    // expire when browser close （client cookie）
            return ssoUser;
        }

        return null;
    }


    /**
     * client logout, cookie only
     *
     * @param request
     * @param response
     */
    public static void removeSessionIdByCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.remove(request, response, Conf.SSO_SESSIONID);
    }

    /**
     * get sessionid by cookie
     *
     * @param request
     * @return
     */
    public static String getSessionIdByCookie(HttpServletRequest request) {
        String cookieSessionId = CookieUtil.getValue(request, Conf.SSO_SESSIONID);
        return cookieSessionId;
    }


}
