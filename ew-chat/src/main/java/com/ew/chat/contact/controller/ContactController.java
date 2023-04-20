package com.ew.chat.contact.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ew.chat.contact.dto.ContactQueryParam;
import com.ew.chat.contact.dto.ContactAddParam;
import com.ew.chat.contact.dto.ContactEditParam;
import com.ew.chat.contact.dto.ContactDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;

import com.ew.chat.contact.service.IContactService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 联系人实体控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-20
 */
@RestController
@RequestMapping("/contact")
@Api(tags = "联系人实体服务接口")
public class ContactController {

    @Autowired
    private IContactService contactService;

    /**
     * 获取联系人实体列表
     *
     * @author LiZhengFan
     * @since 2023-04-20
     */
    @ApiOperation("获取联系人实体列表")
    @GetMapping("/list")
    public RestResponse<List<ContactDto>> list(@Valid ContactQueryParam contactQueryParam) {
        return RestResponse.ok(contactService.listDto(contactQueryParam));
    }

    /**
     * 新增联系人实体
     *
     * @author LiZhengFan
     * @since 2023-04-20
     */
    @ApiOperation("新增联系人实体")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid ContactAddParam contactAddParam) {
        return RestResponse.ok(contactService.saveByParam(contactAddParam));
    }

    /**
     * 修改联系人实体
     *
     * @author LiZhengFan
     * @since 2023-04-20
     */
    @ApiOperation(value = "修改联系人实体")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid ContactEditParam contactEditParam) {
        return RestResponse.ok(contactService.updateByParam(contactEditParam));
    }

    /**
     * 根据id删除联系人实体
     *
     * @author LiZhengFan
     * @since 2023-04-20
     */
    @ApiOperation(value = "根据id删除联系人实体")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(contactService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 根据id获取联系人实体
     *
     * @author LiZhengFan
     * @since 2023-04-20
     */
    @ApiOperation(value = "根据id获取联系人实体")
    @GetMapping(value = "/{id}")
    public RestResponse<ContactDto> get(@PathVariable String id) {
        return RestResponse.ok(contactService.getDtoById(id));
    }

    /**
     * 导出联系人实体列表
     *
     * @author LiZhengFan
     * @since 2023-04-20
     */
    @ApiOperation(value = "导出联系人实体列表")
    @GetMapping("/export")
    public void excelExport(@Valid ContactQueryParam contactQueryParam, HttpServletResponse response) {
        PageResult<ContactDto> pageResult = contactService.pageDto(contactQueryParam);
        ExcelUtils<ContactDto> util = new ExcelUtils<>(ContactDto.class);

        util.exportExcelAndDownload(pageResult.getRecords(), "联系人实体", response);
    }

    /**
     * Excel导入联系人实体
     *
     * @author LiZhengFan
     * @since 2023-04-20
     */
    @ApiOperation(value = "Excel导入联系人实体")
    @PostMapping("/import")
    public RestResponse<Boolean> excelImport(@RequestParam("file") MultipartFile file) {
        ExcelUtils<ContactDto> util = new ExcelUtils<>(ContactDto.class);
        List<ContactDto> rows = util.importExcel(file);
        if (CollectionUtils.isEmpty(rows)) {
            throw new CommonException(CommonErrorEnum.IMPORT_DATA_EMPTY);
        }
        return RestResponse.ok(contactService.saveDtoBatch(rows));
    }

}
