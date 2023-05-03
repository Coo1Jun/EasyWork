package com.ew.project.workitem.comment.service;

import com.ew.project.workitem.comment.entity.WorkItemOtmComment;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentQueryParam;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentAddParam;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentEditParam;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentDto;
import cn.edu.hzu.common.api.PageResult;

import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-05-03
 */
public interface IWorkItemOtmCommentService extends IBaseService<WorkItemOtmComment> {

    /**
     * 分页查询，返回Dto
     *
     * @param workItemOtmCommentQueryParam 查询参数
     * @return WorkItemOtmCommentDto 查询返回列表实体
     * @since 2023-05-03
     */
    PageResult<WorkItemOtmCommentDto> pageDto(WorkItemOtmCommentQueryParam workItemOtmCommentQueryParam);

    /**
     * 新增
     *
     * @param workItemOtmCommentAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-05-03
     */
    boolean saveByParam(WorkItemOtmCommentAddParam workItemOtmCommentAddParam);

    /**
     * 根据id查询，转dto
     *
     * @param id 工作项和评论实体信息id
     * @return WorkItemOtmCommentDto
     * @since 2023-05-03
     */
    WorkItemOtmCommentDto getDtoById(String id);

    /**
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-05-03
     */
    boolean saveDtoBatch(List<WorkItemOtmCommentDto> rows);

    /**
     * 更新
     *
     * @param workItemOtmCommentEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-05-03
     */
    boolean updateByParam(WorkItemOtmCommentEditParam workItemOtmCommentEditParam);
}