package com.ew.project.netdisk.service;

import com.ew.project.netdisk.dto.*;
import com.ew.project.netdisk.entity.NetDiskFile;
import cn.edu.hzu.common.service.IBaseService;
import cn.edu.hzu.common.api.PageResult;

import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-12
 */
public interface INetDiskFileService extends IBaseService<NetDiskFile> {

    /**
     * 分页查询，返回Dto
     *
     * @param netDiskFileQueryParam 查询参数
     * @return NetDiskFileDto 查询返回列表实体
     * @since 2023-04-12
     */
    PageResult<NetDiskFileDto> pageDto(NetDiskFileQueryParam netDiskFileQueryParam);

    /**
     * 新增
     *
     * @param netDiskFileAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-12
     */
    boolean saveByParam(NetDiskFileAddParam netDiskFileAddParam);

    /**
     * 根据id查询，转dto
     *
     * @param id 网盘文件信息id
     * @return NetDiskFileDto
     * @since 2023-04-12
     */
    NetDiskFileDto getDtoById(String id);

    /**
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-12
     */
    boolean saveDtoBatch(List<NetDiskFileDto> rows);

    /**
     * 更新
     *
     * @param netDiskFileEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-12
     */
    boolean updateByParam(NetDiskFileEditParam netDiskFileEditParam);

    /**
     * 新增文件夹
     *
     * @param netDiskFileAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-12
     */
    boolean addDir(NetDiskFileAddParam netDiskFileAddParam, boolean allowSameName);

    /**
     * 上传文件
     *
     * @param netDiskFileAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-12
     */
    boolean uploadFile(NetDiskFileAddParam netDiskFileAddParam, boolean allowSameName);

    /**
     * 文件重命名
     *
     * @param netDiskFileEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-12
     */
    boolean renameFile(NetDiskFileEditParam netDiskFileEditParam);

    /**
     * 文件移动
     *
     * @param netDiskFileEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-12
     */
    boolean moveFile(NetDiskFileEditParam netDiskFileEditParam);

    /**
     * 文件复制
     *
     * @param netDiskFileEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-12
     */
    boolean copyFile(NetDiskFileEditParam netDiskFileEditParam);

    /**
     * 分页查询，获取项目类型的文件夹
     *
     * @param netDiskFileQueryParam 查询参数
     * @return NetDiskFileDto 查询返回列表实体
     * @since 2023-04-12
     */
    PageResult<NetDiskFileDto> getProNetDisk(NetDiskFileQueryParam netDiskFileQueryParam);

    /**
     * 分页查询，获取个人文件夹
     *
     * @param netDiskFileQueryParam 查询参数
     * @return NetDiskFileDto 查询返回列表实体
     * @since 2023-04-12
     */
    PageResult<NetDiskFileDto> getPerNetDisk(NetDiskFileQueryParam netDiskFileQueryParam);

    /**
     * 获取文件夹树结构
     */
    List<DirTreeNode> getDirTree(NetDiskFileQueryParam netDiskFileQueryParam);
}