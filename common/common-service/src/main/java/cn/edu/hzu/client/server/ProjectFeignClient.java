package cn.edu.hzu.client.server;


import cn.edu.hzu.client.dto.GroupDto;
import cn.edu.hzu.client.dto.ProjectDto;
import cn.edu.hzu.client.dto.UserMtmGroup;
import cn.edu.hzu.client.dto.WorkItemDto;
import cn.edu.hzu.common.api.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/api/ew-project/client/group/info")
    RestResponse<GroupDto> getGroupInfoById(@RequestParam("groupId") String groupId);

    @GetMapping("/api/ew-project/client/work/info/{id}")
    RestResponse<WorkItemDto> getWorkItemById(@PathVariable("id") String id);

    @GetMapping("/api/ew-project/client/group/member/add")
    RestResponse<Boolean> addGroupMember(@RequestBody UserMtmGroup userMtmGroup);

    /**
     * 根据项目id获取项目基本信息
     */
    @GetMapping("/api/ew-project/client/project/info/{id}")
    RestResponse<ProjectDto> getProjectById(@PathVariable("id") String id);
}
