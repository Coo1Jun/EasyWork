package com.ew.project.workitem.service.impl;

import com.ew.project.workitem.entity.WorkItem;
import com.ew.project.workitem.mapper.WorkItemMapper;
import com.ew.project.workitem.service.IWorkItemService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.project.workitem.dto.WorkItemQueryParam;
import com.ew.project.workitem.dto.WorkItemAddParam;
import com.ew.project.workitem.dto.WorkItemEditParam;
import com.ew.project.workitem.dto.WorkItemParamMapper;
import com.ew.project.workitem.dto.WorkItemDto;
import cn.edu.hzu.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.ResultCode;

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
 */
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = {Exception.class, Error.class})
public class WorkItemServiceImpl extends BaseServiceImpl<WorkItemMapper, WorkItem> implements IWorkItemService {

    @Autowired
    private WorkItemParamMapper workItemParamMapper;

    @Override
    public PageResult<WorkItemDto> pageDto(WorkItemQueryParam workItemQueryParam) {
        Wrapper<WorkItem> wrapper = getPageSearchWrapper(workItemQueryParam);
        PageResult<WorkItemDto> result = workItemParamMapper.pageEntity2Dto(page(workItemQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(WorkItemAddParam workItemAddParam) {
        WorkItem workItem = workItemParamMapper.addParam2Entity(workItemAddParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,workItem);
        return save(workItem);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(WorkItemEditParam workItemEditParam) {
        WorkItem workItem = workItemParamMapper.editParam2Entity(workItemEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,workItem);
        return updateById(workItem);
    }

    @Override
    public WorkItemDto getDtoById(String id) {
        return workItemParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<WorkItemDto> rows) {
        return saveBatch(workItemParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<WorkItem> getPageSearchWrapper(WorkItemQueryParam workItemQueryParam) {
        LambdaQueryWrapper<WorkItem> wrapper = Wrappers.<WorkItem>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(WorkItem.class)) {
            wrapper.orderByDesc(WorkItem::getUpdateTime, WorkItem::getCreateTime);
        }
        return wrapper;
    }
}
