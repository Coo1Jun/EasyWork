package com.ew.chat.address.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ew.chat.address.dto.AddressBookQueryParam;
import com.ew.chat.address.dto.AddressBookAddParam;
import com.ew.chat.address.dto.AddressBookEditParam;
import com.ew.chat.address.dto.AddressBookDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;

import com.ew.chat.address.service.IAddressBookService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 通讯录信息实体控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-21
 */
@RestController
@RequestMapping("/address")
@Api(tags = "通讯录信息实体服务接口")
public class AddressBookController {

    @Autowired
    private IAddressBookService addressBookService;

    /**
     * 分页获取通讯录信息实体列表
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation("分页获取通讯录信息实体列表")
    @GetMapping("/list")
    public RestResponse<PageResult<AddressBookDto>> pageList(@Valid AddressBookQueryParam addressBookQueryParam) {
        return RestResponse.ok(addressBookService.pageDto(addressBookQueryParam));
    }

    /**
     * 新增通讯录信息实体
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation("新增通讯录信息实体")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid AddressBookAddParam addressBookAddParam) {
        return RestResponse.ok(addressBookService.saveByParam(addressBookAddParam));
    }

    /**
     * 修改通讯录信息实体
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation(value = "修改通讯录信息实体")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid AddressBookEditParam addressBookEditParam) {
        return RestResponse.ok(addressBookService.updateByParam(addressBookEditParam));
    }

    /**
     * 根据id删除通讯录信息实体
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation(value = "根据id删除通讯录信息实体")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(addressBookService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 根据id获取通讯录信息实体
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation(value = "根据id获取通讯录信息实体")
    @GetMapping(value = "/{id}")
    public RestResponse<AddressBookDto> get(@PathVariable String id) {
        return RestResponse.ok(addressBookService.getDtoById(id));
    }

    /**
     * 导出通讯录信息实体列表
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation(value = "导出通讯录信息实体列表")
    @GetMapping("/export")
    public void excelExport(@Valid AddressBookQueryParam addressBookQueryParam, HttpServletResponse response) {
        PageResult<AddressBookDto> pageResult = addressBookService.pageDto(addressBookQueryParam);
        ExcelUtils<AddressBookDto> util = new ExcelUtils<>(AddressBookDto.class);

        util.exportExcelAndDownload(pageResult.getRecords(), "通讯录信息实体", response);
    }

    /**
     * Excel导入通讯录信息实体
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation(value = "Excel导入通讯录信息实体")
    @PostMapping("/import")
    public RestResponse<Boolean> excelImport(@RequestParam("file") MultipartFile file) {
        ExcelUtils<AddressBookDto> util = new ExcelUtils<>(AddressBookDto.class);
        List<AddressBookDto> rows = util.importExcel(file);
        if (CollectionUtils.isEmpty(rows)) {
            throw new CommonException(CommonErrorEnum.IMPORT_DATA_EMPTY);
        }
        return RestResponse.ok(addressBookService.saveDtoBatch(rows));
    }

}
