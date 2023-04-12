package com.ew.project.netdisk.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.project.netdisk.entity.NetDiskFile;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-12
 */
@Mapper(componentModel = "spring", imports = {BaseEntity.class})
public interface NetDiskFileParamMapper {

    /**
     * 新增参数转换为实体类
     *
     * @param netDiskFileAddParam 新增参数
     * @return NetDiskFile 实体类
     * @date 2023-04-12
     */
    NetDiskFile addParam2Entity(NetDiskFileAddParam netDiskFileAddParam);

    /**
     * 修改参数转换为实体类
     *
     * @param netDiskFileEditParam 修改参数
     * @return NetDiskFile 实体类
     * @date 2023-04-12
     */
    NetDiskFile editParam2Entity(NetDiskFileEditParam netDiskFileEditParam);

    /**
     * 实体类换为Dto
     *
     * @param netDiskFile 实体类
     * @return NetDiskFileDto DTO
     * @date 2023-04-12
     */
    NetDiskFileDto entity2Dto(NetDiskFile netDiskFile);

    /**
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<NetDiskFileDto> 分页DTO
     * @date 2023-04-12
     */
    PageResult<NetDiskFileDto> pageEntity2Dto(PageResult<NetDiskFile> page);


    /**
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<NetDiskFile> entity列表
     * @date 2023-04-12
     */
    List<NetDiskFile> dtoList2Entity(List<NetDiskFileDto> rows);

}
