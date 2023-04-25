package com.ew.communication.notification.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ew.communication.notification.dto.NotificationQueryParam;
import com.ew.communication.notification.dto.NotificationAddParam;
import com.ew.communication.notification.dto.NotificationEditParam;
import com.ew.communication.notification.dto.NotificationDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;

import com.ew.communication.notification.service.INotificationService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/list")
    public RestResponse<PageResult<NotificationDto>> pageList(@Valid NotificationQueryParam notificationQueryParam) {
        return RestResponse.ok(notificationService.pageDto(notificationQueryParam));
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
