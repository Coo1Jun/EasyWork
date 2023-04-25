package com.ew.communication.notification.dto;

import lombok.Data;

import java.util.List;

/**
 * @author lzf
 * @create 2023/04/25
 * @description 通知返回实体
 */
@Data
public class NotificationResult {

    /**
     * 未读通知的数量
     */
    private int unread;

    /**
     * 通知列表
     */
    private List<NotificationDto> result;
}
