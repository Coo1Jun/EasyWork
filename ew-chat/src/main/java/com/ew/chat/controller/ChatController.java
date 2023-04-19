package com.ew.chat.controller;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import com.ew.chat.message.dto.MessageDto;
import com.ew.chat.message.dto.MessageQueryParam;
import com.ew.chat.message.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author lzf
 * @create 2023/04/19
 * @description 聊天模块控制层
 */
@RestController
@RequestMapping("/chat")
@Api(tags = "聊天模块控制层")
public class ChatController {

    @Autowired
    private IMessageService messageService;

    /**
     *
     * 获取聊天记录列表
     *
     * @author LiZhengFan
     * @since 2023-04-19
     *
     */
    @ApiOperation("分页获取项目组信息列表")
    @GetMapping("/history/list")
    public RestResponse<List<MessageDto>> list(MessageQueryParam messageQueryParam){
        return RestResponse.ok(messageService.historyList(messageQueryParam));
    }
}
