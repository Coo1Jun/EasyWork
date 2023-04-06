package com.ew.project.group.mapper;

import com.ew.project.group.dto.GroupDto;
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
    /**
     * 根据用户id获取用户加入的项目组，不包括自己创建的
     * @param id
     * @return
     */
    List<Group> getJoinedList(@Param("user_id") String id);

    /**
     * 根据用户id获取用户加入的所有项目组
     * @param id
     * @return
     */
    List<GroupDto> getGroupList(@Param("user_id") String id);
}
