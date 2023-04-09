package com.ew.project.workitem.mapper;

import com.ew.project.workitem.entity.WorkItemOtmFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-07
 */
public interface WorkItemOtmFileMapper extends BaseMapper<WorkItemOtmFile> {

    List<String> getFileIdByWorkItemId(@Param("workItemId") String workItemId);
}
