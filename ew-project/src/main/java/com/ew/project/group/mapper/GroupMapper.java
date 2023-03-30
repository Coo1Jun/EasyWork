package com.ew.project.group.mapper;

import com.ew.project.group.entity.Group;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-03-30
 */
public interface GroupMapper extends BaseMapper<Group> {
    List<Group> getJoinedList(@Param("user_id") String id);
}
