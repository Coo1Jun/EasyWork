package com.ew.project.workitem.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import com.ew.project.workitem.dto.*;
import com.ew.project.workitem.service.IWorkItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作项基本信息控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-07
 */
@RestController
@RequestMapping("/workitem")
@Api(tags = "工作项基本信息服务接口")
public class WorkItemController {

    @Autowired
    private IWorkItemService workItemService;

    /**
     * 根据项目id和EpicId，获取工作项基本信息列表
     *
     * @author LiZhengFan
     * @since 2023-04-08
     */
    @ApiOperation("根据项目id和EpicId，获取工作项基本信息列表")
    @GetMapping("/list")
    public RestResponse<Map<String, List<WorkItemDto>>> workItemList(@Valid WorkItemQueryParam workItemQueryParam) {
        return RestResponse.ok(workItemService.workItemList(workItemQueryParam));
    }

    /**
     * 根据项目id和EpicId，获取工作项基本信息列表的tree树形数据
     *
     * @author LiZhengFan
     * @since 2023-04-08
     */
    @ApiOperation("根据项目id和EpicId，获取工作项基本信息列表的tree树形数据")
    @GetMapping("/tree")
    public RestResponse<List<WorkItemDto>> workItemTree(@Valid WorkItemQueryParam workItemQueryParam) {
        return RestResponse.ok(workItemService.workItemTreeData(workItemQueryParam));
    }

    /**
     * 根据项目id获取当前项目的所有计划集
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation("根据项目id获取当前项目的所有计划集")
    @GetMapping("/plans/list")
    public RestResponse<List<WorkItemDto>> plansList(@Valid WorkItemQueryParam workItemQueryParam) {
        return RestResponse.ok(workItemService.getPlans(workItemQueryParam));
    }

    /**
     * 根据项目id和EpicId获取参与项目工作的用户基本信息
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation("根据项目id和EpicId获取参与项目工作的用户基本信息")
    @GetMapping("/user/list")
    public RestResponse<List<WorkItemUserDto>> workItemUserList(@Valid WorkItemQueryParam workItemQueryParam) {
        return RestResponse.ok(workItemService.workItemUserList(workItemQueryParam));
    }

    /**
     * 根据项目id和EpicId，计算工作项统计信息
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation("根据项目id和EpicId，计算工作项统计信息")
    @GetMapping("/statistics")
    public RestResponse<WorkItemDataDto> workItemStatistics(@Valid WorkItemQueryParam workItemQueryParam) {
        return RestResponse.ok(workItemService.workItemStatistics(workItemQueryParam));
    }

    /**
     * 新增工作项基本信息
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation("新增工作项基本信息")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid WorkItemAddParam workItemAddParam) {
        return RestResponse.ok(workItemService.saveByParam(workItemAddParam));
    }

    /**
     * 新增计划集信息
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation("新增计划集信息")
    @PostMapping("/plans")
    public RestResponse<Boolean> addPlans(@RequestBody @Valid WorkItemAddParam workItemAddParam) {
        return RestResponse.ok(workItemService.savePlans(workItemAddParam));
    }

    /**
     * 修改工作项基本信息
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation(value = "修改工作项基本信息")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid WorkItemEditParam workItemEditParam) {
        return RestResponse.ok(workItemService.updateByParam(workItemEditParam));
    }

    /**
     * 根据id删除工作项基本信息
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation(value = "根据id删除工作项基本信息")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(workItemService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 根据id获取工作项基本信息
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation(value = "根据id获取工作项基本信息")
    @GetMapping(value = "/one")
    public RestResponse<WorkItemDto> get(@Valid WorkItemQueryParam workItemQueryParam) {
        return RestResponse.ok(workItemService.getDtoById(workItemQueryParam));
    }

    /**
     * 导出工作项基本信息列表
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation(value = "导出工作项基本信息列表")
    @GetMapping("/export")
    public void excelExport(@Valid WorkItemQueryParam workItemQueryParam, HttpServletResponse response) {
        PageResult<WorkItemDto> pageResult = workItemService.pageDto(workItemQueryParam);
        ExcelUtils<WorkItemDto> util = new ExcelUtils<>(WorkItemDto.class);

        util.exportExcelAndDownload(pageResult.getRecords(), "工作项基本信息", response);
    }

}
