package com.ew.project.project.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.project.project.entity.UserMtmProject;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-06
 *
 */
@Mapper(componentModel = "spring",imports = {BaseEntity.class})
public interface  UserMtmProjectParamMapper{

    /**
     *
     * 新增参数转换为实体类
     *
     * @param userMtmProjectAddParam 新增参数
     * @return UserMtmProject 实体类
     * @date 2023-04-06
     *
     */
    UserMtmProject addParam2Entity(UserMtmProjectAddParam userMtmProjectAddParam);

    /**
     *
     * 修改参数转换为实体类
     *
     * @param userMtmProjectEditParam 修改参数
     * @return UserMtmProject 实体类
     * @date 2023-04-06
     *
     */
    UserMtmProject editParam2Entity(UserMtmProjectEditParam userMtmProjectEditParam);

    /**
     *
     * 实体类换为Dto
     *
     * @param userMtmProject 实体类
     * @return UserMtmProjectDto DTO
     * @date 2023-04-06
     *
     */
    UserMtmProjectDto entity2Dto(UserMtmProject userMtmProject);

    /**
     *
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<UserMtmProjectDto> 分页DTO
     * @date 2023-04-06
     *
     */
    PageResult<UserMtmProjectDto> pageEntity2Dto(PageResult<UserMtmProject> page);


    /**
     *
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<UserMtmProject> entity列表
     * @date 2023-04-06
     *
     */
    List<UserMtmProject> dtoList2Entity(List<UserMtmProjectDto> rows);

}
