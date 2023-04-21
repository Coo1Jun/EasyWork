package com.ew.communication.groupchat.service.impl;

import com.ew.communication.groupchat.entity.GroupChatMember;
import com.ew.communication.groupchat.mapper.GroupChatMemberMapper;
import com.ew.communication.groupchat.service.IGroupChatMemberService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.communication.groupchat.dto.GroupChatMemberQueryParam;
import com.ew.communication.groupchat.dto.GroupChatMemberAddParam;
import com.ew.communication.groupchat.dto.GroupChatMemberEditParam;
import com.ew.communication.groupchat.dto.GroupChatMemberParamMapper;
import com.ew.communication.groupchat.dto.GroupChatMemberDto;
import cn.edu.hzu.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.ResultCode;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class, Error.class})
public class GroupChatMemberServiceImpl extends BaseServiceImpl<GroupChatMemberMapper, GroupChatMember> implements IGroupChatMemberService {

    @Autowired
    private GroupChatMemberParamMapper groupChatMemberParamMapper;

    @Override
    public PageResult<GroupChatMemberDto> pageDto(GroupChatMemberQueryParam groupChatMemberQueryParam) {
        Wrapper<GroupChatMember> wrapper = getPageSearchWrapper(groupChatMemberQueryParam);
        PageResult<GroupChatMemberDto> result = groupChatMemberParamMapper.pageEntity2Dto(page(groupChatMemberQueryParam, wrapper));
        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(GroupChatMemberAddParam groupChatMemberAddParam) {
        GroupChatMember groupChatMember = groupChatMemberParamMapper.addParam2Entity(groupChatMemberAddParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,groupChatMember);
        return save(groupChatMember);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(GroupChatMemberEditParam groupChatMemberEditParam) {
        GroupChatMember groupChatMember = groupChatMemberParamMapper.editParam2Entity(groupChatMemberEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,groupChatMember);
        return updateById(groupChatMember);
    }

    @Override
    public void addUnreadOrSave(String userId, String groupChatId) {
        int count = this.count(Wrappers.<GroupChatMember>lambdaQuery()
                .eq(GroupChatMember::getGroupChatId, groupChatId)
                .eq(GroupChatMember::getUserId, userId));
        if (count > 0) {
            this.baseMapper.addUnread(userId, groupChatId);
        } else {
            GroupChatMember groupChatMember = new GroupChatMember();
            groupChatMember.setGroupChatId(groupChatId);
            groupChatMember.setUnread(1);
            groupChatMember.setUserId(userId);
            this.save(groupChatMember);
        }
    }

    @Override
    public GroupChatMemberDto getDtoById(String id) {
        return groupChatMemberParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<GroupChatMemberDto> rows) {
        return saveBatch(groupChatMemberParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<GroupChatMember> getPageSearchWrapper(GroupChatMemberQueryParam groupChatMemberQueryParam) {
        LambdaQueryWrapper<GroupChatMember> wrapper = Wrappers.<GroupChatMember>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(GroupChatMember.class)) {
            wrapper.orderByDesc(GroupChatMember::getUpdateTime, GroupChatMember::getCreateTime);
        }
        return wrapper;
    }
}
