package com.ew.communication.utils;

import cn.edu.hzu.common.api.utils.StringUtils;

import javax.websocket.Session;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lzf
 * @create 2023/04/18
 * @description WebSocket Session工具类
 */
public class WsSessionUtils {
    /**
     *  保存已经连接成功的session，key：userId + "," + 时间戳
     */
    public static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    /**
     * 保存session对应的userid，key：session的id，value：userId + "," + 时间戳
     */
    public static Map<String, String> userMap = new ConcurrentHashMap<>();

    /**
     * 保存用户id对应的多个不用地点登录的WebSocket，key：userId，value：userId + "," + 时间戳的集合
     */
    public static Map<String, Set<String>> multiUserMap = new ConcurrentHashMap<>();

    /**
     * 根据userAndTimeId获取session
     * @param userAndTimeId
     * @return
     */
    public static Session getSession(String userAndTimeId) {
        if (StringUtils.isEmpty(userAndTimeId)) {
            return null;
        }
        return sessionMap.get(userAndTimeId);
    }

    /**
     * 保存session
     * @param userAndTimeId
     * @param session
     * @return
     */
    public static boolean putSession(String userAndTimeId, Session session) {
        if (StringUtils.isEmpty(userAndTimeId) || null == session) {
            return false;
        }
        sessionMap.put(userAndTimeId, session);
        userMap.put(session.getId(), userAndTimeId);
        return true;
    }

    /**
     * 保存多端用户的信息
     * @param userAndTimeId
     * @return
     */
    public static boolean putMultiUser(String userAndTimeId) {
        if (StringUtils.isEmpty(userAndTimeId)) {
            return false;
        }
        String[] split = userAndTimeId.split(",");
        String userId = split[0];
        if (StringUtils.isEmpty(userId)) {
            return false;
        }
        Set<String> userSet = multiUserMap.get(userId);
        if (userSet == null) {
            userSet = new HashSet<>();
            userSet.add(userAndTimeId);
            multiUserMap.put(userId, userSet);
        }
        userSet.add(userAndTimeId);
        return true;
    }

    /**
     * 获取多段用户的userAndTimeId集合
     */
    public static Set<String> getMultiUserSet(String userId) {
        if (StringUtils.isEmpty(userId)) return null;
        return multiUserMap.get(userId);
    }

    /**
     * 根据userAndTimeId删除session
     * @param userAndTimeId
     * @return
     */
    public static boolean removeSession(String userAndTimeId) {
        if (StringUtils.isEmpty(userAndTimeId)) {
            return false;
        }
        Session session = sessionMap.get(userAndTimeId);
        sessionMap.remove(userAndTimeId);
        userMap.remove(session.getId());
        return true;
    }

    /**
     * 删除session
     * @param session
     * @return
     */
    public static boolean removeSession(Session session) {
        if (session == null) {
            return false;
        }
        String userAndTimeId = userMap.get(session.getId());
        if (StringUtils.isEmpty(userAndTimeId)) {
            return false;
        }
        sessionMap.remove(userAndTimeId);
        userMap.remove(session.getId());
        // 获取用户id
        String[] split = userAndTimeId.split(",");
        String userId = split[0];
        if (StringUtils.isEmpty(userId)) {
            return false;
        }
        // 根据用户id获取用户集合
        Set<String> userSet = multiUserMap.get(userId);
        if (userSet != null) {
            userSet.remove(userAndTimeId);
        }
        return true;
    }

    /**
     * 判断用户是否在线
     */
    public static boolean isUserOnline(String userId) {
        Set<String> userSet = multiUserMap.get(userId);
        return userSet != null && userSet.size() > 0;
    }
}