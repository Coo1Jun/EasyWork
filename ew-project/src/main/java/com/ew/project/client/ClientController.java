package com.ew.project.client;

import cn.edu.hzu.common.api.RestResponse;
import com.ew.project.group.dto.GroupDto;
import com.ew.project.group.entity.UserMtmGroup;
import com.ew.project.group.mapper.UserMtmGroupMapper;
import com.ew.project.group.service.IGroupService;
import com.ew.project.group.service.IUserMtmGroupService;
import com.ew.project.project.dto.ProjectDto;
import com.ew.project.project.service.IProjectService;
import com.ew.project.workitem.dto.WorkItemDto;
import com.ew.project.workitem.service.IWorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private IUserMtmGroupService userMtmGroupService;
    @Autowired
    private IGroupService groupService;
    @Autowired
    private IWorkItemService workItemService;
    @Autowired
    private IProjectService projectService;

    @GetMapping("/group/user/list")
    public RestResponse<List<String>> getUserIdsByGroupId(@RequestParam("groupId") String groupId) {
        return RestResponse.ok(userMtmGroupMapper.getUserIdsByGroupId(groupId));
    }

    /**
     * 添加项目组成员
     * @return
     */
    @PostMapping("/group/member/add")
    public RestResponse<Boolean> addGroupMember(@RequestBody UserMtmGroup userMtmGroup) {
        return RestResponse.ok(userMtmGroupService.save(userMtmGroup));
    }

    @GetMapping("/group/info")
    public RestResponse<GroupDto> getGroupInfoById(@RequestParam("groupId") String groupId) {
        return RestResponse.ok(groupService.getDtoById(groupId));
    }

    /**
     * 获取工作项基本信息
     */
    @GetMapping("/work/info/{id}")
    public RestResponse<WorkItemDto> getWorkItemById(@PathVariable("id") String id) {
        return RestResponse.ok(workItemService.getDtoById(id));
    }

    /**
     * 根据项目id获取项目基本信息
     */
    @GetMapping("/project/info/{id}")
    public RestResponse<ProjectDto> getProjectById(@PathVariable("id") String id) {
        return RestResponse.ok(projectService.getDtoById(id));
    }
}
