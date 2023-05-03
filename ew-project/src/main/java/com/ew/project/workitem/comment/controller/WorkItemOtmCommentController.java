package com.ew.project.workitem.comment.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentQueryParam;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentAddParam;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentEditParam;
import com.ew.project.workitem.comment.dto.WorkItemOtmCommentDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;

import com.ew.project.workitem.comment.service.IWorkItemOtmCommentService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 工作项和评论实体信息控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-05-03
 */
@RestController
@RequestMapping("/workitem/comment")
@Api(tags = "工作项和评论实体信息服务接口")
public class WorkItemOtmCommentController {

    @Autowired
    private IWorkItemOtmCommentService workItemOtmCommentService;

    /**
     * 分页获取工作项和评论实体信息列表
     *
     * @author LiZhengFan
     * @since 2023-05-03
     */
    @ApiOperation("分页获取工作项和评论实体信息列表")
    @GetMapping("/list")
    public RestResponse<PageResult<WorkItemOtmCommentDto>> pageList(@Valid WorkItemOtmCommentQueryParam workItemOtmCommentQueryParam) {
        return RestResponse.ok(workItemOtmCommentService.pageDto(workItemOtmCommentQueryParam));
    }

    /**
     * 新增工作项和评论实体信息
     *
     * @author LiZhengFan
     * @since 2023-05-03
     */
    @ApiOperation("新增工作项和评论实体信息")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid WorkItemOtmCommentAddParam workItemOtmCommentAddParam) {
        return RestResponse.ok(workItemOtmCommentService.saveByParam(workItemOtmCommentAddParam));
    }

    /**
     * 修改工作项和评论实体信息
     *
     * @author LiZhengFan
     * @since 2023-05-03
     */
    @ApiOperation(value = "修改工作项和评论实体信息")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid WorkItemOtmCommentEditParam workItemOtmCommentEditParam) {
        return RestResponse.ok(workItemOtmCommentService.updateByParam(workItemOtmCommentEditParam));
    }

    /**
     * 根据id删除工作项和评论实体信息
     *
     * @author LiZhengFan
     * @since 2023-05-03
     */
    @ApiOperation(value = "根据id删除工作项和评论实体信息")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(workItemOtmCommentService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 根据id获取工作项和评论实体信息
     *
     * @author LiZhengFan
     * @since 2023-05-03
     */
    @ApiOperation(value = "根据id获取工作项和评论实体信息")
    @GetMapping(value = "/{id}")
    public RestResponse<WorkItemOtmCommentDto> get(@PathVariable String id) {
        return RestResponse.ok(workItemOtmCommentService.getDtoById(id));
    }

    /**
     * 导出工作项和评论实体信息列表
     *
     * @author LiZhengFan
     * @since 2023-05-03
     */
    @ApiOperation(value = "导出工作项和评论实体信息列表")
    @GetMapping("/export")
    public void excelExport(@Valid WorkItemOtmCommentQueryParam workItemOtmCommentQueryParam, HttpServletResponse response) {
        PageResult<WorkItemOtmCommentDto> pageResult = workItemOtmCommentService.pageDto(workItemOtmCommentQueryParam);
        ExcelUtils<WorkItemOtmCommentDto> util = new ExcelUtils<>(WorkItemOtmCommentDto.class);

        util.exportExcelAndDownload(pageResult.getRecords(), "工作项和评论实体信息", response);
    }

    /**
     * Excel导入工作项和评论实体信息
     *
     * @author LiZhengFan
     * @since 2023-05-03
     */
    @ApiOperation(value = "Excel导入工作项和评论实体信息")
    @PostMapping("/import")
    public RestResponse<Boolean> excelImport(@RequestParam("file") MultipartFile file) {
        ExcelUtils<WorkItemOtmCommentDto> util = new ExcelUtils<>(WorkItemOtmCommentDto.class);
        List<WorkItemOtmCommentDto> rows = util.importExcel(file);
        if (CollectionUtils.isEmpty(rows)) {
            throw new CommonException(CommonErrorEnum.IMPORT_DATA_EMPTY);
        }
        return RestResponse.ok(workItemOtmCommentService.saveDtoBatch(rows));
    }

}
