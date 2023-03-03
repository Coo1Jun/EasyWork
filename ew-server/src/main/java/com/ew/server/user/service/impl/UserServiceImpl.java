package com.ew.server.user.service.impl;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.constant.Constant;
import cn.edu.hzu.common.entity.BaseEntity;
import cn.edu.hzu.common.exception.CommonException;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.server.constants.UserRole;
import com.ew.server.email.EmailTemplates;
import com.ew.server.email.enums.EmailErrorEnum;
import com.ew.server.email.service.MailService;
import com.ew.server.email.utils.EmailUtil;
import com.ew.server.user.dto.*;
import com.ew.server.user.entity.User;
import com.ew.server.user.enums.UserErrorEnum;
import com.ew.server.user.mapper.UserMapper;
import com.ew.server.user.service.IUserService;
import com.sso.core.util.JasyptEncryptorUtils;
import com.sso.core.util.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


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
@Transactional(rollbackFor={Exception.class, Error.class})
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserParamMapper userParamMapper;
    @Autowired
    MailService mailService;

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
            return RestResponse.failed("请输入邮箱！");
        }
        if (StringUtils.isEmpty(password)) {
            return RestResponse.failed("请输入密码！");
        }
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        wrapper.eq(User::getEmail, loginName);
        User user = this.getOne(wrapper);
        if (user != null && password.equals(JasyptEncryptorUtils.decode(user.getPassword()))) {
            UserDto userDto = userParamMapper.entity2Dto(user);
            return RestResponse.ok(userDto);
        }
        return RestResponse.failed("用户名不存在或密码错误！");
    }

    @Override
    public boolean register(UserRegisterDto dto) {
        // 参数非空校验
        if (StringUtils.isEmpty(dto.getEmail())) {
            throw CommonException.builder().resultCode(EmailErrorEnum.EMAIL_EMPTY).build();
        }
        if (StringUtils.isEmpty(dto.getRealName())) {
            throw CommonException.builder().resultCode(UserErrorEnum.USER_NAME_EMPTY).build();
        }
        if (StringUtils.isEmpty(dto.getPassword())) {
            throw CommonException.builder().resultCode(UserErrorEnum.PASSWORD_EMPTY).build();
        }
        if (StringUtils.isEmpty(dto.getCode())) {
            throw CommonException.builder().resultCode(UserErrorEnum.VERIFY_CODE_EMPTY).build();
        }
        // 验证邮箱格式是否正确
        checkEmail(dto.getEmail());
        // 判断邮箱是否被注册
        Integer count = this.getBaseMapper()
                .selectCount(Wrappers.<User>lambdaQuery().eq(User::getEmail, dto.getEmail()));
        if (count > 0) {
            throw CommonException.builder().resultCode(UserErrorEnum.EMAIL_REGISTERED).build();
        }
        // 判断验证码是否正确
        String codeInRedis = JedisUtil.getStringValue(dto.getEmail());
        if (!dto.getCode().equals(codeInRedis)) {
            throw CommonException.builder().resultCode(UserErrorEnum.VERIFY_CODE_ERROR).build();
        }
        // 验证成功，删除验证码
        JedisUtil.del(dto.getEmail());
        // 完成注册
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(JasyptEncryptorUtils.encode(dto.getPassword()));
        user.setRealName(dto.getRealName());
        user.setRole(UserRole.USER);
        user.setUserVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        return this.save(user);
    }

    @Override
    public boolean sendVerifyCode(RegisterMail registerMail) {
        // 参数非空校验
        if (StringUtils.isEmpty(registerMail.getEmail())) {
            throw CommonException.builder().resultCode(EmailErrorEnum.EMAIL_EMPTY).build();
        }
        // 验证邮箱格式是否正确
        checkEmail(registerMail.getEmail());
        // 生成验证码
        String code = EmailUtil.generateCode();
        // 先发送邮件，如果邮件发送过程中出现错误，验证码也不会保存
        Context context = new Context();
        context.setVariable("code", code);
        try {
            mailService.sendMail(registerMail.getEmail(), Constant.PROJECT_NAME, EmailTemplates.REGISTER, context);
        } catch (MessagingException e) {
            throw CommonException.builder().resultCode(EmailErrorEnum.EMAIL_SEND_ERROR).build();
        }
        // 再将验证码保存至redis，key为邮箱，保存5分钟
        JedisUtil.setStringValue(registerMail.getEmail(), code, 300);
        return true;
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

    /**
     * 校验邮箱
     * @param email
     */
    private void checkEmail(String email) {
        // 验证邮箱格式是否正确
        if (!EmailUtil.isValidEmail(email)) {
            throw CommonException
                    .builder()
                    .resultCode(EmailErrorEnum.EMAIL_PATTERN_ERROR
                            .setParams(new Object[]{email}))
                    .build();
        }
    }
}
