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
     * 根据工作项id获取子工作项
     *
     * @author LiZhengFan
     * @since 2023-04-23
     */
    @ApiOperation("根据工作项id获取子工作项")
    @GetMapping("/sub/{parentId}")
    public RestResponse<List<WorkItemDto>> getSubWorkItem(@PathVariable("parentId") @NotEmpty String parentId) {
        return RestResponse.ok(workItemService.subWorkItemList(parentId));
    }

    /**
     * 获取用户已经延期的工作项
     *
     * @author LiZhengFan
     * @since 2023-04-23
     */
    @ApiOperation("获取用户已经延期的工作项")
    @GetMapping("/delayed")
    public RestResponse<List<WorkItemDto>> getWorkItemDelay() {
        return RestResponse.ok(workItemService.getWorkItemDelayByUser());
    }

    /**
     * 获取用户即将延期的工作项（截止日期小于三天）
     *
     * @author LiZhengFan
     * @since 2023-04-23
     */
    @ApiOperation("获取用户即将延期的工作项（截止日期小于三天）")
    @GetMapping("/near/delay")
    public RestResponse<List<WorkItemDto>> getWorkItemNearDelay() {
        return RestResponse.ok(workItemService.getWorkItemNearDelayByUser());
    }
    /**
     * 获取还未延期，并且截止日期大于三天，还没完成的工作项
     *
     * @author LiZhengFan
     * @since 2023-04-23
     */
    @ApiOperation("获取还未延期，并且截止日期大于三天，还没完成的工作项")
    @GetMapping("/other")
    public RestResponse<List<WorkItemDto>> getWorkItemOther() {
        return RestResponse.ok(workItemService.getWorkItemOther());
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
     * 根据id删除工作项基本信息（Plans和Epic）
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation(value = "根据id删除工作项基本信息->pe")
    @DeleteMapping("/pe/{id}")
    public RestResponse<Boolean> deletePlansAndEpic(@PathVariable("id") @NotEmpty String id) {
        return RestResponse.ok(workItemService.removeById(id));
    }

    /**
     * 根据id删除工作项基本信息（除了Plans和Epic的其他类型的工作项）
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation(value = "根据id删除工作项基本信息->fstb")
    @DeleteMapping("/fstb/{id}")
    public RestResponse<Boolean> delete(@PathVariable("id") @NotEmpty String id) {
        return RestResponse.ok(workItemService.removeWorkItem(id));
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
