package com.ew.server.file.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.server.file.entity.FileMeta;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-03-01
 *
 */
@Mapper(componentModel = "spring",imports = {BaseEntity.class})
public interface  FileMetaParamMapper{

    /**
     *
     * 新增参数转换为实体类
     *
     * @param fileMetaAddParam 新增参数
     * @return FileMeta 实体类
     * @date 2023-03-01
     *
     */
    FileMeta addParam2Entity(FileMetaAddParam fileMetaAddParam);

    /**
     *
     * 修改参数转换为实体类
     *
     * @param fileMetaEditParam 修改参数
     * @return FileMeta 实体类
     * @date 2023-03-01
     *
     */
    FileMeta editParam2Entity(FileMetaEditParam fileMetaEditParam);

    /**
     *
     * 实体类换为Dto
     *
     * @param fileMeta 实体类
     * @return FileMetaDto DTO
     * @date 2023-03-01
     *
     */
    FileMetaDto entity2Dto(FileMeta fileMeta);

    /**
     *
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<FileMetaDto> 分页DTO
     * @date 2023-03-01
     *
     */
    PageResult<FileMetaDto> pageEntity2Dto(PageResult<FileMeta> page);


    /**
     *
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<FileMeta> entity列表
     * @date 2023-03-01
     *
     */
    List<FileMeta> dtoList2Entity(List<FileMetaDto> rows);

}
