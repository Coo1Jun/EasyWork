package com.ew.project.workitem.comment.service.impl;

import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.ew.project.workitem.comment.entity.WorkItemOtmComment;
import com.ew.project.workitem.comment.mapper.WorkItemOtmCommentMapper;
import com.ew.project.workitem.comment.service.IWorkItemOtmCommentService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentQueryParam;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentAddParam;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentEditParam;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentParamMapper;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentDto;
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
 * @since 2023-05-03
 */
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = {Exception.class, Error.class})
public class WorkItemOtmCommentServiceImpl extends BaseServiceImpl<WorkItemOtmCommentMapper, WorkItemOtmComment> implements IWorkItemOtmCommentService {

    @Autowired
    private WorkItemOtmCommentParamMapper workItemOtmCommentParamMapper;

    @Override
    public PageResult<WorkItemOtmCommentDto> pageDto(WorkItemOtmCommentQueryParam workItemOtmCommentQueryParam) {
        Wrapper<WorkItemOtmComment> wrapper = getPageSearchWrapper(workItemOtmCommentQueryParam);
        PageResult<WorkItemOtmCommentDto> result = workItemOtmCommentParamMapper.pageEntity2Dto(page(workItemOtmCommentQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(WorkItemOtmCommentAddParam addParam) {
        if (StringUtils.isEmpty(addParam.getWorkItemId())) {
            throw CommonException.builder().resultCode(CommonErrorEnum.PARAM_IS_EMPTY.setParams(new Object[]{"所属工作项"})).build();
        }
        if (StringUtils.isEmpty(addParam.getContent().trim())) {
            throw CommonException.builder().resultCode(CommonErrorEnum.PARAM_IS_EMPTY.setParams(new Object[]{"评论内容"})).build();
        }
        WorkItemOtmComment workItemOtmComment = workItemOtmCommentParamMapper.addParam2Entity(addParam);
        return save(workItemOtmComment);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(WorkItemOtmCommentEditParam workItemOtmCommentEditParam) {
        WorkItemOtmComment workItemOtmComment = workItemOtmCommentParamMapper.editParam2Entity(workItemOtmCommentEditParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,workItemOtmComment);
        return updateById(workItemOtmComment);
    }

    @Override
    public WorkItemOtmCommentDto getDtoById(String id) {
        return workItemOtmCommentParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<WorkItemOtmCommentDto> rows) {
        return saveBatch(workItemOtmCommentParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<WorkItemOtmComment> getPageSearchWrapper(WorkItemOtmCommentQueryParam workItemOtmCommentQueryParam) {
        LambdaQueryWrapper<WorkItemOtmComment> wrapper = Wrappers.<WorkItemOtmComment>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(WorkItemOtmComment.class)) {
            wrapper.orderByDesc(WorkItemOtmComment::getUpdateTime, WorkItemOtmComment::getCreateTime);
        }
        return wrapper;
    }
}
