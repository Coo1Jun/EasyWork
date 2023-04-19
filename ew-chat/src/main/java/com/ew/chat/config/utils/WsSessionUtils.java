package com.ew.chat.config.utils;

import cn.edu.hzu.common.api.utils.StringUtils;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lzf
 * @create 2023/04/18
 * @description WebSocket Session工具类
 */
public class WsSessionUtils {
    // 保存已经连接成功的session
    public static Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public static Session getSession(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        return sessionMap.get(userId);
    }
    public static boolean putSession(String userId, Session session) {
        if (StringUtils.isEmpty(userId) || null == session) {
            return false;
        }
        sessionMap.put(userId, session);
        return true;
    }

    public static boolean removeSession(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return false;
        }
        sessionMap.remove(userId);
        return true;
    }

    /**
     * 判断用户是否在线
     */
    public static boolean isUserOnline(String userId) {
        return sessionMap.containsKey(userId);
    }
}