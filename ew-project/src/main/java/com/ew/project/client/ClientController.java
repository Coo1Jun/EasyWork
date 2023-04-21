package com.ew.project.client;

import cn.edu.hzu.common.api.RestResponse;
import com.ew.project.group.mapper.UserMtmGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lzf
 * @create 2023/04/21
 * @description 服务调用控制器
 */
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private UserMtmGroupMapper userMtmGroupMapper;

    @GetMapping("/group/user/list")
    public RestResponse<List<String>> getUserIdsByGroupId(String groupId) {
        return RestResponse.ok(userMtmGroupMapper.getUserIdsByGroupId(groupId));
    }
}
