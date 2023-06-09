package com.ew.server.user.service;

import cn.edu.hzu.common.api.RestResponse;
import com.ew.server.user.dto.*;
import com.ew.server.user.entity.User;
import cn.edu.hzu.common.service.IBaseService;
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
     * 根据用户id集合，获取用户信息列表
     *
     * @param ids 用户id集合
     * @return true - 操作成功 false -操作失败
     * @since 2023-02-13
     *
     */
    List<UserDto> getUserListByIds(List<String> ids);

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

    /**
     * 用户注册
     * @param userRegisterDto 注册实体
     * @return
     */
    boolean register(UserRegisterDto userRegisterDto);

    /**
     * 发送验证码
     * @param registerMail
     * @return
     */
    boolean sendVerifyCode(VerifyEmail registerMail);

    /**
     * 用户找回密码
     * @param userRegisterDto 实体
     * @return
     */
    boolean forgot(UserRegisterDto userRegisterDto);

    /**
     * 用户修改密码
     * @param changePwdDto
     * @return
     */
    boolean changePwd(ChangePwdDto changePwdDto);

    /**
     * 用户修改邮箱
     * @param userRegisterDto
     * @return
     */
    boolean changeEmail(UserRegisterDto userRegisterDto);

    /**
     * 根据邮箱查找用户
     * @param email
     * @return
     */
    UserDto findUserByEmail(String email);
}