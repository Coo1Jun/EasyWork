package com.ew.communication.schedule;

import cn.edu.hzu.client.dto.NotificationAddParam;
import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.client.server.service.ICommunicationClientService;
import cn.edu.hzu.client.server.service.IServerClientService;
import cn.edu.hzu.common.api.utils.DateUtils;
import cn.edu.hzu.common.constant.NotificationType;
import cn.edu.hzu.common.service.MailService;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.communication.calendar.schedule.entity.Schedule;
import com.ew.communication.calendar.schedule.entity.ScheduleOtmUser;
import com.ew.communication.calendar.schedule.mapper.ScheduleOtmUserMapper;
import com.ew.communication.calendar.schedule.service.IScheduleOtmUserService;
import com.ew.communication.calendar.schedule.service.IScheduleService;
import com.ew.communication.calendar.todolist.entity.TodoList;
import com.ew.communication.calendar.todolist.service.ITodoListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author lzf
 * @create 2023/05/01
 * @description 日历模块定时器
 */
@Component
@Slf4j
public class CalendarTask {

    @Autowired
    private IScheduleService scheduleService;
    @Autowired
    private IScheduleOtmUserService scheduleOtmUserService;
    @Autowired
    private ScheduleOtmUserMapper scheduleOtmUserMapper;
    @Autowired
    private ITodoListService todoListService;
    @Autowired
    private ICommunicationClientService communicationClientService;
    @Autowired
    private IServerClientService serverClientService;
    @Autowired
    private MailService mailService;

    private static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM);

    @Scheduled(cron = "0 * * * * ?") // 每分钟的00秒执行一次（即间隔每分钟执行一次）
    public void scheduledMethod() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime now5 = now.plusMinutes(5); // 5分钟后的日期
        LocalDateTime now30 = now.plusMinutes(30); // 30分钟后的日期
        // 查询距离日程开始还有30分钟的日程数据
        List<Schedule> schedule30 = scheduleService.list(Wrappers.<Schedule>lambdaQuery().eq(Schedule::getStartTime, now30.format(format)));
        // 发送日程的通知和邮件提醒
        sendScheduleNoticeAndEmail(schedule30, 30);
        List<Schedule> schedule5 = scheduleService.list(Wrappers.<Schedule>lambdaQuery().eq(Schedule::getStartTime, now5.format(format)));
        // 发送日程的通知和邮件提醒
        sendScheduleNoticeAndEmail(schedule5, 5);
        // 查询还没截止的待办
        List<TodoList> todoLists = todoListService.list(Wrappers.<TodoList>lambdaQuery().gt(TodoList::getEndTime, now.format(format)).eq(TodoList::getIsEnd, 0));
        // 发送待办的通知和邮件提醒
        sendTodoListNoticeAndEmail(todoLists, now);
    }

    /**
     * 发送日程的通知和邮件提醒
     * @param scheduleList 要发送的日程数据
     * @param minutes 距离日程开始时间还有几分钟
     */
    private void sendScheduleNoticeAndEmail(List<Schedule> scheduleList, int minutes) {
        // 开启异步线程发送
        ThreadUtil.execAsync(() -> {
            if (CollectionUtils.isNotEmpty(scheduleList)) {
                for (Schedule schedule : scheduleList) {
                    // 查询该日程的参与人id
                    List<ScheduleOtmUser> users = scheduleOtmUserService.list(Wrappers.<ScheduleOtmUser>lambdaQuery().eq(ScheduleOtmUser::getScheduleId, schedule.getId()));
                    // 发送通知
                    NotificationAddParam notificationAddParam = new NotificationAddParam();
                    notificationAddParam.setType(NotificationType.SCHEDULE);
                    notificationAddParam.setOperationId(schedule.getId());
                    if (CollectionUtils.isNotEmpty(users)) {
                        for (ScheduleOtmUser user : users) {
                            notificationAddParam.setUserId(user.getUserId());
                            communicationClientService.addNotification(notificationAddParam);
                            log.info("日程提醒通知发送成功，用户===》【{}】", user.getUserId());
                        }
                    }
                    // 判断是否开启了邮件提醒
                    if (schedule.getEmailReminder() == 1) {
                        // 查询该日程参与人的邮件信息
                        List<String> userEmails = scheduleOtmUserMapper.getUserEmails(schedule.getId());
                        String subject = "Easy Work 日程提醒";
                        String templateName = "schedule";
                        Context context = new Context();
                        context.setVariable("title", schedule.getTitle());
                        context.setVariable("startTime", DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", schedule.getStartTime()));
                        context.setVariable("endTime", DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", schedule.getEndTime()));
                        context.setVariable("minutes", minutes);
                        try {
                            mailService.sendMail(userEmails, subject, templateName, context);
                            log.info(">>>>>>>>>>>日程提醒邮件发送成功，用户邮箱===》【{}】<<<<<<<<<<<<<<<<", userEmails);
                        } catch (MessagingException e) {
                            log.warn(">>>>>>>>>>>>>>>>日程提醒邮件发送失败<<<<<<<<<<<<<<<<<");
                        }
                    }
                }
            }
        });
    }

    /**
     * 发送待办的通知和邮件提醒
     * @param todoLists 要发送的待办数据
     */
    private void sendTodoListNoticeAndEmail(List<TodoList> todoLists, LocalDateTime now) {
        ThreadUtil.execAsync(() -> {
            if (CollectionUtils.isNotEmpty(todoLists)) {
                for (TodoList todoList : todoLists) {
                    // 判断是否需要发送通知和邮件提醒
                    if (DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM, todoList.getEndTime())
                            .equals(now.plusMinutes(todoList.getReminderTime()).format(format))) {
                        // 发送通知
                        NotificationAddParam notificationAddParam = new NotificationAddParam();
                        notificationAddParam.setType(NotificationType.TODO);
                        notificationAddParam.setOperationId(todoList.getId());
                        notificationAddParam.setUserId(todoList.getCreateId());
                        communicationClientService.addNotification(notificationAddParam);
                        log.info("待办提醒通知发送成功，用户===》【{}】", todoList.getCreateId());
                        if (todoList.getEmailReminder() == 1) {
                            // 发送邮件
                            // 获取用户信息
                            UserDto userDto = serverClientService.getUserDtoById(todoList.getCreateId());
                            String subject = "Easy Work 待办提醒";
                            String templateName = "todo-list";
                            Context context = new Context();
                            context.setVariable("title", todoList.getTitle());
                            context.setVariable("endTime", DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", todoList.getEndTime()));
                            context.setVariable("minutes", todoList.getReminderTime());
                            try {
                                mailService.sendMail(userDto.getEmail(), subject, templateName, context);
                                log.info(">>>>>>>>>>>待办提醒邮件发送成功，用户邮箱===》【{}】<<<<<<<<<<<<<<<<", userDto.getEmail());
                            } catch (MessagingException e) {
                                log.warn(">>>>>>>>>>>>>>>>待办提醒邮件发送失败<<<<<<<<<<<<<<<<<");
                            }
                        }
                    }
                }
            }
        });
    }
}
