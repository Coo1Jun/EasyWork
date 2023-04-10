package com.ew.project.project.mapper;

import com.ew.project.project.dto.ProjectDto;
import com.ew.project.project.dto.ProjectQueryParam;
import com.ew.project.project.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-02
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 根据用户id获取用户加入的所有项目组信息
     * @param userId
     * @param queryParam
     * @return
     */
    List<ProjectDto> getProjectList(@Param("user_id") String userId, @Param("query") ProjectQueryParam queryParam);

    Integer projectListCount(@Param("user_id") String userId, @Param("query") ProjectQueryParam queryParam);

    /**
     * 根据项目id获取项目信息
     */
    ProjectDto getProDtoById(@Param("user_id") String userId, @Param("project_id") String projectId);

    /**
     * 根据创建人的id，查询该用户创建的所有项目标识
     * @param userId
     * @return
     */
    List<String> getTabsById(@Param("user_id") String userId);
}
