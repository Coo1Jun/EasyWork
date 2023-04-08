package cn.edu.hzu.client.server.service.impl;

import cn.edu.hzu.client.AbstractBaseServiceClient;
import cn.edu.hzu.client.server.ServerFeignClient;
import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.client.server.service.IServerClientService;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.enums.ProjectEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzf
 * @create 2023/03/31
 * @description ew-server服务调用是实现类
 */
@Service
@Slf4j
public class IServerClientServiceImpl extends AbstractBaseServiceClient implements IServerClientService {
    @Autowired
    private ServerFeignClient serverFeignClient;

    @Override
    public RestResponse<UserDto> getUserDtoByIdClient(boolean throwEx, String id) {
        execBefore(ProjectEnum.EW_SERVER, id);
        RestResponse<UserDto> result = serverFeignClient.getUserDtoById(id);
        execAfter(throwEx, ProjectEnum.EW_SERVER, result);
        return result;
    }

    @Override
    public RestResponse<List<UserDto>> getUserListByIdsClient(boolean throwEx, List<String> ids) {
        execBefore(ProjectEnum.EW_SERVER, ids);
        RestResponse<List<UserDto>> result = serverFeignClient.getUserListByIds(ids);
        execAfter(throwEx, ProjectEnum.EW_SERVER, result);
        return result;
    }
}
