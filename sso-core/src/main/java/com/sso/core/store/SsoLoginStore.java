package com.sso.core.store;

import cn.edu.hzu.common.constant.Constant;
import cn.edu.hzu.common.entity.SsoUser;
import com.sso.core.util.JedisUtil;

/**
 * local login store
 *
 *
 */
public class SsoLoginStore {

    private static int redisExpireMinute = 720;    // 720 minute, 12 hour
    public static void setRedisExpireMinute(int redisExpireMinute) {
//        if (redisExpireMinute < 30) {
//            redisExpireMinute = 30;
//        }
        SsoLoginStore.redisExpireMinute = redisExpireMinute;
    }
    public static int getRedisExpireMinute() {
        return redisExpireMinute;
    }

    /**
     * get
     *
     * @param storeKey
     * @return
     */
    public static SsoUser get(String storeKey) {

        String redisKey = redisKey(storeKey);
        Object objectValue = JedisUtil.getObjectValue(redisKey);
        if (objectValue != null) {
            SsoUser user = (SsoUser) objectValue;
            return user;
        }
        return null;
    }

    /**
     * remove
     *
     * @param storeKey
     */
    public static void remove(String storeKey) {
        String redisKey = redisKey(storeKey);
        JedisUtil.del(redisKey);
    }

    /**
     * put
     *
     * @param storeKey
     * @param user
     */
    public static void put(String storeKey, SsoUser user) {
        String redisKey = redisKey(storeKey);
        JedisUtil.setObjectValue(redisKey, user, redisExpireMinute * 60);  // minute to second
    }

    private static String redisKey(String storeKey){
        return Constant.SSO_SESSIONID.concat("#").concat(storeKey);
    }

}
