package com.ew.chat.message.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.chat.message.entity.Message;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-19
 */
@Mapper(componentModel = "spring", imports = {BaseEntity.class})
public interface MessageParamMapper {

    /**
     * 新增参数转换为实体类
     *
     * @param messageAddParam 新增参数
     * @return Message 实体类
     * @date 2023-04-19
     */
    Message addParam2Entity(MessageAddParam messageAddParam);

    /**
     * 修改参数转换为实体类
     *
     * @param messageEditParam 修改参数
     * @return Message 实体类
     * @date 2023-04-19
     */
    Message editParam2Entity(MessageEditParam messageEditParam);

    /**
     * 实体类换为Dto
     *
     * @param message 实体类
     * @return MessageDto DTO
     * @date 2023-04-19
     */
    MessageDto entity2Dto(Message message);

    /**
     * Dto换为实体类
     *
     * @param messageDto dto
     * @return 实体类
     * @date 2023-04-19
     */
    Message dto2entity(MessageDto messageDto);

    /**
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<MessageDto> 分页DTO
     * @date 2023-04-19
     */
    PageResult<MessageDto> pageEntity2Dto(PageResult<Message> page);


    /**
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<Message> entity列表
     * @date 2023-04-19
     */
    List<Message> dtoList2Entity(List<MessageDto> rows);

}
