package com.ew.project.schedule;

import cn.edu.hzu.client.dto.NotificationAddParam;
import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.client.server.service.ICommunicationClientService;
import cn.edu.hzu.client.server.service.IServerClientService;
import cn.edu.hzu.common.api.utils.DateUtils;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.constant.NotificationType;
import cn.edu.hzu.common.service.MailService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ew.project.project.dto.ProjectDto;
import com.ew.project.project.service.IProjectService;
import com.ew.project.workitem.dto.WorkItemDto;
import com.ew.project.workitem.service.IWorkItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

/**
 * @author lzf
 * @create 2023/04/25
 * @description 工作项定时器
 */
@Component
@Slf4j
public class WorkItemTask {

    @Autowired
    private IWorkItemService workItemService;
    @Autowired
    private ICommunicationClientService communicationClientService;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private IServerClientService serverClientService;
    @Autowired
    private MailService mailService;

//    @Scheduled( cron = "0 0 8 * * ?") // 每天上午8点
//    @Scheduled( cron = "0 48 19 * * ?") // 每天19点48分
    @Scheduled( cron = "0 0 12 * * ?") // 每天中午12点
//    @Scheduled(fixedDelay = 5000) //上一次执行完毕时间点之后5秒再执行
    public void scheduledMethod() {
        log.info("=============>>>>>>> 定时任务 start <<<<<<<<<<<<<<=======================");
        Date now = new Date();
        Date date = DateUtils.addDays(now, 2); // 计算两天后的日期
        // 查询截止日期是 now ~ date 之间的工作项，标识该工作项剩余时间不足2天，则通知对应的负责人
        List<WorkItemDto> list = workItemService.getEndTimeBetween(now, date);
        if (CollectionUtils.isNotEmpty(list)) {
            for (WorkItemDto workItem : list) {
                if (StringUtils.isNotEmpty(workItem.getPrincipalId())) {
                    log.info("==========>>>>> 用户【{}】负责的工作项【{}】即将截止", workItem.getPrincipalId(), workItem.getId());
                    // 发送通知
                    NotificationAddParam notificationAddParam = new NotificationAddParam();
                    notificationAddParam.setType(NotificationType.WARN);
                    notificationAddParam.setUserId(workItem.getPrincipalId());
                    notificationAddParam.setOperationId(workItem.getId());
                    communicationClientService.addNotification(notificationAddParam);
                    // 获取项目信息
                    ProjectDto projectDto = projectService.getDtoById(workItem.getProjectId());
                    // 获取用户信息，从而获取用户邮箱
                    UserDto userDto = serverClientService.getUserDtoById(workItem.getPrincipalId());
                    // 发送邮件
                    String subject = "Easy Work 预警";
                    String templateName = "work-item-warning";
                    Context context = new Context();
                    context.setVariable("projectTab", projectDto.getProjectTab());
                    context.setVariable("number", workItem.getNumber());
                    context.setVariable("workItemTitle", workItem.getTitle());
                    context.setVariable("endTime", DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", workItem.getEndTime()));
                    try {
                        mailService.sendMail(userDto.getEmail(), subject, templateName, context);
                        log.info(">>>>>>>>>>>邮件预警发送成功，用户邮箱===》【{}】<<<<<<<<<<<<<<<<", userDto.getEmail());
                    } catch (MessagingException e) {
                        log.warn(">>>>>>>>>>>>>>>>邮件预警发送失败<<<<<<<<<<<<<<<<<");
                    }
                }
            }
        }
        log.info("=============>>>>>>> 定时任务 end <<<<<<<<<<<<<<=======================");
    }
}
