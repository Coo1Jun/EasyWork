package com.ew.server.file.service.impl;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.entity.BaseEntity;
import cn.edu.hzu.common.exception.CommonException;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.server.constants.OSSConstant;
import com.ew.server.file.dto.*;
import com.ew.server.file.entity.FileMeta;
import com.ew.server.file.enums.FileErrorEnum;
import com.ew.server.file.mapper.FileMetaMapper;
import com.ew.server.file.oss.OSSUtil;
import com.ew.server.file.service.IFileMetaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-03-01
 */
@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class, Error.class})
public class FileMetaServiceImpl extends BaseServiceImpl<FileMetaMapper, FileMeta> implements IFileMetaService {

    @Autowired
    private FileMetaParamMapper fileMetaParamMapper;

    @Autowired
    private FileMetaMapper fileMetaMapper;

    @Override
    public PageResult<FileMetaDto> pageDto(FileMetaQueryParam fileMetaQueryParam) {
        Wrapper<FileMeta> wrapper = getPageSearchWrapper(fileMetaQueryParam);
        PageResult<FileMetaDto> result = fileMetaParamMapper.pageEntity2Dto(page(fileMetaQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(FileMetaAddParam fileMetaAddParam) {
        FileMeta fileMeta = fileMetaParamMapper.addParam2Entity(fileMetaAddParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,fileMeta);
        return save(fileMeta);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(FileMetaEditParam editParam) {
        FileMeta fileMeta = fileMetaParamMapper.editParam2Entity(editParam);
        if (StringUtils.isNotEmpty(editParam.getName())) {
            fileMeta.setOriginalFilename(editParam.getName());
            if (StringUtils.isNotEmpty(editParam.getExtendName())) {
                fileMeta.setOriginalFilename(editParam.getName() + "." + editParam.getExtendName());
            }
        }
        return updateById(fileMeta);
    }

    @Override
    public FileMetaDto upload(MultipartFile file, String directory) {
        String originalFilename = file.getOriginalFilename();
        // 得到文件扩展名
        String fileExtension = originalFilename.substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".") + 1);
        // 重新拼接文件的名字
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExtension;
        try {
            // 文件内容上传到OSS
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 指定该文件被下载时的名称。
            objectMetadata.setContentDisposition("attachment;filename=" + URLEncoder.encode(originalFilename , "UTF-8"));
            PutObjectResult result = OSSUtil.upload(directory + newFileName, file.getInputStream(), objectMetadata);
            ResponseMessage response = result.getResponse();
            FileMeta fileMeta = new FileMeta();
            fileMeta.setFileName(newFileName);
            fileMeta.setFileSize(file.getSize());
            fileMeta.setLocation(response.getUri());
            fileMeta.setOriginalFilename(originalFilename);
            // 文件元信息保存到DB
            this.save(fileMeta);
            return fileMetaParamMapper.entity2Dto(fileMeta);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(List<String> ids, String directory) {
        // 根据ids查询文件名称
        List<String> fileNames = fileMetaMapper.getFileNameByIds(ids);
        // 根据ids删除记录
        this.removeByIds(ids);
        fileNames = fileNames.stream().map(m -> directory + m).collect(Collectors.toList());
        log.info("要删除OSS的keys =》 {}", fileNames);
        if (StringUtils.isEmpty(fileNames)) return false;
        // 根据文件名称删除OSS
        OSSUtil.delete(fileNames);
        return true;
    }

    @Override
    public void download(String id, HttpServletResponse response, String directory) {
        // 查询文件元信息
        FileMeta fileMeta = this.getById(id);
        if (fileMeta == null) {
            throw new CommonException(FileErrorEnum.FILE_NOT_FOUND.setParams(new Object[]{""}));
        }
        // 创建ossClient
        OSS ossClient = OSSUtil.getOSSClient();
        // 判断文件是否存在
        boolean found = ossClient.doesObjectExist(OSSConstant.BUCKET_NAME, directory + fileMeta.getFileName());
        if (!found) {
            throw new CommonException(FileErrorEnum.FILE_NOT_FOUND.setParams(new Object[]{directory + fileMeta.getFileName()}));
        }
        // 到OSS查询文件对象
        OSSObject ossObject = ossClient.getObject(OSSConstant.BUCKET_NAME, directory + fileMeta.getFileName());
        // 设置content-type
        response.setContentType(ossObject.getObjectMetadata().getContentType());
        try {
            // 设置头部
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileMeta.getOriginalFilename(), "UTF-8"));
            IOUtils.copy(ossObject.getObjectContent(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ossObject.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ossClient.shutdown();
        }
    }

    @Override
    public List<FileMetaDto> getFileList(List<String> ids) {
        List<FileMeta> list = this.list(Wrappers.<FileMeta>lambdaQuery().in(FileMeta::getId, ids));
        return fileMetaParamMapper.fileMetaListToFileMetaDtoList(list);
    }

    @Override
    public FileMetaDto getDtoById(String id) {
        if (StringUtils.isEmpty(id)) return null;
        return fileMetaParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<FileMetaDto> rows) {
        return saveBatch(fileMetaParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<FileMeta> getPageSearchWrapper(FileMetaQueryParam queryParam) {
        LambdaQueryWrapper<FileMeta> wrapper = Wrappers.<FileMeta>lambdaQuery();
        wrapper.eq(queryParam.getOriginalFilename() != null,
                FileMeta::getOriginalFilename,
                queryParam.getOriginalFilename());
        // 根据创建时间升序
        wrapper.orderByAsc(FileMeta::getCreateTime);
        return wrapper;
    }
}
