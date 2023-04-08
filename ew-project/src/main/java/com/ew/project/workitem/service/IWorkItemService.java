package com.ew.project.workitem.service;

import com.ew.project.workitem.entity.WorkItem;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.project.workitem.dto.WorkItemQueryParam;
import com.ew.project.workitem.dto.WorkItemAddParam;
import com.ew.project.workitem.dto.WorkItemEditParam;
import com.ew.project.workitem.dto.WorkItemDto;
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
     * @param id 工作项基本信息id
     * @return WorkItemDto
     * @since 2023-04-07
     */
    WorkItemDto getDtoById(String id);

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
}