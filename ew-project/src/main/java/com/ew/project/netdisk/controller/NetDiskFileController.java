package com.ew.project.netdisk.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ew.project.netdisk.dto.NetDiskFileQueryParam;
import com.ew.project.netdisk.dto.NetDiskFileAddParam;
import com.ew.project.netdisk.dto.NetDiskFileEditParam;
import com.ew.project.netdisk.dto.NetDiskFileDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;

import com.ew.project.netdisk.service.INetDiskFileService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 网盘文件信息控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-12
 */
@RestController
@RequestMapping("/netdisk")
@Api(tags = "网盘文件信息服务接口")
public class NetDiskFileController {

    @Autowired
    private INetDiskFileService netDiskFileService;

    /**
     * 分页获取网盘文件信息列表
     *
     * @author LiZhengFan
     * @since 2023-04-12
     */
    @ApiOperation("分页获取网盘文件信息列表")
    @GetMapping("/list")
    public RestResponse<PageResult<NetDiskFileDto>> pageList(@Valid NetDiskFileQueryParam netDiskFileQueryParam) {
        return RestResponse.ok(netDiskFileService.pageDto(netDiskFileQueryParam));
    }

    /**
     * 新增网盘文件信息
     *
     * @author LiZhengFan
     * @since 2023-04-12
     */
    @ApiOperation("新增网盘文件信息")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid NetDiskFileAddParam netDiskFileAddParam) {
        return RestResponse.ok(netDiskFileService.saveByParam(netDiskFileAddParam));
    }

    /**
     * 新增文件夹
     *
     * @author LiZhengFan
     * @since 2023-04-12
     */
    @ApiOperation("新增文件夹")
    @PostMapping("/directory")
    public RestResponse<Boolean> addDirectory(@RequestBody @Valid NetDiskFileAddParam netDiskFileAddParam) {
        return RestResponse.ok(netDiskFileService.addDir(netDiskFileAddParam, false));
    }

    /**
     * 上传文件
     *
     * @author LiZhengFan
     * @since 2023-04-12
     */
    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public RestResponse<Boolean> uploadFile(@RequestBody @Valid NetDiskFileAddParam netDiskFileAddParam) {
        return RestResponse.ok(netDiskFileService.uploadFile(netDiskFileAddParam, true));
    }

    /**
     * 修改网盘文件信息
     *
     * @author LiZhengFan
     * @since 2023-04-12
     */
    @ApiOperation(value = "修改网盘文件信息")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid NetDiskFileEditParam netDiskFileEditParam) {
        return RestResponse.ok(netDiskFileService.updateByParam(netDiskFileEditParam));
    }

    /**
     * 根据id删除网盘文件信息
     *
     * @author LiZhengFan
     * @since 2023-04-12
     */
    @ApiOperation(value = "根据id删除网盘文件信息")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(netDiskFileService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 根据id获取网盘文件信息
     *
     * @author LiZhengFan
     * @since 2023-04-12
     */
    @ApiOperation(value = "根据id获取网盘文件信息")
    @GetMapping(value = "/{id}")
    public RestResponse<NetDiskFileDto> get(@PathVariable String id) {
        return RestResponse.ok(netDiskFileService.getDtoById(id));
    }

}
