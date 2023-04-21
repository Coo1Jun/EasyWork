package com.ew.communication.groupchat.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.communication.groupchat.entity.GroupChatMember;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-21
 */
@Mapper(componentModel = "spring", imports = {BaseEntity.class})
public interface GroupChatMemberParamMapper {

    /**
     * 新增参数转换为实体类
     *
     * @param groupChatMemberAddParam 新增参数
     * @return GroupChatMember 实体类
     * @date 2023-04-21
     */
    GroupChatMember addParam2Entity(GroupChatMemberAddParam groupChatMemberAddParam);

    /**
     * 修改参数转换为实体类
     *
     * @param groupChatMemberEditParam 修改参数
     * @return GroupChatMember 实体类
     * @date 2023-04-21
     */
    GroupChatMember editParam2Entity(GroupChatMemberEditParam groupChatMemberEditParam);

    /**
     * 实体类换为Dto
     *
     * @param groupChatMember 实体类
     * @return GroupChatMemberDto DTO
     * @date 2023-04-21
     */
    GroupChatMemberDto entity2Dto(GroupChatMember groupChatMember);

    /**
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<GroupChatMemberDto> 分页DTO
     * @date 2023-04-21
     */
    PageResult<GroupChatMemberDto> pageEntity2Dto(PageResult<GroupChatMember> page);


    /**
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<GroupChatMember> entity列表
     * @date 2023-04-21
     */
    List<GroupChatMember> dtoList2Entity(List<GroupChatMemberDto> rows);

}
