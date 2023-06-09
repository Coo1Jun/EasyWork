package com.ew.project.workitem.service.impl;

import cn.edu.hzu.client.dto.FileMetaDto;
import cn.edu.hzu.client.dto.NotificationAddParam;
import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.client.server.service.ICommunicationClientService;
import cn.edu.hzu.client.server.service.IServerClientService;
import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.utils.DateUtils;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.api.utils.UserUtils;
import cn.edu.hzu.common.constant.NotificationType;
import cn.edu.hzu.common.entity.BaseEntity;
import cn.edu.hzu.common.exception.CommonException;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentDto;
import com.ew.project.workitem.comment.mapper.WorkItemOtmCommentMapper;
import com.ew.project.workitem.constants.WorkItemConstant;
import com.ew.project.workitem.dto.*;
import com.ew.project.workitem.entity.WorkItem;
import com.ew.project.workitem.entity.WorkItemOtmFile;
import com.ew.project.workitem.enums.WorkItemErrorEnum;
import com.ew.project.workitem.mapper.WorkItemMapper;
import com.ew.project.workitem.mapper.WorkItemOtmFileMapper;
import com.ew.project.workitem.service.IWorkItemOtmFileService;
import com.ew.project.workitem.service.IWorkItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;


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
@Transactional(rollbackFor = {Exception.class, Error.class})
public class WorkItemServiceImpl extends BaseServiceImpl<WorkItemMapper, WorkItem> implements IWorkItemService {

    @Autowired
    private WorkItemParamMapper workItemParamMapper;
    @Autowired
    private IWorkItemOtmFileService workItemOtmFileService;
    @Autowired
    private WorkItemOtmFileMapper workItemOtmFileMapper;

    @Autowired
    private IServerClientService serverClientService;

    @Autowired
    private ICommunicationClientService communicationClientService;

    @Autowired
    private WorkItemOtmCommentMapper workItemOtmCommentMapper;

    @Override
    public PageResult<WorkItemDto> pageDto(WorkItemQueryParam workItemQueryParam) {
        Wrapper<WorkItem> wrapper = getPageSearchWrapper(workItemQueryParam);
        PageResult<WorkItemDto> result = workItemParamMapper.pageEntity2Dto(page(workItemQueryParam, wrapper));

        return Optional.ofNullable(result).orElse(new PageResult<>());
    }

    @Override
    public Map<String, List<WorkItemDto>> workItemList(WorkItemQueryParam workItemQueryParam) {
        return workItemParamMapper.workItemListToWorkItemDtoList(this.list(Wrappers.<WorkItem>lambdaQuery()
                        .eq(WorkItem::getProjectId, workItemQueryParam.getProjectId())
                        .eq(WorkItem::getEpicId, workItemQueryParam.getEpicId())
                        .ne(WorkItem::getWorkType, WorkItemConstant.PLANS)
                        .ne(WorkItem::getWorkType, WorkItemConstant.EPIC)))
                .stream().collect(Collectors.groupingBy(WorkItemDto::getWorkType));
    }

    @Override
    public List<WorkItemDto> workItemTreeData(WorkItemQueryParam queryParam) {
        boolean isTreeData = false;
        if (StringUtils.isAllEmpty(queryParam.getWorkType(),
                queryParam.getStatus(),
                queryParam.getPrincipalId(),
                queryParam.getTitle())) {
            isTreeData = true;
        }
        // 根据项目id和EpicId查出所有Feature\Story\Task\Bug
        List<WorkItemDto> data = workItemParamMapper.workItemListToWorkItemDtoList(this.list(Wrappers.<WorkItem>lambdaQuery()
                .eq(WorkItem::getProjectId, queryParam.getProjectId())
                .eq(WorkItem::getEpicId, queryParam.getEpicId())
                // 负责人
                .eq(StringUtils.isNotEmpty(queryParam.getPrincipalId()),
                        WorkItem::getPrincipalId, queryParam.getPrincipalId())
                // 流程状态
                .eq(StringUtils.isNotEmpty(queryParam.getStatus()),
                        WorkItem::getStatus, queryParam.getStatus())
                // 类型
                .eq(StringUtils.isNotEmpty(queryParam.getWorkType()),
                        WorkItem::getWorkType, queryParam.getWorkType())
                // 标题
                .like(StringUtils.isNotEmpty(queryParam.getTitle()),
                        WorkItem::getTitle, queryParam.getTitle())
                .ne(WorkItem::getWorkType, WorkItemConstant.PLANS)
                .ne(WorkItem::getWorkType, WorkItemConstant.EPIC)
                .orderByAsc(WorkItem::getNumber)));
        // 根据父工作项的id分组，key：父工作项的id
        Map<String, List<WorkItemDto>> map = data.stream().collect(Collectors.groupingBy(WorkItemDto::getParentWorkItemId));
        List<WorkItemDto> result = new ArrayList<>();
        for (WorkItemDto dto : data) {
            // 赋值孩子工作项
            if (isTreeData) {
                dto.setChildren(map.get(dto.getId()));
            }
            // 给dto赋值信息
            setWorkItemDtoDetail(dto);
            // 赋值Feature工作项
            if (isTreeData) {
                if (WorkItemConstant.FEATURE.equals(dto.getWorkType())) {
                    result.add(dto);
                }
            } else {
                result.add(dto);
            }
            // 查出附件信息
            List<String> fileIdList = workItemOtmFileMapper.getFileIdByWorkItemId(dto.getId());
            if (CollectionUtils.isNotEmpty(fileIdList)) {
                List<FileMetaDto> fileListById = serverClientService.getFileListByIds(fileIdList);
                dto.setFileList(fileListById);
            }
            // 查出评论信息
            List<WorkItemOtmCommentDto> commentList = workItemOtmCommentMapper.getListByWorkItemId(dto.getId());
            dto.setComments(CollectionUtils.isEmpty(commentList) ? new ArrayList<>() : commentList);
        }
        return result;
    }

    private void setWorkItemDtoDetail(WorkItemDto dto) {
        long now = System.currentTimeMillis();
        Set<String> statusSet = WorkItemConstant.TASK_COMPLETION_FLAG;
        // 赋值负责人信息
        if (StringUtils.isEmpty(dto.getPrincipalId())) {
            dto.setPrincipal(new UserDto());
        } else {
            UserDto user = serverClientService.getUserDtoById(dto.getPrincipalId());
            if (user == null) {
                dto.setPrincipal(new UserDto());
            } else {
                dto.setPrincipal(user);
            }
        }
        // 赋值剩余时间
        long endTime = dto.getEndTime().getTime();
        if (endTime <= now) {
            dto.setRemainingTime("0");
        } else {
            dto.setRemainingTime(NumberUtil.roundStr((double) (endTime - now) / (24 * 60 * 60 * 1000), 1));
        }
        // 赋值工时
        dto.setManHour((int) (dto.getEndTime().getTime() - dto.getStartTime().getTime()) / (24 * 60 * 60 * 1000));
        // 赋值结束状态
        dto.setEndFlag(statusSet.contains(dto.getStatus()) ? 1 : 0);
    }

    @Override
    public List<WorkItemUserDto> workItemUserList(WorkItemQueryParam workItemQueryParam) {
        // 查询参与项目工作的所有用户，并且过滤掉没有负责人的数据，根据用户id分组，key:用户id
        Map<String, List<WorkItem>> userMap = this.list(Wrappers.<WorkItem>lambdaQuery()
                .eq(WorkItem::getProjectId, workItemQueryParam.getProjectId())
                .eq(WorkItem::getEpicId, workItemQueryParam.getEpicId())
                .ne(WorkItem::getWorkType, WorkItemConstant.PLANS)
                .ne(WorkItem::getWorkType, WorkItemConstant.EPIC)
                .isNotNull(WorkItem::getPrincipalId)).stream().collect(Collectors.groupingBy(WorkItem::getPrincipalId));
        List<WorkItemUserDto> result = new ArrayList<>();
        for (String id : userMap.keySet()) {
            if (StringUtils.isNotEmpty(id)) {
                WorkItemUserDto WorkItemUserDto = new WorkItemUserDto();
                List<WorkItem> workItems = userMap.get(id);
                UserDto userDtoById = serverClientService.getUserDtoById(id);
                WorkItemUserDto.setId(id);
                WorkItemUserDto.setTaskCount(workItems.size());
                WorkItemUserDto.setUsername(userDtoById.getRealName());
                WorkItemUserDto.setPortrait(userDtoById.getPortrait());
                // 计算已经完成的任务数量
                Set<String> statusSet = WorkItemConstant.TASK_COMPLETION_FLAG;
                int count = 0;
                for (WorkItem workItem : workItems) {
                    if (statusSet.contains(workItem.getStatus())) {
                        count++;
                    }
                }
                WorkItemUserDto.setCompletedTasks(count);
                result.add(WorkItemUserDto);
            }
        }
        return result;
    }

    @Override
    public WorkItemDataDto workItemStatistics(WorkItemQueryParam workItemQueryParam) {
        List<WorkItem> resultList = this.list(Wrappers.<WorkItem>lambdaQuery()
                .eq(WorkItem::getProjectId, workItemQueryParam.getProjectId())
                .eq(WorkItem::getEpicId, workItemQueryParam.getEpicId())
                .ne(WorkItem::getWorkType, WorkItemConstant.PLANS)
                .ne(WorkItem::getWorkType, WorkItemConstant.EPIC));
        int userCount = this.list(Wrappers.<WorkItem>lambdaQuery()
                .eq(WorkItem::getProjectId, workItemQueryParam.getProjectId())
                .eq(WorkItem::getEpicId, workItemQueryParam.getEpicId())
                .ne(WorkItem::getWorkType, WorkItemConstant.PLANS)
                .ne(WorkItem::getWorkType, WorkItemConstant.EPIC)
                .ne(WorkItem::getPrincipalId, "")
                .isNotNull(WorkItem::getPrincipalId)) // 查出对应的记录
                .stream().collect(Collectors.groupingBy(WorkItem::getPrincipalId)) // 根据用户id分组
                .keySet().size(); // 得到有多少个用户
        WorkItemDataDto result = new WorkItemDataDto();
        // 用户数量
        result.setUserCount(userCount);
        // 卡片总数
        result.setAllTaskCount(resultList.size());
        // 人均卡片
        if (userCount == 0) {
            result.setAverageTasks("0");
        } else {
            result.setAverageTasks(NumberUtil.roundStr((double) resultList.size() / (double) userCount, 1));
        }
        Set<String> statusSet = WorkItemConstant.TASK_COMPLETION_FLAG;
        int completed = 0;
        int delay = 0;
        Date now = new Date();
        for (WorkItem workItem : resultList) {
            if (statusSet.contains(workItem.getStatus())) {
                completed++;
            } else {
                if (workItem.getEndTime().compareTo(now) < 0) {
                    delay++;
                }
            }
        }
        // 已经完成的卡片
        result.setAllCompletedTasks(completed);
        // 剩余卡片
        result.setRemainingTasks(resultList.size() - completed);
        // 延期卡片
        result.setDelayedTasks(delay);
        // 剩余时间，单位：天
        WorkItem epicWorkItem = this.getOne(Wrappers.<WorkItem>lambdaQuery()
                .eq(WorkItem::getId, workItemQueryParam.getEpicId()));
        long endTime = epicWorkItem.getEndTime().getTime();
        long nowTime = now.getTime();
        if (endTime <= nowTime) {
            result.setRemainingTime("0");
        } else {
            result.setRemainingTime(NumberUtil.roundStr((double) (endTime - nowTime) / (24 * 60 * 60 * 1000), 1));
            log.info("Epic剩余时间：{}ms", endTime - nowTime);
        }
        // 完成百分比
        if (resultList.size() == 0) {
            result.setPercentage(0);
        } else {
            result.setPercentage((int) Math.ceil((double) completed / (double) resultList.size() * 100));
        }
        return result;
    }

    /**
     * 格式化浮点数
     * @param decimal
     * @param format ".1"表示保留小数点后1位
     * @return
     */
    private static String decimalToText(double decimal, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(decimal);
    }

    @Override
    public List<WorkItemDto> getPlans(WorkItemQueryParam workItemQueryParam) {
        if (StringUtils.isEmpty(workItemQueryParam.getProjectId())) {
            throw CommonException.builder()
                    .resultCode(WorkItemErrorEnum.PARAMETER_EMPTY.setParams(new Object[]{"所属项目"}))
                    .build();
        }
        List<WorkItemDto> list = workItemParamMapper.workItemListToWorkItemDtoList(
                this.list(Wrappers.<WorkItem>lambdaQuery()
                .eq(WorkItem::getProjectId, workItemQueryParam.getProjectId())
                .eq(WorkItem::getWorkType, WorkItemConstant.PLANS)));
        // 查出所有Epic
        List<WorkItemDto> children = workItemParamMapper.workItemListToWorkItemDtoList(
                this.list(Wrappers.<WorkItem>lambdaQuery()
                .eq(WorkItem::getProjectId, workItemQueryParam.getProjectId())
                .eq(WorkItem::getWorkType, WorkItemConstant.EPIC)));
        for (WorkItemDto dto : children) {
            // 查出附件信息
            List<String> fileIdList = workItemOtmFileMapper.getFileIdByWorkItemId(dto.getId());
            if (CollectionUtils.isNotEmpty(fileIdList)) {
                List<FileMetaDto> fileListById = serverClientService.getFileListByIds(fileIdList);
                dto.setFileList(fileListById);
            }
        }
        // 将Epic根据父id分组
        Map<String, List<WorkItemDto>> map = children.stream().collect(Collectors.groupingBy(WorkItemDto::getPlansId));
        for (WorkItemDto dto : list) {
            dto.setChildren(map.get(dto.getId()));
        }
        return list;
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
        if (WorkItemConstant.EPIC.equals(addParam.getWorkType())
                && StringUtils.isEmpty(addParam.getPlansId())) {
            param = "所属计划集";
        }
        if (!"".equals(param)) {
            throw CommonException.builder()
                    .resultCode(WorkItemErrorEnum.PARAMETER_EMPTY.setParams(new Object[]{param}))
                    .build();
        }
        WorkItem workItem = workItemParamMapper.addParam2Entity(addParam);
        // 如果优先级为空，默认优先级为1
        if (workItem.getPriority() == null || workItem.getPriority() <= 0) {
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
            workItem.setNumber(maxNumber == null ? 1 : maxNumber + 1);
            // 初始流程状态：新建
            workItem.setStatus("新建");
        }
        boolean result = save(workItem);
        if (!result) return false;
        // 保存文件列表
        saveFileList(workItem.getId(), addParam.getFileList());
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
    public boolean updateByParam(WorkItemEditParam editParam) {
        String param = "";
        // 对不能为空的字段判空
        // 修改标题
        if (WorkItemConstant.EDIT_TITLE.equals(editParam.getEditType())) {
            param = StringUtils.isEmpty(editParam.getTitle()) ? "标题" : "";
        } else if (WorkItemConstant.EDIT_STATUS.equals(editParam.getEditType())) { // 修改状态
            param = StringUtils.isEmpty(editParam.getStatus()) ? "流程状态" : "";
        } else if (WorkItemConstant.EDIT_DATE.equals(editParam.getEditType())) { // 修改日期
            if (editParam.getStartTime() == null || editParam.getEndTime() == null) {
                param = "完成日期";
            }
        }
        if (!"".equals(param)) {
            throw CommonException.builder()
                    .resultCode(WorkItemErrorEnum.PARAMETER_EMPTY.setParams(new Object[]{param}))
                    .build();
        }
        WorkItem workItem = workItemParamMapper.editParam2Entity(editParam);
        log.info("更新的实体===》{}", JSON.toJSONString(workItem));
        boolean result = updateById(workItem);
        if (!result) return false;
        String curUserId = UserUtils.getCurrentUser().getUserid();
        if (StringUtils.isNotEmpty(editParam.getPrincipalId()) && !editParam.getPrincipalId().equals(curUserId)) {
            // 如果是更换负责人，则给负责人发送通知，如果负责人是本人，则不用
            NotificationAddParam notificationAddParam = new NotificationAddParam();
            notificationAddParam.setType(NotificationType.WORK);
            notificationAddParam.setOperationId(workItem.getId());
            notificationAddParam.setFromId(curUserId);
            notificationAddParam.setUserId(editParam.getPrincipalId());
            communicationClientService.addNotification(notificationAddParam);
        }
        if (editParam.getUpdateFileList() != null && editParam.getUpdateFileList() == 1) {
            // 先删除原有的文件列表
            workItemOtmFileService.remove(Wrappers.<WorkItemOtmFile>lambdaQuery().eq(WorkItemOtmFile::getWorkItemId, editParam.getId()));
            // 保存文件列表
            saveFileList(editParam.getId(), editParam.getFileList());
        }
        return true;
    }

    @Override
    public boolean removeWorkItem(String id) {
        WorkItem workItem = this.getById(id);
        if (workItem == null) return true;
        if (WorkItemConstant.STORY.equals(workItem.getWorkType())) {
            // Story是倒数第二级，直接删除所有子项即可
            this.remove(Wrappers.<WorkItem>lambdaQuery().eq(WorkItem::getParentWorkItemId, id));
        } else if (WorkItemConstant.FEATURE.equals(workItem.getWorkType())) {
            // Feature是倒数第三级，除了要删除子项Story，还要删除子项的子项
            // 查出子项
            List<WorkItem> list = this.list(Wrappers.<WorkItem>lambdaQuery().eq(WorkItem::getParentWorkItemId, id));
            // 删除子项
            this.remove(Wrappers.<WorkItem>lambdaQuery().eq(WorkItem::getParentWorkItemId, id));
            // 遍历子项，删除子项的子项
            if (CollectionUtils.isNotEmpty(list)) {
                 for (WorkItem w : list) {
                     // 删除子项
                     this.remove(Wrappers.<WorkItem>lambdaQuery().eq(WorkItem::getParentWorkItemId, w.getId()));
                 }
            }
        }
        return this.removeById(id);
    }

    @Override
    public List<WorkItemDto> subWorkItemList(String parentId) {
        WorkItem parent = this.getById(parentId);
        List<WorkItemDto> result = workItemParamMapper.workItemListToWorkItemDtoList(
                this.list(Wrappers.<WorkItem>lambdaQuery().eq(WorkItem::getParentWorkItemId, parentId)));
        // 如果类型是Feature，继续查Story的子工作项
        if (WorkItemConstant.FEATURE.equals(parent.getWorkType())) {
            if (CollectionUtils.isNotEmpty(result)) {
                for (WorkItemDto dto : result) {
                    List<WorkItemDto> StorySub = workItemParamMapper.workItemListToWorkItemDtoList(
                            this.list(Wrappers.<WorkItem>lambdaQuery().eq(WorkItem::getParentWorkItemId, dto.getId())));
                    if (CollectionUtils.isNotEmpty(StorySub)) {
                        dto.setChildren(StorySub);
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(result)) {
            for (WorkItemDto dto : result) {
                // 给dto赋值信息
                setWorkItemDtoDetail(dto);
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<WorkItemDto> getWorkItemDelayByUser() {
        String userId = UserUtils.getCurrentUser().getUserid();
        // 工作项已经完成的标志
        Set<String> statusSet = WorkItemConstant.TASK_COMPLETION_FLAG;
        List<WorkItemDto> result = workItemParamMapper.workItemListToWorkItemDtoList(
                this.list(Wrappers.<WorkItem>lambdaQuery()
                .eq(WorkItem::getPrincipalId, userId)
                .notIn(WorkItem::getStatus, statusSet) // 不在完成状态
                .le(WorkItem::getEndTime, new Date()))); // 截止日期已经过了
        if (CollectionUtils.isNotEmpty(result)) {
            for (WorkItemDto dto : result) {
                setWorkItemDtoDetail(dto);
            }
        }
        return result;
    }

    @Override
    public List<WorkItemDto> getWorkItemNearDelayByUser() {
        String userId = UserUtils.getCurrentUser().getUserid();
        // 工作项已经完成的标志
        Set<String> statusSet = WorkItemConstant.TASK_COMPLETION_FLAG;
        // 获取三天后的日期
        Date date = DateUtils.addDays(new Date(), 3);
        List<WorkItemDto> result = workItemParamMapper.workItemListToWorkItemDtoList(
                this.list(Wrappers.<WorkItem>lambdaQuery()
                        .eq(WorkItem::getPrincipalId, userId)
                        .notIn(WorkItem::getStatus, statusSet) // 不在完成状态
                        .ge(WorkItem::getEndTime, new Date()) // 还没截止
                        .lt(WorkItem::getEndTime, date))); // 截止日期小于三天
        if (CollectionUtils.isNotEmpty(result)) {
            for (WorkItemDto dto : result) {
                setWorkItemDtoDetail(dto);
            }
        }
        return result;
    }

    @Override
    public List<WorkItemDto> getWorkItemOther() {
        String userId = UserUtils.getCurrentUser().getUserid();
        // 工作项已经完成的标志
        Set<String> statusSet = WorkItemConstant.TASK_COMPLETION_FLAG;
        // 获取三天后的日期
        Date date = DateUtils.addDays(new Date(), 3);
        List<WorkItemDto> result = workItemParamMapper.workItemListToWorkItemDtoList(
                this.list(Wrappers.<WorkItem>lambdaQuery()
                        .eq(WorkItem::getPrincipalId, userId)
                        .notIn(WorkItem::getStatus, statusSet) // 不在完成状态
                        .ge(WorkItem::getEndTime, date))); // 截止日期大于三天
        if (CollectionUtils.isNotEmpty(result)) {
            for (WorkItemDto dto : result) {
                setWorkItemDtoDetail(dto);
            }
        }
        return result;
    }

    @Override
    public List<WorkItemDto> getEndTimeBetween(Date start, Date end) {
        return workItemParamMapper.workItemListToWorkItemDtoList(
                this.list(Wrappers.<WorkItem>lambdaQuery().between(WorkItem::getEndTime, start, end)));
    }

    /**
     * 保存文件列表
     * @return
     */
    private boolean saveFileList(String workItemId, List<String> fileIdList) {
        if (CollectionUtils.isNotEmpty(fileIdList)) {
            List<WorkItemOtmFile> fileList = new ArrayList<>();
            for (String fileId : fileIdList) {
                WorkItemOtmFile workItemOtmFile = new WorkItemOtmFile();
                workItemOtmFile.setFileId(fileId);
                workItemOtmFile.setWorkItemId(workItemId);
                fileList.add(workItemOtmFile);
            }
            return workItemOtmFileService.saveBatch(fileList);
        }
        return false;
    }

    @Override
    public WorkItemDto getEpicDtoById(WorkItemQueryParam workItemQueryParam) {
        WorkItem epic = this.getOne(Wrappers.<WorkItem>lambdaQuery()
                .eq(StringUtils.isNotEmpty(workItemQueryParam.getProjectId()), WorkItem::getProjectId, workItemQueryParam.getProjectId())
                .eq(WorkItem::getId, workItemQueryParam.getEpicId()));
        return workItemParamMapper.entity2Dto(epic);
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
