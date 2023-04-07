package com.ew.project.workitem.service.impl;

import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ew.project.workitem.constants.WorkItemConstant;
import com.ew.project.workitem.entity.WorkItem;
import com.ew.project.workitem.entity.WorkItemOtmFile;
import com.ew.project.workitem.enums.WorkItemErrorEnum;
import com.ew.project.workitem.mapper.WorkItemMapper;
import com.ew.project.workitem.service.IWorkItemOtmFileService;
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

import java.util.ArrayList;
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
    @Autowired
    private IWorkItemOtmFileService workItemOtmFileService;

    @Override
    public PageResult<WorkItemDto> pageDto(WorkItemQueryParam workItemQueryParam) {
        Wrapper<WorkItem> wrapper = getPageSearchWrapper(workItemQueryParam);
        PageResult<WorkItemDto> result = workItemParamMapper.pageEntity2Dto(page(workItemQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(WorkItemAddParam addParam) {
        // 校验参数
        String param = "";
        if (StringUtils.isEmpty(addParam.getTitle())) {
            param = "标题";
        } else if (StringUtils.isEmpty(addParam.getProjectId())) {
            param = "所属项目";
        } else if (StringUtils.isEmpty(addParam.getWorkType())) {
            param = "工作项类型";
        } else if (addParam.getStartTime() == null || addParam.getEndTime() == null) {
            param = "完成时间";
        }
        // 根据工作项类型判定父工作项，如果不是Epic，则需要判断父工作项是否为空
        if (!WorkItemConstant.EPIC.equals(addParam.getWorkType())
                && StringUtils.isEmpty(addParam.getParentWorkItemId())) {
            param = "父工作项";
        }
        if (!"".equals(param)) {
            throw CommonException.builder()
                    .resultCode(WorkItemErrorEnum.PARAMETER_EMPTY.setParams(new Object[]{param}))
                    .build();
        }
        WorkItem workItem = workItemParamMapper.addParam2Entity(addParam);
        // 如果优先级为空，默认优先级为1
        if (workItem.getPriority() == null || workItem.getPriority() < 0) {
            workItem.setPriority(1); // 默认优先级
        } else if (workItem.getPriority() > 5) {
            workItem.setPriority(5); // 最高为5
        }
        // 风险等级
        if (workItem.getRisk() == null || workItem.getRisk() < 0) {
            workItem.setRisk(0); // 默认风险等级
        } else if (workItem.getRisk() > 3) {
            workItem.setRisk(3); // 最高风险等级为3
        }
        // 赋值编号，如果不是Epic，则想要赋值编号
        if (!WorkItemConstant.EPIC.equals(workItem.getWorkType())) {
            // 查询数据库，获得当前项目的最高值
            Integer maxNumber = this.getBaseMapper().getMaxNumber(workItem.getProjectId());
            workItem.setNumber(maxNumber == null ? 0 : maxNumber + 1);
        }
        boolean result = save(workItem);
        if (!result) return false;
        // 保存文件列表
        if (CollectionUtils.isNotEmpty(addParam.getFileList())) {
            List<WorkItemOtmFile> fileList = new ArrayList<>();
            for (String fileId : addParam.getFileList()) {
                WorkItemOtmFile workItemOtmFile = new WorkItemOtmFile();
                workItemOtmFile.setFileId(fileId);
                workItemOtmFile.setWorkItemId(workItem.getId());
                fileList.add(workItemOtmFile);
            }
            workItemOtmFileService.saveBatch(fileList);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean savePlans(WorkItemAddParam addParam) {
        String param = "";
        if (StringUtils.isEmpty(addParam.getTitle())) {
            param = "标题";
        } else if (StringUtils.isEmpty(addParam.getProjectId())) {
            param = "所属项目";
        }
        if (!"".equals(param)) {
            throw CommonException.builder()
                    .resultCode(WorkItemErrorEnum.PARAMETER_EMPTY.setParams(new Object[]{param}))
                    .build();
        }
        // 标题唯一性
        int count = this.count(Wrappers.<WorkItem>lambdaQuery()
                .eq(WorkItem::getProjectId, addParam.getProjectId())
                .eq(WorkItem::getTitle, addParam.getTitle()));
        if (count > 0) {
            throw CommonException.builder()
                    .resultCode(WorkItemErrorEnum.PLANS_TITLE_EXIST.setParams(new Object[]{addParam.getTitle()}))
                    .build();
        }
        WorkItem workItem = new WorkItem();
        workItem.setTitle(addParam.getTitle());
        workItem.setProjectId(addParam.getProjectId());
        workItem.setWorkType(WorkItemConstant.PLANS);
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
