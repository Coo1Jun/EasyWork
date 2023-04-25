package com.ew.communication.notification.service;

import com.ew.communication.notification.entity.Notification;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.communication.notification.dto.NotificationQueryParam;
import com.ew.communication.notification.dto.NotificationAddParam;
import com.ew.communication.notification.dto.NotificationEditParam;
import com.ew.communication.notification.dto.NotificationDto;
import cn.edu.hzu.common.api.PageResult;

import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-25
 */
public interface INotificationService extends IBaseService<Notification> {

    /**
     * 分页查询，返回Dto
     *
     * @param notificationQueryParam 查询参数
     * @return NotificationDto 查询返回列表实体
     * @since 2023-04-25
     */
    PageResult<NotificationDto> pageDto(NotificationQueryParam notificationQueryParam);

    /**
     * 新增
     *
     * @param notificationAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-25
     */
    boolean saveByParam(NotificationAddParam notificationAddParam);

    /**
     * 根据id查询，转dto
     *
     * @param id 通知实体信息id
     * @return NotificationDto
     * @since 2023-04-25
     */
    NotificationDto getDtoById(String id);

    /**
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-25
     */
    boolean saveDtoBatch(List<NotificationDto> rows);

    /**
     * 更新
     *
     * @param notificationEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-25
     */
    boolean updateByParam(NotificationEditParam notificationEditParam);
}