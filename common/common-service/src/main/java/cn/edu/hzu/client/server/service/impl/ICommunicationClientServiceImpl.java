package cn.edu.hzu.client.server.service.impl;

import cn.edu.hzu.client.AbstractBaseServiceClient;
import cn.edu.hzu.client.dto.NotificationAddParam;
import cn.edu.hzu.client.dto.NotificationEditParam;
import cn.edu.hzu.client.server.CommunicationFeignClient;
import cn.edu.hzu.client.server.service.ICommunicationClientService;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.enums.ProjectEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lzf
 * @create 2023/04/25
 * @description ew-communication服务调用是实现类
 */
@Service
@Slf4j
public class ICommunicationClientServiceImpl extends AbstractBaseServiceClient implements ICommunicationClientService {
    @Autowired
    private CommunicationFeignClient communicationFeignClient;

    @Override
    public RestResponse<Boolean> addNotificationClient(boolean throwEx, NotificationAddParam notificationAddParam) {
        execBefore(ProjectEnum.EW_COMMUNICATION, notificationAddParam);
        RestResponse<Boolean> result = communicationFeignClient.addNotification(notificationAddParam);
        execAfter(throwEx, ProjectEnum.EW_COMMUNICATION, result);
        return result;
    }

    @Override
    public RestResponse<Boolean> editNotificationClient(boolean throwEx, NotificationEditParam notificationEditParam) {
        execBefore(ProjectEnum.EW_COMMUNICATION, notificationEditParam);
        RestResponse<Boolean> result = communicationFeignClient.editNotification(notificationEditParam);
        execAfter(throwEx, ProjectEnum.EW_COMMUNICATION, result);
        return result;
    }
}
