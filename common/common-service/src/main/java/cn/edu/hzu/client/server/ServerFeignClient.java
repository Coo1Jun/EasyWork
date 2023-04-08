package cn.edu.hzu.client.server;


import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.common.api.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lzf
 * @create 2023/03/31
 * @description ew-server 服务调用
 */
@FeignClient("ew-server")
@Component
public interface ServerFeignClient {
    /**
     *  Spring Cloud OpenFeign 内部通过反射机制自动将响应结果转换成了 Feign 接口方法中的返回类型。
     *  即 这里的'UserDto'与ew-server中/api/ew-server/client/user/{id}接口的返回值类型'UserDto'是不用的两个类
     */
    @GetMapping("/api/ew-server/client/user/{id}")
    RestResponse<UserDto> getUserDtoById(@PathVariable String id);

    /**
     * 根据用户id集合获取用户基本信息
     * @param ids 用户id集合
     * @return
     */
    @GetMapping("/api/ew-server/client/user/list")
    RestResponse<List<UserDto>> getUserListByIds(@RequestParam("ids") List<String> ids);
}
