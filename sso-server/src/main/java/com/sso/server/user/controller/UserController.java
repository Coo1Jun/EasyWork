package com.sso.server.user.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.sso.server.user.dto.UserQueryParam;
import com.sso.server.user.dto.UserAddParam;
import com.sso.server.user.dto.UserEditParam;
import com.sso.server.user.dto.UserDto;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import com.sso.server.user.service.IUserService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 用户信息控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-02-13
 *
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户信息服务接口")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     *
     * 分页获取用户信息列表
     *
     * @author LiZhengFan
     * @since 2023-02-13
     *
     */
    @ApiOperation("分页获取用户信息列表")
    @GetMapping("/list")
    public RestResponse<PageResult<UserDto>> pageList(@Valid UserQueryParam userQueryParam){
        return RestResponse.ok(userService.pageDto(userQueryParam));
    }

    /**
     *
     * 新增用户信息
     *
     * @author LiZhengFan
     * @since 2023-02-13
     *
     */
    @ApiOperation("新增用户信息")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid UserAddParam userAddParam) {
        return RestResponse.ok(userService.saveByParam(userAddParam));
    }

    /**
     *
     * 修改用户信息
     *
     * @author LiZhengFan
     * @since 2023-02-13
     *
     */
    @ApiOperation(value = "修改用户信息")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid UserEditParam userEditParam) {
        return RestResponse.ok(userService.updateByParam(userEditParam));
    }

    /**
     *
     * 根据id删除用户信息
     *
     * @author LiZhengFan
     * @since 2023-02-13
     *
     */
    @ApiOperation(value = "根据id删除用户信息")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(userService.removeByIds(Arrays.asList(ids)));
    }

    /**
     *
     * 根据id获取用户信息
     *
     * @author LiZhengFan
     * @since 2023-02-13
     *
     */
    @ApiOperation(value = "根据id获取用户信息")
    @GetMapping(value = "/{id}")
    public RestResponse<UserDto> get(@PathVariable String id) {
        return RestResponse.ok(userService.getDtoById(id));
    }

    /**
     *
     * 导出用户信息列表
     *
     * @author LiZhengFan
     * @since 2023-02-13
     *
     */
    @ApiOperation(value = "导出用户信息列表")
    @GetMapping("/export")
    public void excelExport(@Valid UserQueryParam userQueryParam, HttpServletResponse response){
        PageResult<UserDto> pageResult  = userService.pageDto(userQueryParam);
        ExcelUtils<UserDto> util = new ExcelUtils<>(UserDto.class);

        util.exportExcelAndDownload(pageResult.getRecords(), "用户信息", response);
    }

    /**
     *
     * Excel导入用户信息
     *
     * @author LiZhengFan
     * @since 2023-02-13
     *
     */
    @ApiOperation(value = "Excel导入用户信息")
    @PostMapping("/import")
    public RestResponse<Boolean> excelImport(@RequestParam("file") MultipartFile file){
        ExcelUtils<UserDto> util = new ExcelUtils<>(UserDto.class);
        List<UserDto> rows = util.importExcel(file);
        if (CollectionUtils.isEmpty(rows)) {
            throw new CommonException(CommonErrorEnum.IMPORT_DATA_EMPTY);
        }
        return RestResponse.ok(userService.saveDtoBatch(rows));
    }

}
