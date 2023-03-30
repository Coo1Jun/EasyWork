package com.ew.project.group.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.project.group.entity.UserMtmGroup;
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
public interface  UserMtmGroupParamMapper{

    /**
     *
     * 新增参数转换为实体类
     *
     * @param userMtmGroupAddParam 新增参数
     * @return UserMtmGroup 实体类
     * @date 2023-03-30
     *
     */
    UserMtmGroup addParam2Entity(UserMtmGroupAddParam userMtmGroupAddParam);

    /**
     *
     * 修改参数转换为实体类
     *
     * @param userMtmGroupEditParam 修改参数
     * @return UserMtmGroup 实体类
     * @date 2023-03-30
     *
     */
    UserMtmGroup editParam2Entity(UserMtmGroupEditParam userMtmGroupEditParam);

    /**
     *
     * 实体类换为Dto
     *
     * @param userMtmGroup 实体类
     * @return UserMtmGroupDto DTO
     * @date 2023-03-30
     *
     */
    UserMtmGroupDto entity2Dto(UserMtmGroup userMtmGroup);

    /**
     *
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<UserMtmGroupDto> 分页DTO
     * @date 2023-03-30
     *
     */
    PageResult<UserMtmGroupDto> pageEntity2Dto(PageResult<UserMtmGroup> page);


    /**
     *
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<UserMtmGroup> entity列表
     * @date 2023-03-30
     *
     */
    List<UserMtmGroup> dtoList2Entity(List<UserMtmGroupDto> rows);

}
