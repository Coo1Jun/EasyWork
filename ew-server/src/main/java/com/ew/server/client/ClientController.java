package com.ew.server.client;

import cn.edu.hzu.common.api.RestResponse;
import com.ew.server.user.dto.UserDto;
import com.ew.server.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lzf
 * @create 2023/03/31
 * @description 服务调用控制器
 */
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private IUserService userService;

    // user用户服务 start ==========================================================
    /**
     *
     * 根据id获取用户信息
     *
     * @author LiZhengFan
     * @since 2023-02-13
     *
     */
    @GetMapping(value = "/user/{id}")
    public RestResponse<UserDto> get(@PathVariable String id) {
        return RestResponse.ok(userService.getDtoById(id));
    }

    /**
     * 根据用户id集合，获取用户信息列表
     */
    @GetMapping(value = "/user/list")
    public RestResponse<List<UserDto>> getUserList(@RequestParam("ids") List<String> ids) {
        return RestResponse.ok(userService.getUserListByIds(ids));
    }

    // user用户服务 end ============================================================
}
