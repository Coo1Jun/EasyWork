package com.ew.communication.calendar.schedule.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ew.communication.calendar.schedule.dto.ScheduleQueryParam;
import com.ew.communication.calendar.schedule.dto.ScheduleAddParam;
import com.ew.communication.calendar.schedule.dto.ScheduleEditParam;
import com.ew.communication.calendar.schedule.dto.ScheduleDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;

import com.ew.communication.calendar.schedule.service.IScheduleService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 日程实体信息控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-30
 */
@RestController
@RequestMapping("/schedule")
@Api(tags = "日程实体信息服务接口")
public class ScheduleController {

    @Autowired
    private IScheduleService scheduleService;

    /**
     * 分页获取日程实体信息列表
     *
     * @author LiZhengFan
     * @since 2023-04-30
     */
    @ApiOperation("分页获取日程实体信息列表")
    @GetMapping("/list")
    public RestResponse<PageResult<ScheduleDto>> pageList(@Valid ScheduleQueryParam scheduleQueryParam) {
        return RestResponse.ok(scheduleService.pageDto(scheduleQueryParam));
    }

    /**
     * 新增日程实体信息
     *
     * @author LiZhengFan
     * @since 2023-04-30
     */
    @ApiOperation("新增日程实体信息")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid ScheduleAddParam scheduleAddParam) {
        return RestResponse.ok(scheduleService.saveByParam(scheduleAddParam));
    }

    /**
     * 修改日程实体信息
     *
     * @author LiZhengFan
     * @since 2023-04-30
     */
    @ApiOperation(value = "修改日程实体信息")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid ScheduleEditParam scheduleEditParam) {
        return RestResponse.ok(scheduleService.updateByParam(scheduleEditParam));
    }

    /**
     * 根据id删除日程实体信息
     *
     * @author LiZhengFan
     * @since 2023-04-30
     */
    @ApiOperation(value = "根据id删除日程实体信息")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(scheduleService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 根据id获取日程实体信息
     *
     * @author LiZhengFan
     * @since 2023-04-30
     */
    @ApiOperation(value = "根据id获取日程实体信息")
    @GetMapping(value = "/{id}")
    public RestResponse<ScheduleDto> get(@PathVariable String id) {
        return RestResponse.ok(scheduleService.getDtoById(id));
    }

}
