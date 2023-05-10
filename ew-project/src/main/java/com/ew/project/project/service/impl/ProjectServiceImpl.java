package com.ew.project.project.service.impl;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.api.utils.UserUtils;
import cn.edu.hzu.common.entity.BaseEntity;
import cn.edu.hzu.common.entity.SsoUser;
import cn.edu.hzu.common.exception.CommonException;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.project.netdisk.dto.NetDiskFileAddParam;
import com.ew.project.netdisk.enums.NetDiskTypeEnum;
import com.ew.project.netdisk.service.INetDiskFileService;
import com.ew.project.project.constants.ProjectConstants;
import com.ew.project.project.dto.*;
import com.ew.project.project.entity.Project;
import com.ew.project.project.enums.ProjectErrorEnum;
import com.ew.project.project.mapper.ProjectMapper;
import com.ew.project.project.service.IProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private INetDiskFileService netDiskFileService;

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
        List<String> tabs = this.baseMapper.getTabsById(curUser.getUserid());
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
        if (result) {
            NetDiskFileAddParam netDiskFileAddParam = new NetDiskFileAddParam();
            netDiskFileAddParam.setBelongType(NetDiskTypeEnum.PROJECT.getCode());
            netDiskFileAddParam.setBelongId(project.getId());
            netDiskFileAddParam.setFileName(project.getName());
            netDiskFileService.addDir(netDiskFileAddParam, true);
        }
        return result;
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
        if (StringUtils.isEmpty(id)) return null;
        return this.baseMapper.getProDtoById(UserUtils.getCurrentUser().getUserid(), id);
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
