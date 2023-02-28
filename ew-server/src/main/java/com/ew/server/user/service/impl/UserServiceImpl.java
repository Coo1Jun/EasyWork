package com.ew.server.user.service.impl;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.entity.BaseEntity;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.server.user.dto.*;
import com.ew.server.user.entity.User;
import com.ew.server.user.mapper.UserMapper;
import com.ew.server.user.service.IUserService;
import com.sso.core.util.JasyptEncryptorUtils;
import com.ew.server.user.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-02-13
 *
 */
@Slf4j
@Service
@Transactional(readOnly = true,rollbackFor={Exception.class, Error.class})
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserParamMapper userParamMapper;

    @Override
    public PageResult<UserDto> pageDto(UserQueryParam userQueryParam) {
        Wrapper<User> wrapper = getPageSearchWrapper(userQueryParam);
        PageResult<UserDto> result = userParamMapper.pageEntity2Dto(page(userQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor={Exception.class, Error.class})
    public boolean saveByParam(UserAddParam userAddParam) {
        User user = userParamMapper.addParam2Entity(userAddParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,user);
        return save(user);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor={Exception.class, Error.class})
    public boolean updateByParam(UserEditParam userEditParam) {
        User user = userParamMapper.editParam2Entity(userEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,user);
        return updateById(user);
    }

    @Override
    public RestResponse<UserDto> findUser(String loginName, String password) {
        if (StringUtils.isEmpty(loginName)) {
            return RestResponse.failed("请输入登录名！");
        }
        if (StringUtils.isEmpty(password)) {
            return RestResponse.failed("请输入密码！");
        }
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        wrapper.eq(User::getEngName, loginName)
                .or()
                .eq(User::getEmail, loginName)
                .or()
                .eq(User::getPhone, loginName);
        User user = this.getOne(wrapper);
        if (user != null && password.equals(JasyptEncryptorUtils.decode(user.getPassword()))) {
            UserDto userDto = userParamMapper.entity2Dto(user);
            return RestResponse.ok(userDto);
        }
        return RestResponse.failed("用户名不存在或密码错误！");
    }

    @Override
    public UserDto getDtoById(String id) {
        return userParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor={Exception.class, Error.class})
    public boolean saveDtoBatch(List<UserDto> rows) {
        return saveBatch(userParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<User> getPageSearchWrapper(UserQueryParam userQueryParam) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            if(BaseEntity.class.isAssignableFrom(User.class)){
            wrapper.orderByDesc(User::getUpdateTime,User::getCreateTime);
        }
        return wrapper;
    }
}
