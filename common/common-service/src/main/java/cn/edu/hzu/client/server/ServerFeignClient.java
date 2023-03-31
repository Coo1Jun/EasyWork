package cn.edu.hzu.client.server;


import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.common.api.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lzf
 * @create 2023/03/31
 * @description ew-server 服务调用
 */
@FeignClient("ew-server")
@Component
public interface ServerFeignClient {

    @GetMapping("/api/ew-server/client/user/{id}")
    RestResponse<UserDto> getUserDtoById(@PathVariable String id);
}
