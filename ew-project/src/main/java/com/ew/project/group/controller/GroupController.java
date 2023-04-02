package com.ew.project.group.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ew.project.group.dto.*;
import com.ew.project.group.service.IUserMtmGroupService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import com.ew.project.group.service.IGroupService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 项目组信息控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-03-30
 *
 */
@RestController
@RequestMapping("/group")
@Api(tags = "项目组信息服务接口")
public class GroupController {

    @Autowired
    private IGroupService groupService;
    @Autowired
    private IUserMtmGroupService userMtmGroupService;

    /**
     *
     * 分页获取项目组信息列表
     *
     * @author LiZhengFan
     * @since 2023-03-30
     *
     */
    @ApiOperation("分页获取项目组信息列表")
    @GetMapping("/list")
    public RestResponse<PageResult<GroupDto>> pageList(@Valid GroupQueryParam groupQueryParam){
        return RestResponse.ok(groupService.pageDto(groupQueryParam));
    }

    /**
     *
     * 分页获取项目组用户信息列表
     *
     * @author LiZhengFan
     * @since 2023-04-02
     *
     */
    @ApiOperation("分页获取项目组用户信息列表")
    @GetMapping("/member/list")
    public RestResponse<PageResult<UserMtmGroupDto>> pageMemberList(@Valid UserMtmGroupQueryParam groupQueryParam){
        return RestResponse.ok(userMtmGroupService.memberList(groupQueryParam));
    }

    /**
     *
     * 获取用户创建的项目组信息列表
     *
     * @author LiZhengFan
     * @since 2023-03-30
     *
     */
    @ApiOperation("获取用户创建的项目组信息列表")
    @GetMapping("/created")
    public RestResponse<PageResult<GroupDto>> list(@Valid GroupQueryParam groupQueryParam){
        return RestResponse.ok(groupService.getList(groupQueryParam));
    }

    /**
     *
     * 获取用户加入的的项目组信息列表，不包括自己创建的
     *
     * @author LiZhengFan
     * @since 2023-03-30
     *
     */
    @ApiOperation("获取用户加入的的项目组信息列表，不包括自己创建的")
    @GetMapping("/joined")
    public RestResponse<PageResult<GroupDto>> joined(@Valid GroupQueryParam groupQueryParam){
        return RestResponse.ok(groupService.getJoinedList(groupQueryParam));
    }

    /**
     *
     * 新增项目组信息
     *
     * @author LiZhengFan
     * @since 2023-03-30
     *
     */
    @ApiOperation("新增项目组信息")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid GroupAddParam groupAddParam) {
        return RestResponse.ok(groupService.saveByParam(groupAddParam));
    }

    /**
     *
     * 修改项目组信息
     *
     * @author LiZhengFan
     * @since 2023-03-30
     *
     */
    @ApiOperation(value = "修改项目组信息")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid GroupEditParam groupEditParam) {
        return RestResponse.ok(groupService.updateByParam(groupEditParam));
    }

    /**
     *
     * 根据id删除项目组信息
     *
     * @author LiZhengFan
     * @since 2023-03-30
     *
     */
    @ApiOperation(value = "根据id删除项目组信息")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(groupService.deleteByIds(ids));
    }

    /**
     *
     * 根据id获取项目组信息
     *
     * @author LiZhengFan
     * @since 2023-03-30
     *
     */
    @ApiOperation(value = "根据id获取项目组信息")
    @GetMapping(value = "/{id}")
    public RestResponse<GroupDto> get(@PathVariable String id) {
        return RestResponse.ok(groupService.getDtoById(id));
    }

    /**
     *
     * 导出项目组信息列表
     *
     * @author LiZhengFan
     * @since 2023-03-30
     *
     */
    @ApiOperation(value = "导出项目组信息列表")
    @GetMapping("/export")
    public void excelExport(@Valid GroupQueryParam groupQueryParam, HttpServletResponse response){
        PageResult<GroupDto> pageResult  = groupService.pageDto(groupQueryParam);
        ExcelUtils<GroupDto> util = new ExcelUtils<>(GroupDto.class);

        util.exportExcelAndDownload(pageResult.getRecords(), "项目组信息", response);
    }

    /**
     *
     * Excel导入项目组信息
     *
     * @author LiZhengFan
     * @since 2023-03-30
     *
     */
    @ApiOperation(value = "Excel导入项目组信息")
    @PostMapping("/import")
    public RestResponse<Boolean> excelImport(@RequestParam("file") MultipartFile file){
        ExcelUtils<GroupDto> util = new ExcelUtils<>(GroupDto.class);
        List<GroupDto> rows = util.importExcel(file);
        if (CollectionUtils.isEmpty(rows)) {
            throw new CommonException(CommonErrorEnum.IMPORT_DATA_EMPTY);
        }
        return RestResponse.ok(groupService.saveDtoBatch(rows));
    }

}
