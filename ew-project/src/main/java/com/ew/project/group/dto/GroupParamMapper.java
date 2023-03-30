package com.ew.project.group.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.project.group.entity.Group;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-03-30
 *
 */
@Mapper(componentModel = "spring",imports = {BaseEntity.class})
public interface  GroupParamMapper{

    /**
     *
     * 新增参数转换为实体类
     *
     * @param groupAddParam 新增参数
     * @return Group 实体类
     * @date 2023-03-30
     *
     */
    Group addParam2Entity(GroupAddParam groupAddParam);

    /**
     *
     * 修改参数转换为实体类
     *
     * @param groupEditParam 修改参数
     * @return Group 实体类
     * @date 2023-03-30
     *
     */
    Group editParam2Entity(GroupEditParam groupEditParam);

    /**
     *
     * 实体类换为Dto
     *
     * @param group 实体类
     * @return GroupDto DTO
     * @date 2023-03-30
     *
     */
    GroupDto entity2Dto(Group group);

    /**
     *
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<GroupDto> 分页DTO
     * @date 2023-03-30
     *
     */
    PageResult<GroupDto> pageEntity2Dto(PageResult<Group> page);


    /**
     *
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<Group> entity列表
     * @date 2023-03-30
     *
     */
    List<Group> dtoList2Entity(List<GroupDto> rows);

}
