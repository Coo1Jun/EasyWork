package com.ew.server.user.service.impl;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.api.utils.UserUtils;
import cn.edu.hzu.common.constant.Constant;
import cn.edu.hzu.common.entity.BaseEntity;
import cn.edu.hzu.common.entity.SsoUser;
import cn.edu.hzu.common.exception.CommonException;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.server.constants.UserRole;
import com.ew.server.email.enums.EmailErrorEnum;
import com.ew.server.email.service.MailService;
import com.ew.server.email.utils.EmailUtil;
import com.ew.server.user.dto.*;
import com.ew.server.user.entity.User;
import com.ew.server.user.enums.UserErrorEnum;
import com.ew.server.user.mapper.UserMapper;
import com.ew.server.user.service.IUserService;
import com.sso.core.store.SsoLoginStore;
import com.sso.core.util.JasyptEncryptorUtils;
import com.sso.core.util.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.ArrayList;
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
        if (userEditParam.getSex() != null) {
            String sex = userEditParam.getSex();
            userEditParam.setSex(("0".equals(sex) || "1".equals(sex)) ? sex : null);
        }
        User user = userParamMapper.editParam2Entity(userEditParam);
        // 更新redis的值
        updateSsoUserFromRedis(user);
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
        if (user == null) {
            return RestResponse.failed("邮箱不存在！");
        }
        if (password.equals(JasyptEncryptorUtils.decode(user.getPassword()))) {
            UserDto userDto = userParamMapper.entity2Dto(user);
            return RestResponse.ok(userDto);
        }
        return RestResponse.failed("密码错误！");
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
        // 密码必须包含字母和数字，且不少于8位
        if (!Constant.PASSWORD_PATTERN.matcher(dto.getPassword()).matches()) {
            throw CommonException.builder().resultCode(UserErrorEnum.PASSWORD_RULE_WRONG).build();
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
        log.info("用户完成注册 ===> {}", user);
        return this.save(user);
    }

    @Override
    public boolean sendVerifyCode(VerifyEmail verifyEmail) {
        // 参数非空校验
        if (StringUtils.isEmpty(verifyEmail.getEmail())) {
            throw CommonException.builder().resultCode(EmailErrorEnum.EMAIL_EMPTY).build();
        }
        // 验证邮箱格式是否正确
        checkEmail(verifyEmail.getEmail());
        // 生成验证码
        String code = EmailUtil.generateCode();
        // 先发送邮件，如果邮件发送过程中出现错误，验证码也不会保存
        Context context = new Context();
        context.setVariable("code", code);
        try {
            mailService.sendMail(verifyEmail.getEmail(), Constant.PROJECT_NAME, verifyEmail.getType(), context);
        } catch (MessagingException e) {
            throw CommonException.builder().resultCode(EmailErrorEnum.EMAIL_SEND_ERROR).build();
        }
        // 再将验证码保存至redis，key为邮箱，保存5分钟
        JedisUtil.setStringValue(verifyEmail.getEmail(), code, 300);
        log.info("邮箱 ===> [{}]，验证码 ===> [{}]", verifyEmail.getEmail(), code);
        return true;
    }

    @Override
    public boolean forgot(UserRegisterDto dto) {
        // 参数非空校验
        if (StringUtils.isEmpty(dto.getPassword())) {
            throw CommonException.builder().resultCode(UserErrorEnum.PASSWORD_EMPTY).build();
        }
        if (StringUtils.isEmpty(dto.getEmail())) {
            throw CommonException.builder().resultCode(EmailErrorEnum.EMAIL_EMPTY).build();
        }
        if (StringUtils.isEmpty(dto.getCode())) {
            throw CommonException.builder().resultCode(UserErrorEnum.VERIFY_CODE_EMPTY).build();
        }
        // 验证邮箱格式是否正确
        checkEmail(dto.getEmail());
        // 判断邮箱是否被注册
        Integer count = this.getBaseMapper()
                .selectCount(Wrappers.<User>lambdaQuery().eq(User::getEmail, dto.getEmail()));
        // 如果邮箱没有被注册，则不能找回密码
        if (!(count > 0)) {
            throw CommonException.builder().resultCode(UserErrorEnum.USER_EMPTY).build();
        }
        // 判断验证码是否正确
        String codeInRedis = JedisUtil.getStringValue(dto.getEmail());
        if (!dto.getCode().equals(codeInRedis)) {
            throw CommonException.builder().resultCode(UserErrorEnum.VERIFY_CODE_ERROR).build();
        }
        // 验证成功，删除验证码
        JedisUtil.del(dto.getEmail());
        // 找回密码
        this.getBaseMapper().update(null, Wrappers.<User>lambdaUpdate()
                .set(User::getPassword, JasyptEncryptorUtils.encode(dto.getPassword()))
                .eq(User::getEmail, dto.getEmail()));
        log.info("用户邮箱 ===> [{}]，成功更新密码", dto.getEmail());
        return true;
    }

    @Override
    public boolean changePwd(ChangePwdDto changePwdDto) {
        // 判空
        if (StringUtils.isEmpty(changePwdDto.getNewPassword()) || StringUtils.isEmpty(changePwdDto.getOldPassword())) {
            throw CommonException.builder().resultCode(UserErrorEnum.PASSWORD_EMPTY).build();
        }
        // 根据id取出用户旧密码
        User user = this.getById(changePwdDto.getId());
        // 旧密码不相等
        if (!JasyptEncryptorUtils.decode(user.getPassword()).equals(changePwdDto.getOldPassword())) {
            throw CommonException.builder().resultCode(UserErrorEnum.PASSWORD_WRONG).build();
        }
        // 密码必须包含字母和数字，且不少于8位
        if (!Constant.PASSWORD_PATTERN.matcher(changePwdDto.getNewPassword()).matches()) {
            throw CommonException.builder().resultCode(UserErrorEnum.PASSWORD_RULE_WRONG).build();
        }
        // 修改密码
        this.getBaseMapper().update(null, Wrappers.<User>lambdaUpdate()
                .set(User::getPassword, JasyptEncryptorUtils.encode(changePwdDto.getNewPassword()))
                .eq(User::getId, changePwdDto.getId()));
        log.info("用户id ===> [{}]，成功更新密码", changePwdDto.getId());
        return true;
    }

    @Override
    public boolean changeEmail(UserRegisterDto dto) {
        // 获取当前用户
        SsoUser currentUser = UserUtils.getCurrentUser();
        // 参数非空校验
        if (StringUtils.isEmpty(dto.getEmail())) {
            throw CommonException.builder().resultCode(EmailErrorEnum.EMAIL_EMPTY).build();
        }
        if (StringUtils.isEmpty(dto.getCode())) {
            throw CommonException.builder().resultCode(UserErrorEnum.VERIFY_CODE_EMPTY).build();
        }
        // 验证新邮箱格式是否正确
        checkEmail(dto.getEmail());
        // 判断新邮箱是否被注册
        Integer count = this.getBaseMapper()
                .selectCount(Wrappers.<User>lambdaQuery().eq(User::getEmail, dto.getEmail()));
        // 如果新邮箱已经被注册，则不能被更换
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
        // 修改邮箱
        this.getBaseMapper().update(null, Wrappers.<User>lambdaUpdate()
                .set(User::getEmail, dto.getEmail())
                .eq(User::getId, currentUser.getUserid()));
        log.info("用户id ===> [{}]，成功更新邮箱 ===> [{}]", currentUser.getUserid(), dto.getEmail());
        // 修改redis
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setId(currentUser.getUserid());
        updateSsoUserFromRedis(user);
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

    @Override
    public List<UserDto> getUserListByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        List<UserDto> result = new ArrayList<>();
        for (String id : ids) {
            if (StringUtils.isNotEmpty(id)) {
                result.add(getDtoById(id));
            }
        }
        return result;
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

    /**
     * 从redis更新ssoUser
     * @param user
     */
    private void updateSsoUserFromRedis(User user) {
        String redisKey = Constant.SSO_SESSIONID.concat("#").concat(user.getId());
        SsoUser ssoUserFromRedis = null;
        Object objectValue = JedisUtil.getObjectValue(redisKey);
        long second = JedisUtil.ttlObjectKey(redisKey);
        log.info("key:{} 剩余时间：{}s", redisKey, second);
        if (second == -1) {
            second = SsoLoginStore.getRedisExpireMinute() * 60;
        }
        if (objectValue != null) {
            ssoUserFromRedis = (SsoUser) objectValue;
        }
        if (ssoUserFromRedis != null) {
            SsoUser newSsoUser = new SsoUser();
            newSsoUser.setUserid(user.getId());
            newSsoUser.setVersion(ssoUserFromRedis.getVersion());
            newSsoUser.setExpireMinute(ssoUserFromRedis.getExpireMinute());
            newSsoUser.setFreshTime(ssoUserFromRedis.getFreshTime());

            newSsoUser.setBirthDate(user.getBirthDate() == null ? ssoUserFromRedis.getBirthDate() : user.getBirthDate());
            newSsoUser.setCreateTime(ssoUserFromRedis.getCreateTime());
            newSsoUser.setDescription(user.getDescription() == null ? ssoUserFromRedis.getDescription() : user.getDescription());
            newSsoUser.setEmail(user.getEmail() == null ? ssoUserFromRedis.getEmail() : user.getEmail());
            newSsoUser.setPhone(user.getPhone() == null ? ssoUserFromRedis.getPhone() : user.getPhone());
            newSsoUser.setEngName(user.getEngName() == null ? ssoUserFromRedis.getEngName() : user.getEngName());
            newSsoUser.setRealName(user.getRealName() == null ? ssoUserFromRedis.getRealName() : user.getRealName());
            newSsoUser.setNickname(user.getNickname() == null ? ssoUserFromRedis.getNickname() : user.getNickname());
            newSsoUser.setRole(user.getRole() == null ? ssoUserFromRedis.getRole() : user.getRole());
            newSsoUser.setSex(user.getSex() == null ? ssoUserFromRedis.getSex() : user.getSex());
            newSsoUser.setPortrait(user.getPortrait() == null ? ssoUserFromRedis.getPortrait() : user.getPortrait());

            JedisUtil.setObjectValue(redisKey, newSsoUser, (int)second);
            log.info("redis里的ssoUser已更新 ===》 {}", newSsoUser);
        }
    }
}
