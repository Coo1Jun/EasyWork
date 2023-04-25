package cn.edu.hzu.client.server.service.impl;

import cn.edu.hzu.client.AbstractBaseServiceClient;
import cn.edu.hzu.client.dto.*;
import cn.edu.hzu.client.server.ProjectFeignClient;
import cn.edu.hzu.client.server.service.IProjectClientService;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.enums.ProjectEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzf
 * @create 2023/04/21
 * @description ew-project服务调用的是实现类
 */
@Service
@Slf4j
public class ProjectClientServiceImpl extends AbstractBaseServiceClient implements IProjectClientService {

    @Autowired
    private ProjectFeignClient projectFeignClient;

    @Override
    public RestResponse<List<String>> getUserIdsByGroupIdClient(boolean throwEx, String groupId) {
        execBefore(ProjectEnum.EW_PROJECT, groupId);
        RestResponse<List<String>> result = projectFeignClient.getUserIds(groupId);
        execAfter(throwEx, ProjectEnum.EW_PROJECT, result);
        return result;
    }

    @Override
    public RestResponse<GroupDto> getGroupInfoByIdClient(boolean throwEx, String groupId) {
        execBefore(ProjectEnum.EW_PROJECT, groupId);
        RestResponse<GroupDto> result = projectFeignClient.getGroupInfoById(groupId);
        execAfter(throwEx, ProjectEnum.EW_PROJECT, result);
        return result;
    }

    @Override
    public RestResponse<WorkItemDto> getWorkItemByIdClient(boolean throwEx, String id) {
        execBefore(ProjectEnum.EW_PROJECT, id);
        RestResponse<WorkItemDto> result = projectFeignClient.getWorkItemById(id);
        execAfter(throwEx, ProjectEnum.EW_PROJECT, result);
        return result;
    }

    @Override
    public RestResponse<Boolean> addGroupMemberClient(boolean throwEx, UserMtmGroup userMtmGroup) {
        execBefore(ProjectEnum.EW_PROJECT, userMtmGroup);
        RestResponse<Boolean> result = projectFeignClient.addGroupMember(userMtmGroup);
        execAfter(throwEx, ProjectEnum.EW_PROJECT, result);
        return result;
    }

    @Override
    public RestResponse<ProjectDto> getProjectInfoByIdClient(boolean throwEx, String id) {
        execBefore(ProjectEnum.EW_PROJECT, id);
        RestResponse<ProjectDto> result = projectFeignClient.getProjectById(id);
        execAfter(throwEx, ProjectEnum.EW_PROJECT, result);
        return result;
    }
}
