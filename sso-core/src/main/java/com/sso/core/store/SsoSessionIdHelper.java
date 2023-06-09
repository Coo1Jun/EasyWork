package com.sso.core.store;


import cn.edu.hzu.common.entity.SsoUser;

/**
 * make client sessionId
 * <p>
 * client: cookie = [userid#version]
 * server: redis
 * key = [userid]
 * value = user (user.version, valid this)
 * <p>
 * //   group         The same group shares the login status, Different groups will not interact
 *
 *
 */

public class SsoSessionIdHelper {


    /**
     * make client sessionId
     *
     * @param ssoUser
     * @return
     */
    public static String makeSessionId(SsoUser ssoUser) {
        String sessionId = ssoUser.getUserid().concat("_").concat(ssoUser.getVersion());
        return sessionId;
    }

    /**
     * parse storeKey from sessionId
     *
     * @param cookieValue
     * @return userId
     */
    public static String parseStoreKey(String cookieValue) {
        if (cookieValue != null && cookieValue.contains("_")) {
            String[] sessionIdArr = cookieValue.split("_");
            if (sessionIdArr.length == 2
                    && sessionIdArr[0] != null
                    && sessionIdArr[0].trim().length() > 0) {
                String userId = sessionIdArr[0].trim();
                return userId;
            }
        }
        return null;
    }

    /**
     * parse version from sessionId
     *
     * @param cookieValue
     * @return version
     */
    public static String parseVersion(String cookieValue) {
        if (cookieValue != null && cookieValue.contains("_")) {
            String[] sessionIdArr = cookieValue.split("_");
            if (sessionIdArr.length == 2
                    && sessionIdArr[1] != null
                    && sessionIdArr[1].trim().length() > 0) {
                String version = sessionIdArr[1].trim();
                return version;
            }
        }
        return null;
    }

}
