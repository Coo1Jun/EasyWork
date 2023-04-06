package com.ew.project.project.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ew.project.project.dto.ProjectQueryParam;
import com.ew.project.project.dto.ProjectAddParam;
import com.ew.project.project.dto.ProjectEditParam;
import com.ew.project.project.dto.ProjectDto;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import com.ew.project.project.service.IProjectService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 项目信息控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-02
 *
 */
@RestController
@RequestMapping("/project")
@Api(tags = "项目信息服务接口")
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    /**
     *
     * 分页获取项目信息列表
     *
     * @author LiZhengFan
     * @since 2023-04-02
     *
     */
    @ApiOperation("分页获取项目信息列表")
    @GetMapping("/list")
    public RestResponse<PageResult<ProjectDto>> pageList(@Valid ProjectQueryParam projectQueryParam){
        return RestResponse.ok(projectService.pageDto(projectQueryParam));
    }

    /**
     *
     * 新增项目信息
     *
     * @author LiZhengFan
     * @since 2023-04-02
     *
     */
    @ApiOperation("新增项目信息")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid ProjectAddParam projectAddParam) {
        return RestResponse.ok(projectService.saveByParam(projectAddParam));
    }

    /**
     *
     * 修改项目信息
     *
     * @author LiZhengFan
     * @since 2023-04-02
     *
     */
    @ApiOperation(value = "修改项目信息")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid ProjectEditParam projectEditParam) {
        return RestResponse.ok(projectService.updateByParam(projectEditParam));
    }

    /**
     *
     * 根据id删除项目信息
     *
     * @author LiZhengFan
     * @since 2023-04-02
     *
     */
    @ApiOperation(value = "根据id删除项目信息")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(projectService.removeByIds(Arrays.asList(ids)));
    }

    /**
     *
     * 根据id获取项目信息
     *
     * @author LiZhengFan
     * @since 2023-04-02
     *
     */
    @ApiOperation(value = "根据id获取项目信息")
    @GetMapping(value = "/{id}")
    public RestResponse<ProjectDto> get(@PathVariable String id) {
        return RestResponse.ok(projectService.getDtoById(id));
    }

    /**
     *
     * 导出项目信息列表
     *
     * @author LiZhengFan
     * @since 2023-04-02
     *
     */
    @ApiOperation(value = "导出项目信息列表")
    @GetMapping("/export")
    public void excelExport(@Valid ProjectQueryParam projectQueryParam, HttpServletResponse response){
        PageResult<ProjectDto> pageResult  = projectService.pageDto(projectQueryParam);
        ExcelUtils<ProjectDto> util = new ExcelUtils<>(ProjectDto.class);

        util.exportExcelAndDownload(pageResult.getRecords(), "项目信息", response);
    }

    /**
     *
     * Excel导入项目信息
     *
     * @author LiZhengFan
     * @since 2023-04-02
     *
     */
    @ApiOperation(value = "Excel导入项目信息")
    @PostMapping("/import")
    public RestResponse<Boolean> excelImport(@RequestParam("file") MultipartFile file){
        ExcelUtils<ProjectDto> util = new ExcelUtils<>(ProjectDto.class);
        List<ProjectDto> rows = util.importExcel(file);
        if (CollectionUtils.isEmpty(rows)) {
            throw new CommonException(CommonErrorEnum.IMPORT_DATA_EMPTY);
        }
        return RestResponse.ok(projectService.saveDtoBatch(rows));
    }

}
