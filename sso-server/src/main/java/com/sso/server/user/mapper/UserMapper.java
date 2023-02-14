package com.sso.server.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.server.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-02-13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
//    User getUserByLogin(@Param("loginName") String loginName);
}
