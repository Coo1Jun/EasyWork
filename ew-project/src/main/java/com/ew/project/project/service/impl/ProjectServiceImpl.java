package com.ew.project.project.service.impl;

import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.api.utils.UserUtils;
import cn.edu.hzu.common.entity.SsoUser;
import cn.edu.hzu.common.exception.CommonException;
import com.ew.project.project.constants.ProjectConstants;
import com.ew.project.project.entity.Project;
import com.ew.project.project.enums.ProjectErrorEnum;
import com.ew.project.project.mapper.ProjectMapper;
import com.ew.project.project.service.IProjectService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.project.project.dto.ProjectQueryParam;
import com.ew.project.project.dto.ProjectAddParam;
import com.ew.project.project.dto.ProjectEditParam;
import com.ew.project.project.dto.ProjectParamMapper;
import com.ew.project.project.dto.ProjectDto;
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
 * @since 2023-04-02
 */
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = {Exception.class, Error.class})
public class ProjectServiceImpl extends BaseServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Autowired
    private ProjectParamMapper projectParamMapper;

    @Override
    public PageResult<ProjectDto> pageDto(ProjectQueryParam projectQueryParam) {
        Wrapper<Project> wrapper = getPageSearchWrapper(projectQueryParam);
        PageResult<ProjectDto> result = projectParamMapper.pageEntity2Dto(page(projectQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
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
        Project project = projectParamMapper.addParam2Entity(addParam);
        return save(project);
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
    public ProjectDto getDtoById(String id) {
        return projectParamMapper.entity2Dto(this.getById(id));
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
