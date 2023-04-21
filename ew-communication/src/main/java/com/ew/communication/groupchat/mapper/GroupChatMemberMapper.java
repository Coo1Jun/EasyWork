package com.ew.communication.groupchat.mapper;

import com.ew.communication.groupchat.entity.GroupChatMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-21
 */
public interface GroupChatMemberMapper extends BaseMapper<GroupChatMember> {

    void addUnread(@Param("userId") String userId, @Param("groupChatId") String groupChatId);
}
