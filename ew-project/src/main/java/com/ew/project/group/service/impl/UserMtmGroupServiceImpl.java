package com.ew.project.group.service.impl;

import cn.edu.hzu.client.dto.FileMetaDto;
import cn.edu.hzu.client.server.service.IServerClientService;
import cn.edu.hzu.common.api.utils.StringUtils;
import com.ew.project.group.dto.*;
import com.ew.project.group.entity.Group;
import com.ew.project.group.entity.UserMtmGroup;
import com.ew.project.group.enums.MemberRoleEnum;
import com.ew.project.group.mapper.UserMtmGroupMapper;
import com.ew.project.group.service.IUserMtmGroupService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import cn.edu.hzu.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.edu.hzu.common.api.PageResult;

import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;



/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-03-30
 *
 */
@Slf4j
@Service
@Transactional(readOnly = true,rollbackFor={Exception.class, Error.class})
public class UserMtmGroupServiceImpl extends BaseServiceImpl<UserMtmGroupMapper, UserMtmGroup> implements IUserMtmGroupService {

    @Autowired
    private UserMtmGroupParamMapper userMtmGroupParamMapper;

    @Autowired
    private UserMtmGroupMapper userMtmGroupMapper;
    @Autowired
    private IServerClientService serverClientService;

    @Override
    public PageResult<UserMtmGroupDto> pageDto(UserMtmGroupQueryParam userMtmGroupQueryParam) {
        Wrapper<UserMtmGroup> wrapper = getPageSearchWrapper(userMtmGroupQueryParam);
        PageResult<UserMtmGroupDto> result = userMtmGroupParamMapper.pageEntity2Dto(page(userMtmGroupQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @Override
    public PageResult<UserMtmGroupDto> memberList(UserMtmGroupQueryParam queryParam) {
        queryParam.setOffset((queryParam.getPageNo() - 1) * queryParam.getLimit());
        List<UserMtmGroupDto> memberList = userMtmGroupMapper.getMemberList(queryParam);
        if (memberList == null) return null;
        for (UserMtmGroupDto dto : memberList) {
            dto.setRole(MemberRoleEnum.getTitle(dto.getRole()));
            dto.setId(dto.getUserId());
            FileMetaDto fileById = serverClientService.getFileById(dto.getAvatar());
            if (fileById != null) {
                dto.setAvatar(fileById.getUrl());
            }
        }
        // 查询总数
        int total = userMtmGroupMapper.memberListCount(queryParam);
        return PageResult.<UserMtmGroupDto>builder().records(memberList).total(total).build();
    }

    @Override
    public List<UserMtmGroupDto> memberList(String groupId) {
        List<UserMtmGroupDto> result = userMtmGroupMapper.getMemberListByGroupId(groupId);
        if (result == null) return null;
        for (UserMtmGroupDto dto : result) {
            dto.setRole(MemberRoleEnum.getTitle(dto.getRole()));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor={Exception.class, Error.class})
    public boolean saveByParam(UserMtmGroupAddParam userMtmGroupAddParam) {
        UserMtmGroup userMtmGroup = userMtmGroupParamMapper.addParam2Entity(userMtmGroupAddParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,userMtmGroup);
        return save(userMtmGroup);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor={Exception.class, Error.class})
    public boolean updateByParam(UserMtmGroupEditParam userMtmGroupEditParam) {
        UserMtmGroup userMtmGroup = userMtmGroupParamMapper.editParam2Entity(userMtmGroupEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,userMtmGroup);
        return updateById(userMtmGroup);
    }

    @Override
    public UserMtmGroupDto getDtoById(String id) {
        return userMtmGroupParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor={Exception.class, Error.class})
    public boolean saveDtoBatch(List<UserMtmGroupDto> rows) {
        return saveBatch(userMtmGroupParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<UserMtmGroup> getPageSearchWrapper(UserMtmGroupQueryParam queryParam) {
        LambdaQueryWrapper<UserMtmGroup> wrapper = Wrappers.<UserMtmGroup>lambdaQuery();
        if(BaseEntity.class.isAssignableFrom(UserMtmGroup.class)){
            wrapper.orderByDesc(UserMtmGroup::getUpdateTime,UserMtmGroup::getCreateTime);
        }
        return wrapper;
    }
}
