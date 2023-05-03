package com.ew.project.workitem.comment.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.project.workitem.comment.entity.WorkItemOtmComment;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-05-03
 */
@Mapper(componentModel = "spring", imports = {BaseEntity.class})
public interface WorkItemOtmCommentParamMapper {

    /**
     * 新增参数转换为实体类
     *
     * @param workItemOtmCommentAddParam 新增参数
     * @return WorkItemOtmComment 实体类
     * @date 2023-05-03
     */
    WorkItemOtmComment addParam2Entity(WorkItemOtmCommentAddParam workItemOtmCommentAddParam);

    /**
     * 修改参数转换为实体类
     *
     * @param workItemOtmCommentEditParam 修改参数
     * @return WorkItemOtmComment 实体类
     * @date 2023-05-03
     */
    WorkItemOtmComment editParam2Entity(WorkItemOtmCommentEditParam workItemOtmCommentEditParam);

    /**
     * 实体类换为Dto
     *
     * @param workItemOtmComment 实体类
     * @return WorkItemOtmCommentDto DTO
     * @date 2023-05-03
     */
    WorkItemOtmCommentDto entity2Dto(WorkItemOtmComment workItemOtmComment);

    /**
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<WorkItemOtmCommentDto> 分页DTO
     * @date 2023-05-03
     */
    PageResult<WorkItemOtmCommentDto> pageEntity2Dto(PageResult<WorkItemOtmComment> page);


    /**
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<WorkItemOtmComment> entity列表
     * @date 2023-05-03
     */
    List<WorkItemOtmComment> dtoList2Entity(List<WorkItemOtmCommentDto> rows);

}
