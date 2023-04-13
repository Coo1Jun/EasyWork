package com.ew.project.netdisk.service.impl;

import cn.edu.hzu.client.dto.FileMetaDto;
import cn.edu.hzu.client.server.service.IServerClientService;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.api.utils.UserUtils;
import cn.edu.hzu.common.entity.SsoUser;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ew.project.netdisk.constant.NetDiskConstant;
import com.ew.project.netdisk.entity.NetDiskFile;
import com.ew.project.netdisk.enums.NetDiskErrorEnum;
import com.ew.project.netdisk.enums.NetDiskTypeEnum;
import com.ew.project.netdisk.mapper.NetDiskFileMapper;
import com.ew.project.netdisk.service.INetDiskFileService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.project.netdisk.dto.NetDiskFileQueryParam;
import com.ew.project.netdisk.dto.NetDiskFileAddParam;
import com.ew.project.netdisk.dto.NetDiskFileEditParam;
import com.ew.project.netdisk.dto.NetDiskFileParamMapper;
import com.ew.project.netdisk.dto.NetDiskFileDto;
import cn.edu.hzu.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.edu.hzu.common.api.PageResult;

import java.util.*;

import org.springframework.transaction.annotation.Transactional;


/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class, Error.class})
public class NetDiskFileServiceImpl extends BaseServiceImpl<NetDiskFileMapper, NetDiskFile> implements INetDiskFileService {

    @Autowired
    private NetDiskFileParamMapper netDiskFileParamMapper;
    @Autowired
    private IServerClientService serverClientService;

    @Override
    public PageResult<NetDiskFileDto> pageDto(NetDiskFileQueryParam netDiskFileQueryParam) {
        String userid = UserUtils.getCurrentUser().getUserid();
        // 查出项目类型的文件总数
        Integer proTotal = this.baseMapper.getProNetFileCount(userid, netDiskFileQueryParam);
        // 查出个人类型的文件总数
        Integer perTotal = this.baseMapper.getPerNetFileCount(userid, netDiskFileQueryParam);
        // 计算需要舍弃项目类型文件的前几条记录
        int proOffset = (netDiskFileQueryParam.getPageNo() - 1) * netDiskFileQueryParam.getLimit();
        PageResult<NetDiskFileDto> proNetDisk = null;
        PageResult<NetDiskFileDto> perNetDisk = null;
        if (proTotal <= proOffset) {
            netDiskFileQueryParam.setOffset(proOffset - proTotal);
            // 查出个人类型的文件
            perNetDisk = this.getPerNetDisk(netDiskFileQueryParam);
        } else {
            netDiskFileQueryParam.setOffset(proOffset);
            // 查询项目类型的文件
            proNetDisk = this.getProNetDisk(netDiskFileQueryParam);
            // 如果项目类型的文件不够，则需要查询个人类型的
            if (proNetDisk.getRecords().size() < netDiskFileQueryParam.getLimit()) {
                // 计算需要舍弃个人类型文件的前几条记录
                netDiskFileQueryParam.setOffset(0);
                netDiskFileQueryParam.setPageNo(1);
                netDiskFileQueryParam.setLimit(netDiskFileQueryParam.getLimit() - proNetDisk.getRecords().size());
                // 查出个人类型的文件
                perNetDisk = this.getPerNetDisk(netDiskFileQueryParam);
            }

        }
        if (proNetDisk == null) {
            if (perNetDisk != null) {
                perNetDisk.setTotal(proTotal + perTotal);
            }
            return Optional.ofNullable(perNetDisk).orElse(new PageResult<>(new ArrayList<>(), 0));
        } else {
            // 两种类型的文件合并
            List<NetDiskFileDto> result = new ArrayList<>(proNetDisk.getRecords());
            if (perNetDisk != null) {
                result.addAll(perNetDisk.getRecords());
            }
            proNetDisk.setRecords(result);
            proNetDisk.setTotal(proTotal + perTotal);
            return proNetDisk;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(NetDiskFileAddParam netDiskFileAddParam) {
        NetDiskFile netDiskFile = netDiskFileParamMapper.addParam2Entity(netDiskFileAddParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,netDiskFile);
        return save(netDiskFile);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(NetDiskFileEditParam netDiskFileEditParam) {
        NetDiskFile netDiskFile = netDiskFileParamMapper.editParam2Entity(netDiskFileEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,netDiskFile);
        return updateById(netDiskFile);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean addDir(NetDiskFileAddParam addParam, boolean allowSameName) {
        return addNetDiskFile(addParam, true, allowSameName);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean uploadFile(NetDiskFileAddParam addParam, boolean allowSameName) {
        return addNetDiskFile(addParam, false, allowSameName);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean renameFile(NetDiskFileEditParam editParam) {
        // 判断文件名
        if (StringUtils.isEmpty(editParam.getFileName())) {
            throw CommonException.builder().resultCode(CommonErrorEnum.PARAM_IS_EMPTY.setParams(new Object[]{"名称"})).build();
        }
        // 判断是否重名
        NetDiskFile file = this.getById(editParam.getId());
        int count = this.count(Wrappers.<NetDiskFile>lambdaQuery()
                .ne(NetDiskFile::getId, file.getId()) // 排除自身
                .eq(NetDiskFile::getFileName, editParam.getFileName()) // 文件名
                .eq(NetDiskFile::getBelongType, file.getBelongType()) // 所属类型
                .eq(NetDiskFile::getBelongId, file.getBelongId()) // 所属id
                .eq(NetDiskFile::getIsDir, file.getIsDir()) // 是否为文件夹
                .eq(NetDiskFile::getFilePath, file.getFilePath()) // 文件路径要相同
                .eq(NetDiskFile::getDeleted, 0));// 没有被删除);
        if (count > 0) {
            throw CommonException.builder().resultCode(CommonErrorEnum.PARAM_IS_EXIST.setParams(new Object[]{editParam.getFileName()})).build();
        }
        file.setFileName(editParam.getFileName());
        return updateById(file);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean moveFile(NetDiskFileEditParam editParam) {
        if (StringUtils.isEmpty(editParam.getFilePath())) {
            throw CommonException.builder().resultCode(CommonErrorEnum.PARAM_IS_EMPTY.setParams(new Object[]{"目标路径"})).build();
        }
        NetDiskFile file = this.getById(editParam.getId());
        // 删除原有的
        this.removeById(file.getId());
        // 在对应的路径添加
        NetDiskFileAddParam addParam = new NetDiskFileAddParam();
        addParam.setFileName(file.getFileName());
        addParam.setBelongType(file.getBelongType());
        addParam.setBelongId(file.getBelongId());
        addParam.setFilePath(editParam.getFilePath());
        addParam.setFileId(file.getFileId());
        return this.addNetDiskFile(addParam, file.getIsDir() == 1, true);
    }

    @Override
    public PageResult<NetDiskFileDto> getProNetDisk(NetDiskFileQueryParam queryParam) {
        if (queryParam.getOffset() == null || queryParam.getOffset() == 0) {
            queryParam.setOffset((queryParam.getPageNo() - 1) * queryParam.getLimit());
        }
        SsoUser curUser = UserUtils.getCurrentUser();
        List<NetDiskFileDto> fileList = this.baseMapper.getProNetFile(curUser.getUserid(), queryParam);
        if (CollectionUtils.isEmpty(fileList)) return null;
        // 查询文件对应的信息
        for (NetDiskFileDto dto : fileList) {
            if (StringUtils.isNotEmpty(dto.getFileId())) {
                FileMetaDto fileMeta = serverClientService.getFileById(dto.getFileId());
                dto.setFileUrl(fileMeta.getUrl());
                dto.setFileSize(fileMeta.getFileSize());
            }
            if (dto.getFileNameNum() != null) {
                dto.setFileName(dto.getFileName() + "(" + dto.getFileNameNum() + ")");
            }
        }
        Integer total = this.baseMapper.getProNetFileCount(curUser.getUserid(), queryParam);
        return PageResult.<NetDiskFileDto>builder().records(fileList).total(total).build();
    }

    @Override
    public PageResult<NetDiskFileDto> getPerNetDisk(NetDiskFileQueryParam queryParam) {
        if (queryParam.getOffset() == null || queryParam.getOffset() == 0) {
            queryParam.setOffset((queryParam.getPageNo() - 1) * queryParam.getLimit());
        }
        SsoUser curUser = UserUtils.getCurrentUser();
        List<NetDiskFileDto> fileList = this.baseMapper.getPerNetFile(curUser.getUserid(), queryParam);
        if (CollectionUtils.isEmpty(fileList)) return null;
        // 查询文件对应的信息
        for (NetDiskFileDto dto : fileList) {
            if (StringUtils.isNotEmpty(dto.getFileId())) {
                FileMetaDto fileMeta = serverClientService.getFileById(dto.getFileId());
                dto.setFileUrl(fileMeta.getUrl());
                dto.setFileSize(fileMeta.getFileSize());
            }
            if (dto.getFileNameNum() != null) {
                dto.setFileName(dto.getFileName() + "(" + dto.getFileNameNum() + ")");
            }
        }
        Integer total = this.baseMapper.getPerNetFileCount(curUser.getUserid(), queryParam);
        return PageResult.<NetDiskFileDto>builder().records(fileList).total(total).build();
    }

    @Override
    public NetDiskFileDto getDtoById(String id) {
        NetDiskFileDto dto = netDiskFileParamMapper.entity2Dto(this.getById(id));
        if (StringUtils.isNotEmpty(dto.getFileId())) {
            FileMetaDto fileMeta = serverClientService.getFileById(dto.getFileId());
            dto.setFileUrl(fileMeta.getUrl());
            dto.setFileSize(fileMeta.getFileSize());
        }
        if (dto.getFileNameNum() != null) {
            dto.setFileName(dto.getFileName() + "(" + dto.getFileNameNum() + ")");
        }
        return dto;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<NetDiskFileDto> rows) {
        return saveBatch(netDiskFileParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<NetDiskFile> getPageSearchWrapper(NetDiskFileQueryParam netDiskFileQueryParam) {
        LambdaQueryWrapper<NetDiskFile> wrapper = Wrappers.<NetDiskFile>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(NetDiskFile.class)) {
            wrapper.orderByDesc(NetDiskFile::getUpdateTime, NetDiskFile::getCreateTime);
        }
        return wrapper;
    }

    /**
     * 检查所属类型是否正确
     * @param type
     * @return true：错误的类型。false：不是错误的类型
     */
    private boolean isErrorBelongType(Integer type) {
        if (type == null) return true;
        NetDiskTypeEnum[] values = NetDiskTypeEnum.values();
        for (NetDiskTypeEnum typeEnum : values) {
            if (type == typeEnum.getCode()) return false;
        }
        return true;
    }

    /**
     * 保存网盘文件
     * @param netDiskFile
     * @return
     */
    private boolean saveNetDiskFile(NetDiskFile netDiskFile, boolean allowSameName) {
        // 判断是否重名
        List<NetDiskFile> list = this.list(Wrappers.<NetDiskFile>lambdaQuery()
                .eq(NetDiskFile::getBelongType, netDiskFile.getBelongType()) // 所属类型
                .eq(NetDiskFile::getBelongId, netDiskFile.getBelongId()) // 所属id
                .eq(NetDiskFile::getIsDir, netDiskFile.getIsDir()) // 是否为文件夹
                .eq(NetDiskFile::getFilePath, netDiskFile.getFilePath()) // 文件路径要相同
                .eq(NetDiskFile::getFileName, netDiskFile.getFileName()) // 文件名相同
                .eq(NetDiskFile::getDeleted, 0));// 没有被删除
        if (CollectionUtils.isNotEmpty(list)) {
            // 不允许重名，抛出错误
            if (!allowSameName) {
                throw CommonException
                        .builder()
                        .resultCode(CommonErrorEnum.PARAM_IS_EXIST.setParams(new Object[]{netDiskFile.getFileName()}))
                        .build();
            }
            int num = 1;
            Set<Integer> numSet = new HashSet<>();
            for (NetDiskFile file : list) {
                if (file.getFileNameNum() != null) {
                    numSet.add(file.getFileNameNum()); // 保存所有的文件名字编号
                }
            }
            while (numSet.contains(num)) num++;
            netDiskFile.setFileNameNum(num);
        }
        netDiskFile.setDeleted(0);
        return save(netDiskFile);
    }

    /**
     * 添加网盘文件
     * @param addParam
     * @return
     */
    private boolean addNetDiskFile(NetDiskFileAddParam addParam, boolean isDir, boolean allowSameName) {
        // 判断所属类型
        if (isErrorBelongType(addParam.getBelongType())) {
            // 错误的所属类型，默认将其设置为个人
            addParam.setBelongType(NetDiskTypeEnum.PERSONAL.getCode());
            addParam.setBelongId(UserUtils.getCurrentUser().getUserid());
        }
        // 判断文件路径
        if (StringUtils.isEmpty(addParam.getFilePath())) {
            // 默认为主目录路径
            addParam.setFilePath(NetDiskConstant.MAIN_DIR_PATH);
        } else if (addParam.getFilePath().indexOf(NetDiskConstant.MAIN_DIR_PATH) != 0) {
            // 开头不是"/"
            addParam.setFilePath(NetDiskConstant.MAIN_DIR_PATH + addParam.getFilePath());
        }
        // 判断文件名
        if (StringUtils.isEmpty(addParam.getFileName())) {
            throw CommonException.builder().resultCode(CommonErrorEnum.PARAM_IS_EMPTY.setParams(new Object[]{"文件夹名称"})).build();
        }
        NetDiskFile netDiskFile = netDiskFileParamMapper.addParam2Entity(addParam);
        // 如果添加的是文件
        if (!isDir) {
            if (StringUtils.isEmpty(netDiskFile.getFileId())) {
                throw CommonException.builder().resultCode(NetDiskErrorEnum.FILE_NOT_EXIST).build();
            }
            FileMetaDto fileMeta = serverClientService.getFileById(netDiskFile.getFileId());
            if (fileMeta == null) {
                throw CommonException.builder().resultCode(NetDiskErrorEnum.FILE_NOT_EXIST).build();
            }
            String fileName = fileMeta.getName();
            // 赋值文件扩展名
            int idx = fileName.lastIndexOf('.');
            if (idx == -1) {
                netDiskFile.setExtendName("");
                netDiskFile.setFileName(fileName); // 赋值文件名称
            } else {
                netDiskFile.setExtendName(fileName.substring(idx + 1));
                netDiskFile.setFileName(fileName.substring(0, idx)); // 赋值文件名称
            }
        }
        // 设置类型是否为文件夹
        netDiskFile.setIsDir(isDir ? 1 : 0);
        return saveNetDiskFile(netDiskFile, allowSameName);
    }
}