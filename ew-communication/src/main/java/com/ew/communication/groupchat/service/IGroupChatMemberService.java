package com.ew.communication.groupchat.service;

import com.ew.communication.groupchat.entity.GroupChatMember;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.communication.groupchat.dto.GroupChatMemberQueryParam;
import com.ew.communication.groupchat.dto.GroupChatMemberAddParam;
import com.ew.communication.groupchat.dto.GroupChatMemberEditParam;
import com.ew.communication.groupchat.dto.GroupChatMemberDto;
import cn.edu.hzu.common.api.PageResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-21
 */
public interface IGroupChatMemberService extends IBaseService<GroupChatMember> {

    /**
     * 分页查询，返回Dto
     *
     * @param groupChatMemberQueryParam 查询参数
     * @return GroupChatMemberDto 查询返回列表实体
     * @since 2023-04-21
     */
    PageResult<GroupChatMemberDto> pageDto(GroupChatMemberQueryParam groupChatMemberQueryParam);

    /**
     * 新增
     *
     * @param groupChatMemberAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-21
     */
    boolean saveByParam(GroupChatMemberAddParam groupChatMemberAddParam);

    /**
     * 根据id查询，转dto
     *
     * @param id 群聊成员信息实体id
     * @return GroupChatMemberDto
     * @since 2023-04-21
     */
    GroupChatMemberDto getDtoById(String id);

    /**
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-21
     */
    boolean saveDtoBatch(List<GroupChatMemberDto> rows);

    /**
     * 更新
     *
     * @param groupChatMemberEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-21
     */
    boolean updateByParam(GroupChatMemberEditParam groupChatMemberEditParam);

    void addUnreadOrSave(String userId, String groupChatId);
}