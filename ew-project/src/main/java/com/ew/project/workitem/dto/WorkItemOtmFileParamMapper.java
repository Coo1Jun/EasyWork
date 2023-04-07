package com.ew.project.workitem.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.project.workitem.entity.WorkItemOtmFile;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-07
 *
 */
@Mapper(componentModel = "spring",imports = {BaseEntity.class})
public interface  WorkItemOtmFileParamMapper{

    /**
     *
     * 新增参数转换为实体类
     *
     * @param workItemOtmFileAddParam 新增参数
     * @return WorkItemOtmFile 实体类
     * @date 2023-04-07
     *
     */
    WorkItemOtmFile addParam2Entity(WorkItemOtmFileAddParam workItemOtmFileAddParam);

    /**
     *
     * 修改参数转换为实体类
     *
     * @param workItemOtmFileEditParam 修改参数
     * @return WorkItemOtmFile 实体类
     * @date 2023-04-07
     *
     */
    WorkItemOtmFile editParam2Entity(WorkItemOtmFileEditParam workItemOtmFileEditParam);

    /**
     *
     * 实体类换为Dto
     *
     * @param workItemOtmFile 实体类
     * @return WorkItemOtmFileDto DTO
     * @date 2023-04-07
     *
     */
    WorkItemOtmFileDto entity2Dto(WorkItemOtmFile workItemOtmFile);

    /**
     *
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<WorkItemOtmFileDto> 分页DTO
     * @date 2023-04-07
     *
     */
    PageResult<WorkItemOtmFileDto> pageEntity2Dto(PageResult<WorkItemOtmFile> page);


    /**
     *
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<WorkItemOtmFile> entity列表
     * @date 2023-04-07
     *
     */
    List<WorkItemOtmFile> dtoList2Entity(List<WorkItemOtmFileDto> rows);

}
