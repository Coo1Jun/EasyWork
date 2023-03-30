package com.ew.project.group.service.impl;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.api.utils.UserUtils;
import cn.edu.hzu.common.entity.BaseEntity;
import cn.edu.hzu.common.entity.SsoUser;
import cn.edu.hzu.common.exception.CommonException;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.project.group.dto.*;
import com.ew.project.group.entity.Group;
import com.ew.project.group.entity.UserMtmGroup;
import com.ew.project.group.enums.GroupErrorEnum;
import com.ew.project.group.mapper.GroupMapper;
import com.ew.project.group.service.IGroupService;
import com.ew.project.group.service.IUserMtmGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-03-30
 */
@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class, Error.class})
public class GroupServiceImpl extends BaseServiceImpl<GroupMapper, Group> implements IGroupService {

    @Autowired
    private GroupParamMapper groupParamMapper;
    @Autowired
    private IUserMtmGroupService userMtmGroupService;
    @Autowired
    private GroupMapper groupMapper;

    @Override
    public PageResult<GroupDto> pageDto(GroupQueryParam groupQueryParam) {
        Wrapper<Group> wrapper = getPageSearchWrapper(groupQueryParam);
        PageResult<GroupDto> result = groupParamMapper.pageEntity2Dto(page(groupQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @Override
    public PageResult<GroupDto> getList(GroupQueryParam groupQueryParam) {
        groupQueryParam.setLimit(-1); // 设置limit为-1，查所有
        LambdaQueryWrapper<Group> wrapper = Wrappers.<Group>lambdaQuery();
        // 用户自己创建的
        wrapper.eq(Group::getCreateId, UserUtils.getCurrentUser().getUserid());
        wrapper.orderByDesc(Group::getUpdateTime, Group::getCreateTime);
        PageResult<GroupDto> result = groupParamMapper.pageEntity2Dto(page(groupQueryParam, wrapper));
        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @Override
    public PageResult<GroupDto> getJoinedList(GroupQueryParam groupQueryParam) {
        List<Group> joinedList = groupMapper.getJoinedList(UserUtils.getCurrentUser().getUserid());
        if (joinedList != null) {
            ArrayList<GroupDto> result = new ArrayList<>();
            for (Group group : joinedList) {
                result.add(groupParamMapper.entity2Dto(group));
            }
            return PageResult.<GroupDto>builder().records(result).total(result.size()).build();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(GroupAddParam groupAddParam) {
        // 校验参数
        if (StringUtils.isEmpty(groupAddParam.getName())) {
            throw CommonException.builder().resultCode(GroupErrorEnum.GROUP_NAME_IS_EMPTY).build();
        }
        // 获取当前用户
        SsoUser currentUser = UserUtils.getCurrentUser();
        // 校验唯一性
        int count = this.count(Wrappers.<Group>lambdaQuery()
                .eq(Group::getCreateId, currentUser.getUserid())
                .eq(Group::getName, groupAddParam.getName()));
        if (count > 0) {
            // 当前用户已经创建过相同名字的项目组
            throw CommonException.builder()
                    .resultCode(GroupErrorEnum.GROUP_NAME_EXIST.setParams(new Object[]{groupAddParam.getName()})).build();
        }
        Group group = groupParamMapper.addParam2Entity(groupAddParam);
        boolean result = save(group);
        if (!result) return false;
        // 保存用户-项目组对应关系
        UserMtmGroup userMtmGroup = new UserMtmGroup();
        userMtmGroup.setUserId(currentUser.getUserid());
        userMtmGroup.setGroupId(group.getId());
        userMtmGroupService.save(userMtmGroup);
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(GroupEditParam groupEditParam) {
        Group groupFromDB = this.getById(groupEditParam.getId());
        if (groupFromDB == null) return false;
        // 如果该项目组不是由当前用户所创建，则不能编辑
        if (!groupFromDB.getCreateId().equals(UserUtils.getCurrentUser().getUserid())) {
            throw CommonException.builder().resultCode(GroupErrorEnum.NO_PERMISSION).build();
        }
        // 获取当前用户
        SsoUser currentUser = UserUtils.getCurrentUser();
        // 校验唯一性
        int count = this.count(Wrappers.<Group>lambdaQuery()
                .eq(Group::getCreateId, currentUser.getUserid())
                .eq(Group::getName, groupEditParam.getName())
                .ne(Group::getId, groupEditParam.getId())); // 更新时，除去自己本身，在进行校验
        if (count > 0) {
            // 当前用户已经创建过相同名字的项目组
            throw CommonException.builder()
                    .resultCode(GroupErrorEnum.GROUP_NAME_EXIST.setParams(new Object[]{groupEditParam.getName()})).build();
        }
        Group group = groupParamMapper.editParam2Entity(groupEditParam);

        return updateById(group);
    }

    @Override
    public boolean deleteByIds(String[] ids) {
        if (StringUtils.isEmpty(ids)) return false;
        SsoUser curUser = UserUtils.getCurrentUser();
        for (String id : ids) {
            if (StringUtils.isEmpty(id)) continue;
            boolean result = this.remove(Wrappers.<Group>lambdaQuery()
                    .eq(Group::getCreateId, curUser.getUserid())
                    .eq(Group::getId, id));
            if (!result) {
                throw CommonException.builder().resultCode(GroupErrorEnum.NO_PERMISSION).build();
            }
            // 删除对照关系表
            userMtmGroupService.remove(Wrappers.<UserMtmGroup>lambdaQuery()
                    .eq(UserMtmGroup::getUserId, curUser.getUserid())
                    .eq(UserMtmGroup::getGroupId, id));
        }
        return true;
    }

    @Override
    public GroupDto getDtoById(String id) {
        return groupParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<GroupDto> rows) {
        return saveBatch(groupParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<Group> getPageSearchWrapper(GroupQueryParam groupQueryParam) {
        LambdaQueryWrapper<Group> wrapper = Wrappers.<Group>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(Group.class)) {
            wrapper.orderByDesc(Group::getUpdateTime, Group::getCreateTime);
        }
        return wrapper;
    }
}
