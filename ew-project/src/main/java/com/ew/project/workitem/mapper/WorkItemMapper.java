package com.ew.project.workitem.mapper;

import com.ew.project.workitem.entity.WorkItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-07
 */
public interface WorkItemMapper extends BaseMapper<WorkItem> {

    /**
     * 根据项目id获取当前项目的编号最高值
     * @param projectId
     * @return
     */
    Integer getMaxNumber(@Param("project_id") String projectId);

}
