package com.ew.project.workitem.comment.mapper;

import com.ew.project.workitem.comment.dto.WorkItemOtmCommentDto;
import com.ew.project.workitem.comment.entity.WorkItemOtmComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-05-03
 */
public interface WorkItemOtmCommentMapper extends BaseMapper<WorkItemOtmComment> {
    List<WorkItemOtmCommentDto> getListByWorkItemId(@Param("work_item_id") String workItemId);
}
