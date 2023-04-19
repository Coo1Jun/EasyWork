package com.ew.chat.config.controller;

import com.alibaba.fastjson.JSON;
import com.ew.chat.config.utils.WsSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author lzf
 * @create 2023/04/18
 * @description WebSocket控制层
 */
@Controller
@ServerEndpoint("/websocket/{userId}")
@CrossOrigin
@Slf4j
public class WebSocketController {


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        WsSessionUtils.putSession(userId, session);
        log.info("【websocket】=》用户【{}】连接加入,【{}】", userId, session.getId());
    }

    /**
     * 关闭连接的方法
     */
    @OnClose
    public void onClose(Session session) {
        WsSessionUtils.sessionMap.remove(session);
        log.info("连接关闭");
    }

    /**
     * 接受到消息的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("接收到消息=》{}", message);
        Session session1 = WsSessionUtils.getSession(message);
        if (session1 != session) {
            log.info("给{}发送消息", message);
            session1.getBasicRemote().sendText("我不是" + message);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket出现错误");
        error.printStackTrace();
    }
}