package com.ew.project.workitem.service;

import com.ew.project.workitem.dto.*;
import com.ew.project.workitem.entity.WorkItem;
import cn.edu.hzu.common.service.IBaseService;
import cn.edu.hzu.common.api.PageResult;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-07
 */
public interface IWorkItemService extends IBaseService<WorkItem> {

    /**
     * 分页查询，返回Dto
     *
     * @param workItemQueryParam 查询参数
     * @return WorkItemDto 查询返回列表实体
     * @since 2023-04-07
     */
    PageResult<WorkItemDto> pageDto(WorkItemQueryParam workItemQueryParam);

    /**
     * 根据项目id和EpicId，获取工作项基本信息列表
     *
     * @param workItemQueryParam 查询参数
     * @return key：工作项类型。value：工作项进本信息
     * @since 2023-04-07
     */
    Map<String, List<WorkItemDto>> workItemList(WorkItemQueryParam workItemQueryParam);

    /**
     * 根据项目id和EpicId，获取工作项基本信息列表的tree树形数据
     *
     * @param workItemQueryParam 查询参数
     * @return Tree树形数据
     * @since 2023-04-08
     */
    List<WorkItemDto> workItemTreeData(WorkItemQueryParam workItemQueryParam);

    /**
     * 根据项目id和EpicId，获取参与项目工作的用户基本信息
     *
     * @param workItemQueryParam 查询参数
     * @return 参与项目工作的用户基本信息
     * @since 2023-04-08
     */
    List<WorkItemUserDto> workItemUserList(WorkItemQueryParam workItemQueryParam);

    /**
     * 根据项目id和EpicId，获取工作项统计信息
     *
     * @param workItemQueryParam 查询参数
     * @return 工作项统计信息
     * @since 2023-04-08
     */
    WorkItemDataDto workItemStatistics(WorkItemQueryParam workItemQueryParam);

    /**
     * 根据项目id获取当前项目的所有计划集
     *
     * @param workItemQueryParam 查询参数
     * @return WorkItemDto 查询返回列表实体
     * @since 2023-04-07
     */
    List<WorkItemDto> getPlans(WorkItemQueryParam workItemQueryParam);

    /**
     * 新增
     *
     * @param workItemAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-07
     */
    boolean saveByParam(WorkItemAddParam workItemAddParam);

    /**
     * 新增计划集
     *
     * @param workItemAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-07
     */
    boolean savePlans(WorkItemAddParam workItemAddParam);

    /**
     * 根据id查询，转dto
     *
     * @param workItemQueryParam 工作项基本信息id
     * @return WorkItemDto
     * @since 2023-04-07
     */
    WorkItemDto getDtoById(WorkItemQueryParam workItemQueryParam);

    /**
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-07
     */
    boolean saveDtoBatch(List<WorkItemDto> rows);

    /**
     * 更新
     *
     * @param workItemEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-07
     */
    boolean updateByParam(WorkItemEditParam workItemEditParam);

    /**
     * 删除工作项，并删除所有子项
     * @param id
     * @return
     */
    boolean removeWorkItem(String id);
}