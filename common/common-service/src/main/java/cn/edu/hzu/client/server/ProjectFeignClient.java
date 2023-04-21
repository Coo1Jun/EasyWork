package cn.edu.hzu.client.server;


import cn.edu.hzu.common.api.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("ew-project")
@Component
public interface ProjectFeignClient {
    /**
     * 根据项目组id，获取用户id集合
     */
    @GetMapping("/api/ew-project/client/group/user/list")
    RestResponse<List<String>> getUserIds(@RequestParam("groupId") String groupId);
}
