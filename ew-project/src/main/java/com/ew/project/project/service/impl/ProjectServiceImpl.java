package com.ew.project.project.service.impl;

import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.api.utils.UserUtils;
import cn.edu.hzu.common.entity.SsoUser;
import cn.edu.hzu.common.exception.CommonException;
import com.alibaba.fastjson.JSONObject;
import com.ew.project.group.dto.UserMtmGroupDto;
import com.ew.project.project.constants.ProjectConstants;
import com.ew.project.project.dto.*;
import com.ew.project.project.entity.Project;
import com.ew.project.project.entity.UserMtmProject;
import com.ew.project.project.enums.ProjectErrorEnum;
import com.ew.project.project.mapper.ProjectMapper;
import com.ew.project.project.mapper.UserMtmProjectMapper;
import com.ew.project.project.service.IProjectService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.project.project.service.IUserMtmProjectService;
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
 * @since 2023-04-02
 */
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = {Exception.class, Error.class})
public class ProjectServiceImpl extends BaseServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Autowired
    private ProjectParamMapper projectParamMapper;
    @Autowired
    private IUserMtmProjectService userMtmProjectService;
    @Autowired
    private UserMtmProjectMapper userMtmProjectMapper;

    @Override
    public PageResult<ProjectDto> pageDto(ProjectQueryParam queryParam) {
        queryParam.setOffset((queryParam.getPageNo() - 1) * queryParam.getLimit());
//        List<UserMtmProjectDto> result = userMtmProjectMapper.getProjectList(UserUtils.getCurrentUser().getUserid(), queryParam);
        List<ProjectDto> result = this.baseMapper.getProjectList(UserUtils.getCurrentUser().getUserid(), queryParam);
        if (result == null) return null;
        Integer total = this.baseMapper.projectListCount(UserUtils.getCurrentUser().getUserid(), queryParam);
        return PageResult.<ProjectDto>builder().records(result).total(total).build();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(ProjectAddParam addParam) {
        // 校验非空参数
        // 所属项目组
        if (StringUtils.isEmpty(addParam.getGroupId())) {
            throw CommonException.builder()
                    .resultCode(ProjectErrorEnum.PARAMETER_EMPTY.setParams(new Object[]{ProjectConstants.GROUP_ID_PARAM}))
                    .build();
        }
        // 项目名
        if (StringUtils.isEmpty(addParam.getName())) {
            throw CommonException.builder()
                    .resultCode(ProjectErrorEnum.PARAMETER_EMPTY.setParams(new Object[]{ProjectConstants.PROJECT_NAME_PARAM}))
                    .build();
        }
        // 项目标识
        if (StringUtils.isEmpty(addParam.getTab())) {
            throw CommonException.builder()
                    .resultCode(ProjectErrorEnum.PARAMETER_EMPTY.setParams(new Object[]{ProjectConstants.PROJECT_TAB_PARAM}))
                    .build();
        }
        // 获取当前用户
        SsoUser curUser = UserUtils.getCurrentUser();
        // 校验项目标识唯一性
        List<String> tabs = userMtmProjectService.getTabById(curUser.getUserid());
        if (StringUtils.isNotEmpty(tabs)) {
            if (tabs.contains(addParam.getTab())) {
                throw CommonException.builder()
                        .resultCode(ProjectErrorEnum.PROJECT_TAB_EXIST)
                        .build();
            }
        }
        Project project = projectParamMapper.addParam2Entity(addParam);
        log.info("添加项目信息=====》{}", JSONObject.toJSONString(project));
        boolean result = save(project);
        if (!result) return false;
        // 保存用户-项目对照关系
        UserMtmProject userMtmProject = new UserMtmProject();
        userMtmProject.setProjectId(project.getId());
        userMtmProject.setUserId(curUser.getUserid());
        userMtmProjectService.save(userMtmProject);
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(ProjectEditParam projectEditParam) {
        Project project = projectParamMapper.editParam2Entity(projectEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,project);
        return updateById(project);
    }

    @Override
    public UserMtmProjectDto getDtoById(String id) {
        if (StringUtils.isEmpty(id)) return null;
        return userMtmProjectMapper.getProDtoById(UserUtils.getCurrentUser().getUserid(), id);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<ProjectDto> rows) {
        return saveBatch(projectParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<Project> getPageSearchWrapper(ProjectQueryParam projectQueryParam) {
        LambdaQueryWrapper<Project> wrapper = Wrappers.<Project>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(Project.class)) {
            wrapper.orderByDesc(Project::getUpdateTime, Project::getCreateTime);
        }
        return wrapper;
    }
}
