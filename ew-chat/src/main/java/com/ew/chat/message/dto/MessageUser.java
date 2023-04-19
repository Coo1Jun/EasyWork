package com.ew.chat.message.dto;

import lombok.Data;

/**
 * @author lzf
 * @create 2023/04/19
 * @description 消息体中的用户实体
 */
@Data
public class MessageUser {
    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String displayName;

    /**
     * 头像(图片路径)
     */
    private String avatar;
}
