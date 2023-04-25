package cn.edu.hzu.client.server.service;

import cn.edu.hzu.client.dto.GroupDto;
import cn.edu.hzu.client.dto.ProjectDto;
import cn.edu.hzu.client.dto.UserMtmGroup;
import cn.edu.hzu.client.dto.WorkItemDto;
import cn.edu.hzu.common.api.RestResponse;

import java.util.List;

public interface IProjectClientService {

    /**
     * 根据项目组id获取用户id集合
     *
     * @param throwEx
     * @param groupId
     * @return
     */
    RestResponse<List<String>> getUserIdsByGroupIdClient(boolean throwEx, String groupId);

    default List<String> getUserIdsByGroupId(String groupId) {
        RestResponse<List<String>> response = getUserIdsByGroupIdClient(true, groupId);
        return response.getData();
    }

    /**
     * 根据项目id获取项目信息
     */
    RestResponse<GroupDto> getGroupInfoByIdClient(boolean throwEx, String groupId);

    default GroupDto getGroupInfoById(String groupId) {
        RestResponse<GroupDto> response = getGroupInfoByIdClient(true, groupId);
        return response.getData();
    }

    /**
     * 根据项目id获取工作项信息
     */
    RestResponse<WorkItemDto> getWorkItemByIdClient(boolean throwEx, String id);

    default WorkItemDto getWorkItemById(String id) {
        RestResponse<WorkItemDto> response = getWorkItemByIdClient(true, id);
        return response.getData();
    }

    /**
     * 添加项目组成员
     */
    RestResponse<Boolean> addGroupMemberClient(boolean throwEx, UserMtmGroup userMtmGroup);

    default Boolean addGroupMember(UserMtmGroup userMtmGroup) {
        RestResponse<Boolean> response = addGroupMemberClient(true, userMtmGroup);
        return response.getData();
    }

    /**
     * 根据项目id获取项目基本信息
     */
    RestResponse<ProjectDto> getProjectInfoByIdClient(boolean throwEx, String id);
    default ProjectDto getProjectInfoById(String id) {
        RestResponse<ProjectDto> response = getProjectInfoByIdClient(true, id);
        return response.getData();
    }
}
