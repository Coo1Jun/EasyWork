package com.ew.project.workitem.service.impl;

import com.ew.project.workitem.entity.WorkItemOtmFile;
import com.ew.project.workitem.mapper.WorkItemOtmFileMapper;
import com.ew.project.workitem.service.IWorkItemOtmFileService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.project.workitem.dto.WorkItemOtmFileQueryParam;
import com.ew.project.workitem.dto.WorkItemOtmFileAddParam;
import com.ew.project.workitem.dto.WorkItemOtmFileEditParam;
import com.ew.project.workitem.dto.WorkItemOtmFileParamMapper;
import com.ew.project.workitem.dto.WorkItemOtmFileDto;
import cn.edu.hzu.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.edu.hzu.common.api.PageResult;

import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;



/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-07
 *
 */
@Slf4j
@Service
@Transactional(readOnly = true,rollbackFor={Exception.class, Error.class})
public class WorkItemOtmFileServiceImpl extends BaseServiceImpl<WorkItemOtmFileMapper, WorkItemOtmFile> implements IWorkItemOtmFileService {

    @Autowired
    private WorkItemOtmFileParamMapper workItemOtmFileParamMapper;

    @Override
    public PageResult<WorkItemOtmFileDto> pageDto(WorkItemOtmFileQueryParam workItemOtmFileQueryParam) {
        Wrapper<WorkItemOtmFile> wrapper = getPageSearchWrapper(workItemOtmFileQueryParam);
        PageResult<WorkItemOtmFileDto> result = workItemOtmFileParamMapper.pageEntity2Dto(page(workItemOtmFileQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor={Exception.class, Error.class})
    public boolean saveByParam(WorkItemOtmFileAddParam workItemOtmFileAddParam) {
        WorkItemOtmFile workItemOtmFile = workItemOtmFileParamMapper.addParam2Entity(workItemOtmFileAddParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,workItemOtmFile);
        return save(workItemOtmFile);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor={Exception.class, Error.class})
    public boolean updateByParam(WorkItemOtmFileEditParam workItemOtmFileEditParam) {
        WorkItemOtmFile workItemOtmFile = workItemOtmFileParamMapper.editParam2Entity(workItemOtmFileEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,workItemOtmFile);
        return updateById(workItemOtmFile);
    }

    @Override
    public WorkItemOtmFileDto getDtoById(String id) {
        return workItemOtmFileParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor={Exception.class, Error.class})
    public boolean saveDtoBatch(List<WorkItemOtmFileDto> rows) {
        return saveBatch(workItemOtmFileParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<WorkItemOtmFile> getPageSearchWrapper(WorkItemOtmFileQueryParam workItemOtmFileQueryParam) {
        LambdaQueryWrapper<WorkItemOtmFile> wrapper = Wrappers.<WorkItemOtmFile>lambdaQuery();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            if(BaseEntity.class.isAssignableFrom(WorkItemOtmFile.class)){
            wrapper.orderByDesc(WorkItemOtmFile::getUpdateTime,WorkItemOtmFile::getCreateTime);
        }
        return wrapper;
    }
}
