package cn.edu.hzu.client.server;

import cn.edu.hzu.client.dto.NotificationAddParam;
import cn.edu.hzu.client.dto.NotificationEditParam;
import cn.edu.hzu.common.api.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("ew-communication")
@Component
public interface CommunicationFeignClient {

    /**
     * 添加通知
     * @param notificationAddParam
     * @return
     */
    @PostMapping("/api/ew-communication/client/notification/add")
    RestResponse<Boolean> addNotification(@RequestBody NotificationAddParam notificationAddParam);

    /**
     * 修改通知内容（设置已读或设置已处理）
     * @return
     */
    @PutMapping("/api/ew-communication/client/notification/edit")
    RestResponse<Boolean> editNotification(@RequestBody NotificationEditParam notificationEditParam);
}
