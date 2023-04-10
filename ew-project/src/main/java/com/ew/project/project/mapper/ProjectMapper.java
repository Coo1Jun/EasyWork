package com.ew.project.project.mapper;

import com.ew.project.project.dto.ProjectDto;
import com.ew.project.project.dto.ProjectQueryParam;
import com.ew.project.project.dto.UserMtmProjectDto;
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
}
