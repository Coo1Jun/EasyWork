package com.ew.project.workitem.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.project.workitem.entity.WorkItem;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-07
 */
@Mapper(componentModel = "spring", imports = {BaseEntity.class})
public interface WorkItemParamMapper {

    /**
     * 新增参数转换为实体类
     *
     * @param workItemAddParam 新增参数
     * @return WorkItem 实体类
     * @date 2023-04-07
     */
    WorkItem addParam2Entity(WorkItemAddParam workItemAddParam);

    /**
     * 修改参数转换为实体类
     *
     * @param workItemEditParam 修改参数
     * @return WorkItem 实体类
     * @date 2023-04-07
     */
    WorkItem editParam2Entity(WorkItemEditParam workItemEditParam);

    /**
     * 实体类换为Dto
     *
     * @param workItem 实体类
     * @return WorkItemDto DTO
     * @date 2023-04-07
     */
    WorkItemDto entity2Dto(WorkItem workItem);

    /**
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<WorkItemDto> 分页DTO
     * @date 2023-04-07
     */
    PageResult<WorkItemDto> pageEntity2Dto(PageResult<WorkItem> page);


    /**
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<WorkItem> entity列表
     * @date 2023-04-07
     */
    List<WorkItem> dtoList2Entity(List<WorkItemDto> rows);

    List<WorkItemDto> workItemListToWorkItemDtoList(List<WorkItem> rows);

}
