package cn.edu.hzu.client.server.service;


import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.common.api.RestResponse;


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
}
