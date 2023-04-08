package com.ew.project.project.mapper;

import com.ew.project.project.dto.ProjectQueryParam;
import com.ew.project.project.dto.UserMtmProjectDto;
import com.ew.project.project.dto.UserMtmProjectQueryParam;
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

    /**
     * 根据用户id获取用户加入的所有项目组信息
     * @param userId
     * @param queryParam
     * @return
     */
    List<UserMtmProjectDto> getProjectList(@Param("user_id") String userId, @Param("query") ProjectQueryParam queryParam);

    Integer projectListCount(@Param("user_id") String userId, @Param("query") ProjectQueryParam queryParam);

    /**
     * 根据项目id获取项目信息
     */
    UserMtmProjectDto getProDtoById(@Param("user_id") String userId, @Param("project_id") String projectId);
}
