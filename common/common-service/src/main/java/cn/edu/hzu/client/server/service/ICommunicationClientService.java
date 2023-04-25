package cn.edu.hzu.client.server.service;

import cn.edu.hzu.client.dto.NotificationAddParam;
import cn.edu.hzu.client.dto.NotificationEditParam;
import cn.edu.hzu.common.api.RestResponse;

public interface ICommunicationClientService {

    /**
     * 添加通知
     * @param throwEx 是否抛出异常（当程序报错时）
     * @param notificationAddParam
     * @return
     */
    RestResponse<Boolean> addNotificationClient(boolean throwEx, NotificationAddParam notificationAddParam);
    default Boolean addNotification(NotificationAddParam notificationAddParam) {
        RestResponse<Boolean> response = addNotificationClient(true, notificationAddParam);
        return response.getData();
    }

    /**
     * 修改通知内容（设置已读或设置已处理）
     * @param throwEx 是否抛出异常（当程序报错时）
     * @param notificationEditParam
     * @return
     */
    RestResponse<Boolean> editNotificationClient(boolean throwEx, NotificationEditParam notificationEditParam);
    default Boolean editNotification(NotificationEditParam notificationEditParam) {
        RestResponse<Boolean> response = editNotificationClient(true, notificationEditParam);
        return response.getData();
    }
}
