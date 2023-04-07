package com.ew.project.workitem.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ew.project.workitem.dto.WorkItemQueryParam;
import com.ew.project.workitem.dto.WorkItemAddParam;
import com.ew.project.workitem.dto.WorkItemEditParam;
import com.ew.project.workitem.dto.WorkItemDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;

import com.ew.project.workitem.service.IWorkItemService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
     * 分页获取工作项基本信息列表
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation("分页获取工作项基本信息列表")
    @GetMapping("/list")
    public RestResponse<PageResult<WorkItemDto>> pageList(@Valid WorkItemQueryParam workItemQueryParam) {
        return RestResponse.ok(workItemService.pageDto(workItemQueryParam));
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
    @GetMapping(value = "/{id}")
    public RestResponse<WorkItemDto> get(@PathVariable String id) {
        return RestResponse.ok(workItemService.getDtoById(id));
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

    /**
     * Excel导入工作项基本信息
     *
     * @author LiZhengFan
     * @since 2023-04-07
     */
    @ApiOperation(value = "Excel导入工作项基本信息")
    @PostMapping("/import")
    public RestResponse<Boolean> excelImport(@RequestParam("file") MultipartFile file) {
        ExcelUtils<WorkItemDto> util = new ExcelUtils<>(WorkItemDto.class);
        List<WorkItemDto> rows = util.importExcel(file);
        if (CollectionUtils.isEmpty(rows)) {
            throw new CommonException(CommonErrorEnum.IMPORT_DATA_EMPTY);
        }
        return RestResponse.ok(workItemService.saveDtoBatch(rows));
    }

}
