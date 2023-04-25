package com.ew.communication.notification.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.communication.notification.entity.Notification;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-25
 */
@Mapper(componentModel = "spring", imports = {BaseEntity.class})
public interface NotificationParamMapper {

    /**
     * 新增参数转换为实体类
     *
     * @param notificationAddParam 新增参数
     * @return Notification 实体类
     * @date 2023-04-25
     */
    Notification addParam2Entity(NotificationAddParam notificationAddParam);

    /**
     * 修改参数转换为实体类
     *
     * @param notificationEditParam 修改参数
     * @return Notification 实体类
     * @date 2023-04-25
     */
    Notification editParam2Entity(NotificationEditParam notificationEditParam);

    /**
     * 实体类换为Dto
     *
     * @param notification 实体类
     * @return NotificationDto DTO
     * @date 2023-04-25
     */
    NotificationDto entity2Dto(Notification notification);

    /**
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<NotificationDto> 分页DTO
     * @date 2023-04-25
     */
    PageResult<NotificationDto> pageEntity2Dto(PageResult<Notification> page);


    /**
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<Notification> entity列表
     * @date 2023-04-25
     */
    List<Notification> dtoList2Entity(List<NotificationDto> rows);

    List<NotificationDto> entityList2Dto(List<Notification> rows);

}
