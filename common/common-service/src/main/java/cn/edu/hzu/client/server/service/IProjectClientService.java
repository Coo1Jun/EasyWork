package cn.edu.hzu.client.server.service;

import cn.edu.hzu.client.dto.GroupDto;
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
}
