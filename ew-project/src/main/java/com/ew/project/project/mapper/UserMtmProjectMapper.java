package com.ew.project.project.mapper;

import com.ew.project.project.entity.UserMtmProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-06
 */
public interface UserMtmProjectMapper extends BaseMapper<UserMtmProject> {

    List<String> getTabsById(@Param("user_id") String userId);
}
