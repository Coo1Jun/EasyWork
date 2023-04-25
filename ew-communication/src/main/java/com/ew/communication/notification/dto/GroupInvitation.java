package com.ew.communication.notification.dto;

import lombok.Data;

import java.util.List;

/**
 * @author lzf
 * @create 2023/04/25
 * @description 项目组邀请实体
 */
@Data
public class GroupInvitation {
    /**
     * 邮箱
     */
    List<String> emails;

    /**
     * 项目组id
     */
    String groupId;
}
