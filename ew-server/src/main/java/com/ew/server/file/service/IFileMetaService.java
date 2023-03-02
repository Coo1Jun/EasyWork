package com.ew.server.file.service;

import cn.edu.hzu.common.api.RestResponse;
import com.ew.server.file.entity.FileMeta;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.server.file.dto.FileMetaQueryParam;
import com.ew.server.file.dto.FileMetaAddParam;
import com.ew.server.file.dto.FileMetaEditParam;
import com.ew.server.file.dto.FileMetaDto;
import cn.edu.hzu.common.api.PageResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-03-01
 *
 */
public interface IFileMetaService extends IBaseService<FileMeta> {

    /**
     *
     * 分页查询，返回Dto
     *
     * @param fileMetaQueryParam 查询参数
     * @return FileMetaDto 查询返回列表实体
     * @since 2023-03-01
     *
     */
    PageResult<FileMetaDto> pageDto(FileMetaQueryParam fileMetaQueryParam);

    /**
     *
     * 新增
     *
     * @param fileMetaAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-03-01
     *
     */
    boolean saveByParam(FileMetaAddParam fileMetaAddParam);

    /**
     *
     * 根据id查询，转dto
     *
     * @param id 文件元信息id
     * @return FileMetaDto
     * @since 2023-03-01
     *
     */
    FileMetaDto getDtoById(String id);

    /**
     *
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-03-01
     *
     */
    boolean saveDtoBatch(List<FileMetaDto> rows);

    /**
     *
     * 更新
     *
     * @param fileMetaEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-03-01
     *
     */
    boolean updateByParam(FileMetaEditParam fileMetaEditParam);

    /**
     * 上传文件内容到OSS和保存文件元信息到DB
     * @param file
     */
    FileMetaDto upload(MultipartFile file, String directory);

    /**
     * 批量删除文件
     * @param ids
     */
    boolean delete(List<String> ids, String directory);

    /**
     * 下载文件
     * @param id 文件id
     * @param response
     */
    void download(String id, HttpServletResponse response, String directory);
}