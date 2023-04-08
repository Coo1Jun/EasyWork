package cn.edu.hzu.client.server.service;


import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.common.api.RestResponse;

import java.util.List;


public interface IServerClientService {

    /**
     * 根据用户id获取用户基本信息
     * @param throwEx 是否抛出异常（当程序报错时）
     * @param id 用户id
     * @return
     * @author lzf
     * @date: 2023/3/31 14:58
     */
    RestResponse<UserDto> getUserDtoByIdClient(boolean throwEx, String id);
    default UserDto getUserDtoById(String id) {
        RestResponse<UserDto> response = getUserDtoByIdClient(true, id);
        return response.getData();
    }

    /**
     * 根据用户id集合获取用户基本信息
     * @param throwEx 是否抛出异常（当程序报错时）
     * @param ids 用户id集合
     * @return
     * @author lzf
     * @date: 2023/4/8 14:58
     */
    RestResponse<List<UserDto>> getUserListByIdsClient(boolean throwEx, List<String> ids);
    default List<UserDto> getUserListById(List<String> ids) {
        RestResponse<List<UserDto>> response = getUserListByIdsClient(true, ids);
        return response.getData();
    }
}
