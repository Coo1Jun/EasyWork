package com.sso.server.user.service;

import cn.edu.hzu.common.api.RestResponse;
import com.sso.server.user.entity.User;
import cn.edu.hzu.common.service.IBaseService;
import com.sso.server.user.dto.UserQueryParam;
import com.sso.server.user.dto.UserAddParam;
import com.sso.server.user.dto.UserEditParam;
import com.sso.server.user.dto.UserDto;
import cn.edu.hzu.common.api.PageResult;
import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-02-13
 *
 */
public interface IUserService extends IBaseService<User> {

    /**
     *
     * 分页查询，返回Dto
     *
     * @param userQueryParam 查询参数
     * @return UserDto 查询返回列表实体
     * @since 2023-02-13
     *
     */
    PageResult<UserDto> pageDto(UserQueryParam userQueryParam);

    /**
     *
     * 新增
     *
     * @param userAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-02-13
     *
     */
    boolean saveByParam(UserAddParam userAddParam);

    /**
     *
     * 根据id查询，转dto
     *
     * @param id 用户信息id
     * @return UserDto
     * @since 2023-02-13
     *
     */
    UserDto getDtoById(String id);

    /**
     *
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-02-13
     *
     */
    boolean saveDtoBatch(List<UserDto> rows);

    /**
     *
     * 更新
     *
     * @param userEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-02-13
     *
     */
    boolean updateByParam(UserEditParam userEditParam);

    RestResponse<UserDto> findUser(String loginName, String password);
}