package com.ew.communication.websocket;

import cn.edu.hzu.client.dto.GroupDto;
import cn.edu.hzu.client.server.service.IProjectClientService;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ew.communication.contact.constants.ContactType;
import com.ew.communication.contact.dto.ContactAddParam;
import com.ew.communication.contact.service.IContactService;
import com.ew.communication.groupchat.service.IGroupChatMemberService;
import com.ew.communication.message.constants.MessageType;
import com.ew.communication.message.dto.MessageDto;
import com.ew.communication.message.dto.MessageParamMapper;
import com.ew.communication.message.entity.Message;
import com.ew.communication.message.service.IMessageService;
import com.ew.communication.utils.WsSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author lzf
 * @create 2023/04/18
 * @description WebSocket控制层
 */
@Controller
@ServerEndpoint("/websocket/{userAndTimeId}")
@CrossOrigin
@Slf4j
public class WebSocketController {
    /**
     * @Autowired private MessageParamMapper messageParamMapper;
     * 使用以上方式注入，属性为空，为什么？
     * 原因解析：
     * 首先我们来从@Autowired和Bean的属性注入角度来看。
     * 1.容器启动的时候，会将每个’单例‘的Bean加载到容器当中。
     * 2.单例Bean的加载有三个步骤。实例化、依赖注入、初始化。而@Autowired的作用就体现于依赖注入当中。
     * 3.我们WebSocketController类中，通过@Autowired对MessageParamMapper进行自动装配。其实他是成功的，假设我们这时候装配的对象是sender1。
     * <p>
     * 接着我们再来从WebSocket的角度来看：
     * 1.我们每建立一个WebSocket链接，都会产生一个新的对象，也就是被@ServerEndpoint修饰的对象。
     * 2.也就是说WebSocket是一个多例的对象而非单例。 也就是说，当建立链接之后创建的WebSocketController实例对象和容器启动的时候，
     * 加载到容器当中的WebSocketController已经不是一个东西了。
     * 3.那么为什么通过@Autowired注入的对象是null呢？
     * 因为@Autowired注解注入对象是在启动的时候就把对象注入，而不是在使用A对象时才把A需要的B对象注入到A中的。
     * 4.因此我们每建立一个WebSocket链接，按照上面的写法，我们是无法注入MessageParamMapper的。
     */

    private static MessageParamMapper messageParamMapper;

    @Autowired
    private void setMessageParamMapper(MessageParamMapper messageParamMapper) {
        WebSocketController.messageParamMapper = messageParamMapper;
    }

    private static IMessageService messageService;

    @Autowired
    private void setMessageParamMapper(IMessageService messageService) {
        WebSocketController.messageService = messageService;
    }

    private static IProjectClientService projectClientService;

    @Autowired
    private void setProjectClientService(IProjectClientService projectClientService) {
        WebSocketController.projectClientService = projectClientService;
    }

    private static IGroupChatMemberService groupChatMemberService;

    @Autowired
    private void setGroupChatMemberMapper(IGroupChatMemberService groupChatMemberService) {
        WebSocketController.groupChatMemberService = groupChatMemberService;
    }

    private static IContactService contactService;

    @Autowired
    private void setContactService(IContactService contactService) {
        WebSocketController.contactService = contactService;
    }

    /**
     * 连接建立成功调用的方法
     * 这里userAndTimeId 是userId + "," + 时间戳
     * 这样设计可以实现多端连接
     */
    @OnOpen
    public void onOpen(@PathParam("userAndTimeId") String userAndTimeId, Session session) {
        WsSessionUtils.putSession(userAndTimeId, session);
        // 保存多端用户
        WsSessionUtils.putMultiUser(userAndTimeId);
        log.info("【websocket】=》用户【{}】连接加入,【{}】", userAndTimeId, session.getId());
    }

    /**
     * 关闭连接的方法
     */
    @OnClose
    public void onClose(Session session) {
        WsSessionUtils.removeSession(session);
        log.info("连接关闭");
    }

    /**
     * 接受到消息的方法
     */
    @OnMessage
    public void onMessage(String jsonStrMsg, Session session) throws IOException {
        if (StringUtils.isNotEmpty(jsonStrMsg)) {
            log.info("接收到消息=》{}", jsonStrMsg);
            MessageDto messageDto = JSON.parseObject(jsonStrMsg, MessageDto.class);
            Message message = messageParamMapper.dto2entity(messageDto);
            message.setFromUserId(messageDto.getFromUser().getId());
            if (ContactType.GROUP.equals(messageDto.getToContactType())) {
                // 根据contactId（项目组id）找出组成员，逐个判断是否在线。在线->转发，不在线->未读数量+1
                List<String> userIds = projectClientService.getUserIdsByGroupId(messageDto.getToContactId());
                if (CollectionUtils.isNotEmpty(userIds)) {
                    for (String userId : userIds) {
                        if (userId.equals(message.getFromUserId())) {
                            continue;
                        }
                        // 逐个判断是否在线
                        if (WsSessionUtils.isUserOnline(userId)) {
                            Set<String> userSet = WsSessionUtils.getMultiUserSet(userId);
                            if (CollectionUtils.isNotEmpty(userSet)) {
                                for (String userAndTimeId : userSet) {
                                    Session targetSession = WsSessionUtils.getSession(userAndTimeId);
                                    // 异步转发消息
                                    targetSession.getAsyncRemote().sendText(jsonStrMsg);
                                }
                            }
                        } else {
                            // 不在线，未读数量+1
                            groupChatMemberService.addUnreadOrSave(userId, messageDto.getToContactId());
                        }
                        ThreadUtil.execAsync(() -> {
                            // 为项目组中的成员添加 聊天窗口
                            ContactAddParam contactAddParam = new ContactAddParam();
                            contactAddParam.setContactId(messageDto.getToContactId());
                            contactAddParam.setFromId(userId);
                            GroupDto groupInfo = projectClientService.getGroupInfoById(messageDto.getToContactId());
                            contactAddParam.setName("【项目组】" + groupInfo.getName());
                            contactAddParam.setType(ContactType.GROUP);
                            contactService.saveByParam(contactAddParam);
                        });
                    }
                }
                // 将消息保存至数据库
                messageService.save(message);
            } else {
                // 判断对方是否在线
                if (WsSessionUtils.isUserOnline(messageDto.getToContactId())) {
                    Set<String> userSet = WsSessionUtils.getMultiUserSet(messageDto.getToContactId());
                    if (CollectionUtils.isNotEmpty(userSet)) {
                        for (String userAndTimeId : userSet) {
                            Session targetSession = WsSessionUtils.getSession(userAndTimeId);
                            // 异步转发消息
                            targetSession.getAsyncRemote().sendText(jsonStrMsg);
                            // 将消息保存至数据库
                            messageService.save(message);
                        }
                    }
                } else {
                    // 将消息保存至数据库 类型改为 unread
                    message.setStatus(MessageType.UNREAD);
                    messageService.save(message);
                }
                ThreadUtil.execAsync(() -> {
                    // 为对方添加联系人
                    ContactAddParam contactAddParam = new ContactAddParam();
                    contactAddParam.setContactId(messageDto.getFromUser().getId());
                    contactAddParam.setFromId(messageDto.getToContactId());
                    contactAddParam.setType(ContactType.PERSON);
                    contactService.saveByParam(contactAddParam);
                });
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        WsSessionUtils.removeSession(session);
        log.error("websocket出现错误");
        error.printStackTrace();
    }
}