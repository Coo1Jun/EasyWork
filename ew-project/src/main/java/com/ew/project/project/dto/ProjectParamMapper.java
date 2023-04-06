package com.ew.project.project.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.project.project.entity.Project;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-02
 *
 */
@Mapper(componentModel = "spring",imports = {BaseEntity.class})
public interface  ProjectParamMapper{

    /**
     *
     * 新增参数转换为实体类
     *
     * @param projectAddParam 新增参数
     * @return Project 实体类
     * @date 2023-04-02
     *
     */
    Project addParam2Entity(ProjectAddParam projectAddParam);

    /**
     *
     * 修改参数转换为实体类
     *
     * @param projectEditParam 修改参数
     * @return Project 实体类
     * @date 2023-04-02
     *
     */
    Project editParam2Entity(ProjectEditParam projectEditParam);

    /**
     *
     * 实体类换为Dto
     *
     * @param project 实体类
     * @return ProjectDto DTO
     * @date 2023-04-02
     *
     */
    ProjectDto entity2Dto(Project project);

    /**
     *
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<ProjectDto> 分页DTO
     * @date 2023-04-02
     *
     */
    PageResult<ProjectDto> pageEntity2Dto(PageResult<Project> page);


    /**
     *
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<Project> entity列表
     * @date 2023-04-02
     *
     */
    List<Project> dtoList2Entity(List<ProjectDto> rows);

}
