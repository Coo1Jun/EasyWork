package com.ew.communication.groupchat.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ew.communication.groupchat.dto.GroupChatMemberQueryParam;
import com.ew.communication.groupchat.dto.GroupChatMemberAddParam;
import com.ew.communication.groupchat.dto.GroupChatMemberEditParam;
import com.ew.communication.groupchat.dto.GroupChatMemberDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;

import com.ew.communication.groupchat.service.IGroupChatMemberService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 群聊成员信息实体控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-21
 */
@RestController
@RequestMapping("/group/chat/member")
@Api(tags = "群聊成员信息实体服务接口")
public class GroupChatMemberController {

    @Autowired
    private IGroupChatMemberService groupChatMemberService;

    /**
     * 分页获取群聊成员信息实体列表
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation("分页获取群聊成员信息实体列表")
    @GetMapping("/list")
    public RestResponse<PageResult<GroupChatMemberDto>> pageList(@Valid GroupChatMemberQueryParam groupChatMemberQueryParam) {
        return RestResponse.ok(groupChatMemberService.pageDto(groupChatMemberQueryParam));
    }

    /**
     * 新增群聊成员信息实体
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation("新增群聊成员信息实体")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid GroupChatMemberAddParam groupChatMemberAddParam) {
        return RestResponse.ok(groupChatMemberService.saveByParam(groupChatMemberAddParam));
    }

    /**
     * 修改群聊成员信息实体
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation(value = "修改群聊成员信息实体")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid GroupChatMemberEditParam groupChatMemberEditParam) {
        return RestResponse.ok(groupChatMemberService.updateByParam(groupChatMemberEditParam));
    }

    /**
     * 根据id删除群聊成员信息实体
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation(value = "根据id删除群聊成员信息实体")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(groupChatMemberService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 根据id获取群聊成员信息实体
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation(value = "根据id获取群聊成员信息实体")
    @GetMapping(value = "/{id}")
    public RestResponse<GroupChatMemberDto> get(@PathVariable String id) {
        return RestResponse.ok(groupChatMemberService.getDtoById(id));
    }

    /**
     * 导出群聊成员信息实体列表
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation(value = "导出群聊成员信息实体列表")
    @GetMapping("/export")
    public void excelExport(@Valid GroupChatMemberQueryParam groupChatMemberQueryParam, HttpServletResponse response) {
        PageResult<GroupChatMemberDto> pageResult = groupChatMemberService.pageDto(groupChatMemberQueryParam);
        ExcelUtils<GroupChatMemberDto> util = new ExcelUtils<>(GroupChatMemberDto.class);

        util.exportExcelAndDownload(pageResult.getRecords(), "群聊成员信息实体", response);
    }

    /**
     * Excel导入群聊成员信息实体
     *
     * @author LiZhengFan
     * @since 2023-04-21
     */
    @ApiOperation(value = "Excel导入群聊成员信息实体")
    @PostMapping("/import")
    public RestResponse<Boolean> excelImport(@RequestParam("file") MultipartFile file) {
        ExcelUtils<GroupChatMemberDto> util = new ExcelUtils<>(GroupChatMemberDto.class);
        List<GroupChatMemberDto> rows = util.importExcel(file);
        if (CollectionUtils.isEmpty(rows)) {
            throw new CommonException(CommonErrorEnum.IMPORT_DATA_EMPTY);
        }
        return RestResponse.ok(groupChatMemberService.saveDtoBatch(rows));
    }

}
