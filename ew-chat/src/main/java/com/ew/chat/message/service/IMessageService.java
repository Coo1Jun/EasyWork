package com.ew.chat.message.service;

import com.ew.chat.message.entity.Message;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.chat.message.dto.MessageQueryParam;
import com.ew.chat.message.dto.MessageAddParam;
import com.ew.chat.message.dto.MessageEditParam;
import com.ew.chat.message.dto.MessageDto;
import cn.edu.hzu.common.api.PageResult;
import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-19
 *
 */
public interface IMessageService extends IBaseService<Message> {

    /**
     *
     * 分页查询，返回Dto
     *
     * @param messageQueryParam 查询参数
     * @return MessageDto 查询返回列表实体
     * @since 2023-04-19
     *
     */
    PageResult<MessageDto> pageDto(MessageQueryParam messageQueryParam);

    /**
     *
     * 新增
     *
     * @param messageAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-19
     *
     */
    boolean saveByParam(MessageAddParam messageAddParam);

    /**
     *
     * 根据id查询，转dto
     *
     * @param id 聊天消息实体id
     * @return MessageDto
     * @since 2023-04-19
     *
     */
    MessageDto getDtoById(String id);

    /**
     *
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-19
     *
     */
    boolean saveDtoBatch(List<MessageDto> rows);

    /**
     *
     * 更新
     *
     * @param messageEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-19
     *
     */
    boolean updateByParam(MessageEditParam messageEditParam);
}