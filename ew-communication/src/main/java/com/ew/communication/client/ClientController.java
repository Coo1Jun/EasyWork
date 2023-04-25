package com.ew.communication.client;

import cn.edu.hzu.common.api.RestResponse;
import com.ew.communication.notification.dto.NotificationAddParam;
import com.ew.communication.notification.dto.NotificationEditParam;
import com.ew.communication.notification.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lzf
 * @create 2023/04/25
 * @description 服务调用控制器
 */
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private INotificationService notificationService;

    /**
     * 添加通知
     * @param notificationAddParam
     * @return
     */
    @PostMapping("/notification/add")
    public RestResponse<Boolean> addNotification(@RequestBody NotificationAddParam notificationAddParam) {
        return RestResponse.ok(notificationService.saveByParam(notificationAddParam));
    }

    /**
     * 修改通知内容（设置已读或设置已处理）
     * @return
     */
    @PutMapping("/notification/edit")
    public RestResponse<Boolean> setNotificationRead(@RequestBody NotificationEditParam notificationEditParam) {
        return RestResponse.ok(notificationService.updateByParam(notificationEditParam));
    }

}
