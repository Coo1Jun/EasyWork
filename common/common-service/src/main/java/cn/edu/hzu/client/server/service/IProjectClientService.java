package cn.edu.hzu.client.server.service;

import cn.edu.hzu.common.api.RestResponse;

import java.util.List;

public interface IProjectClientService {

    /**
     * 根据项目组id获取用户id集合
     * @param throwEx
     * @param groupId
     * @return
     */
    RestResponse<List<String>> getUserIdsByGroupIdClient(boolean throwEx, String groupId);
    default List<String> getUserIdsByGroupId(String groupId) {
        RestResponse<List<String>> response = getUserIdsByGroupIdClient(true, groupId);
        return response.getData();
    }
}
