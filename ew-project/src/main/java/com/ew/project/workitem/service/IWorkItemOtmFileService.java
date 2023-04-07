package com.ew.project.workitem.service;

import com.ew.project.workitem.entity.WorkItemOtmFile;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.project.workitem.dto.WorkItemOtmFileQueryParam;
import com.ew.project.workitem.dto.WorkItemOtmFileAddParam;
import com.ew.project.workitem.dto.WorkItemOtmFileEditParam;
import com.ew.project.workitem.dto.WorkItemOtmFileDto;
import cn.edu.hzu.common.api.PageResult;
import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-07
 *
 */
public interface IWorkItemOtmFileService extends IBaseService<WorkItemOtmFile> {

    /**
     *
     * 分页查询，返回Dto
     *
     * @param workItemOtmFileQueryParam 查询参数
     * @return WorkItemOtmFileDto 查询返回列表实体
     * @since 2023-04-07
     *
     */
    PageResult<WorkItemOtmFileDto> pageDto(WorkItemOtmFileQueryParam workItemOtmFileQueryParam);

    /**
     *
     * 新增
     *
     * @param workItemOtmFileAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-07
     *
     */
    boolean saveByParam(WorkItemOtmFileAddParam workItemOtmFileAddParam);

    /**
     *
     * 根据id查询，转dto
     *
     * @param id 工作项-文件对照信息id
     * @return WorkItemOtmFileDto
     * @since 2023-04-07
     *
     */
    WorkItemOtmFileDto getDtoById(String id);

    /**
     *
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-07
     *
     */
    boolean saveDtoBatch(List<WorkItemOtmFileDto> rows);

    /**
     *
     * 更新
     *
     * @param workItemOtmFileEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-07
     *
     */
    boolean updateByParam(WorkItemOtmFileEditParam workItemOtmFileEditParam);
}