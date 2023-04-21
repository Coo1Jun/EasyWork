package cn.edu.hzu.client.server.service.impl;

import cn.edu.hzu.client.AbstractBaseServiceClient;
import cn.edu.hzu.client.dto.UserDto;
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
}
