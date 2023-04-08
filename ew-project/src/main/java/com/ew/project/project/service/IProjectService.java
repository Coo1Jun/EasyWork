package com.ew.project.project.service;

import com.ew.project.project.dto.*;
import com.ew.project.project.entity.Project;
import cn.edu.hzu.common.service.IBaseService;
import cn.edu.hzu.common.api.PageResult;
import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-02
 *
 */
public interface IProjectService extends IBaseService<Project> {

    /**
     *
     * 分页查询，返回Dto
     *
     * @param projectQueryParam 查询参数
     * @return ProjectDto 查询返回列表实体
     * @since 2023-04-02
     *
     */
    PageResult<UserMtmProjectDto> pageDto(ProjectQueryParam projectQueryParam);

    /**
     *
     * 新增
     *
     * @param projectAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-02
     *
     */
    boolean saveByParam(ProjectAddParam projectAddParam);

    /**
     *
     * 根据id查询，转dto
     *
     * @param id 项目信息id
     * @return ProjectDto
     * @since 2023-04-02
     *
     */
    UserMtmProjectDto getDtoById(String id);

    /**
     *
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-02
     *
     */
    boolean saveDtoBatch(List<ProjectDto> rows);

    /**
     *
     * 更新
     *
     * @param projectEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-02
     *
     */
    boolean updateByParam(ProjectEditParam projectEditParam);
}