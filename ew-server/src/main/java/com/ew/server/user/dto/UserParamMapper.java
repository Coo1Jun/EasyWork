package com.ew.server.user.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.server.user.entity.User;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-02-13
 *
 */
@Mapper(componentModel = "spring",imports = {BaseEntity.class})
public interface  UserParamMapper{

    /**
     *
     * 新增参数转换为实体类
     *
     * @param userAddParam 新增参数
     * @return User 实体类
     * @date 2023-02-13
     *
     */
    User addParam2Entity(UserAddParam userAddParam);

    /**
     *
     * 修改参数转换为实体类
     *
     * @param userEditParam 修改参数
     * @return User 实体类
     * @date 2023-02-13
     *
     */
    User editParam2Entity(UserEditParam userEditParam);

    /**
     *
     * 实体类换为Dto
     *
     * @param user 实体类
     * @return UserDto DTO
     * @date 2023-02-13
     *
     */
    UserDto entity2Dto(User user);

    /**
     *
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<UserDto> 分页DTO
     * @date 2023-02-13
     *
     */
    PageResult<UserDto> pageEntity2Dto(PageResult<User> page);


    /**
     *
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<User> entity列表
     * @date 2023-02-13
     *
     */
    List<User> dtoList2Entity(List<UserDto> rows);

}
