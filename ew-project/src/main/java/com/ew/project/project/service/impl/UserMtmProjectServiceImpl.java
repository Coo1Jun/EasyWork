package com.ew.project.project.service.impl;

import com.ew.project.project.entity.UserMtmProject;
import com.ew.project.project.mapper.UserMtmProjectMapper;
import com.ew.project.project.service.IUserMtmProjectService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.project.project.dto.UserMtmProjectQueryParam;
import com.ew.project.project.dto.UserMtmProjectAddParam;
import com.ew.project.project.dto.UserMtmProjectEditParam;
import com.ew.project.project.dto.UserMtmProjectParamMapper;
import com.ew.project.project.dto.UserMtmProjectDto;
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
 * @since 2023-04-06
 */
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = {Exception.class, Error.class})
public class UserMtmProjectServiceImpl extends BaseServiceImpl<UserMtmProjectMapper, UserMtmProject> implements IUserMtmProjectService {

    @Autowired
    private UserMtmProjectParamMapper userMtmProjectParamMapper;
    @Autowired
    private UserMtmProjectMapper userMtmProjectMapper;

    @Override
    public PageResult<UserMtmProjectDto> pageDto(UserMtmProjectQueryParam userMtmProjectQueryParam) {
        Wrapper<UserMtmProject> wrapper = getPageSearchWrapper(userMtmProjectQueryParam);
        PageResult<UserMtmProjectDto> result = userMtmProjectParamMapper.pageEntity2Dto(page(userMtmProjectQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(UserMtmProjectAddParam userMtmProjectAddParam) {
        UserMtmProject userMtmProject = userMtmProjectParamMapper.addParam2Entity(userMtmProjectAddParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,userMtmProject);
        return save(userMtmProject);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(UserMtmProjectEditParam userMtmProjectEditParam) {
        UserMtmProject userMtmProject = userMtmProjectParamMapper.editParam2Entity(userMtmProjectEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,userMtmProject);
        return updateById(userMtmProject);
    }

    @Override
    public List<String> getTabById(String userId) {
        return userMtmProjectMapper.getTabsById(userId);
    }

    @Override
    public UserMtmProjectDto getDtoById(String id) {
        return userMtmProjectParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<UserMtmProjectDto> rows) {
        return saveBatch(userMtmProjectParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<UserMtmProject> getPageSearchWrapper(UserMtmProjectQueryParam userMtmProjectQueryParam) {
        LambdaQueryWrapper<UserMtmProject> wrapper = Wrappers.<UserMtmProject>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(UserMtmProject.class)) {
            wrapper.orderByDesc(UserMtmProject::getUpdateTime, UserMtmProject::getCreateTime);
        }
        return wrapper;
    }
}
