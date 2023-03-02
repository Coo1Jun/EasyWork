package com.ew.server.file.service.impl;

import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.exception.CommonException;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ew.server.constants.OSSConstant;
import com.ew.server.file.entity.FileMeta;
import com.ew.server.file.mapper.FileMetaMapper;
import com.ew.server.file.oss.OSSUtil;
import com.ew.server.file.service.IFileMetaService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.server.file.dto.FileMetaQueryParam;
import com.ew.server.file.dto.FileMetaAddParam;
import com.ew.server.file.dto.FileMetaEditParam;
import com.ew.server.file.dto.FileMetaParamMapper;
import com.ew.server.file.dto.FileMetaDto;
import cn.edu.hzu.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.ResultCode;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    public boolean updateByParam(FileMetaEditParam fileMetaEditParam) {
        FileMeta fileMeta = fileMetaParamMapper.editParam2Entity(fileMetaEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,fileMeta);
        return updateById(fileMeta);
    }

    @Override
    public FileMetaDto upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        // 得到文件扩展名
        String fileExtension = originalFilename.substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".") + 1);
        // 重新拼接文件的名字
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExtension;
        try {
            // 文件内容上传到OSS
            PutObjectResult result = OSSUtil.upload(OSSConstant.FILE_DIRECTORY + newFileName, file.getInputStream());
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
    public boolean delete(List<String> ids) {
        // 根据ids查询文件名称
        List<String> fileNames = fileMetaMapper.getFileNameByIds(ids);
        // 根据ids删除记录
        this.removeByIds(ids);
        fileNames = fileNames.stream().map(m -> OSSConstant.FILE_DIRECTORY + m).collect(Collectors.toList());
        log.info("要删除OSS的keys =》 {}", fileNames);
        if (StringUtils.isEmpty(fileNames)) return false;
        // 根据文件名称删除OSS
        OSSUtil.delete(fileNames);
        return true;
    }

    @Override
    public FileMetaDto getDtoById(String id) {
        return fileMetaParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<FileMetaDto> rows) {
        return saveBatch(fileMetaParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<FileMeta> getPageSearchWrapper(FileMetaQueryParam fileMetaQueryParam) {
        LambdaQueryWrapper<FileMeta> wrapper = Wrappers.<FileMeta>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(FileMeta.class)) {
            wrapper.orderByDesc(FileMeta::getUpdateTime, FileMeta::getCreateTime);
        }
        return wrapper;
    }
}
