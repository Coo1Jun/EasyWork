package com.ew.communication.notification.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import com.ew.communication.notification.dto.*;
import com.ew.communication.notification.service.INotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;

/**
 * <p>
 * 通知实体信息控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-25
 */
@RestController
@RequestMapping("/notification")
@Api(tags = "通知实体信息服务接口")
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    /**
     * 分页获取通知实体信息列表
     *
     * @author LiZhengFan
     * @since 2023-04-25
     */
    @ApiOperation("分页获取通知实体信息列表")
    @GetMapping("/page/list")
    public RestResponse<PageResult<NotificationDto>> pageList(@Valid NotificationQueryParam notificationQueryParam) {
        return RestResponse.ok(notificationService.pageDto(notificationQueryParam));
    }

    /**
     * 获取通知实体信息列表
     *
     * @author LiZhengFan
     * @since 2023-04-25
     */
    @ApiOperation("获取通知实体信息列表")
    @GetMapping("/list")
    public RestResponse<NotificationResult> list(@Valid NotificationQueryParam notificationQueryParam) {
        return RestResponse.ok(notificationService.getList(notificationQueryParam));
    }


    /**
     * 新增通知实体信息
     *
     * @author LiZhengFan
     * @since 2023-04-25
     */
    @ApiOperation("新增通知实体信息")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid NotificationAddParam notificationAddParam) {
        return RestResponse.ok(notificationService.saveByParam(notificationAddParam));
    }

    /**
     * 项目组邀请成员
     */
    @ApiOperation("项目组邀请成员")
    @PostMapping("/group/invite")
    public RestResponse<Boolean> groupInvite(@RequestBody GroupInvitation groupInvitation) {
        return RestResponse.ok(notificationService.groupInvite(groupInvitation));
    }

    /**
     * 修改通知实体信息
     *
     * @author LiZhengFan
     * @since 2023-04-25
     */
    @ApiOperation(value = "修改通知实体信息")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid NotificationEditParam notificationEditParam) {
        return RestResponse.ok(notificationService.updateByParam(notificationEditParam));
    }

    /**
     * 根据id删除通知实体信息
     *
     * @author LiZhengFan
     * @since 2023-04-25
     */
    @ApiOperation(value = "根据id删除通知实体信息")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(notificationService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 根据id获取通知实体信息
     *
     * @author LiZhengFan
     * @since 2023-04-25
     */
    @ApiOperation(value = "根据id获取通知实体信息")
    @GetMapping(value = "/{id}")
    public RestResponse<NotificationDto> get(@PathVariable String id) {
        return RestResponse.ok(notificationService.getDtoById(id));
    }

}
