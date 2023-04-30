package com.ew.communication.calendar.controller;

import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.client.server.service.IServerClientService;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.api.utils.UserUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.communication.address.dto.AddressBookDto;
import com.ew.communication.calendar.dto.CalendarDto;
import com.ew.communication.calendar.dto.CalendarParamMapper;
import com.ew.communication.calendar.schedule.entity.Schedule;
import com.ew.communication.calendar.schedule.entity.ScheduleOtmUser;
import com.ew.communication.calendar.schedule.service.IScheduleOtmUserService;
import com.ew.communication.calendar.schedule.service.IScheduleService;
import com.ew.communication.calendar.todolist.entity.TodoList;
import com.ew.communication.calendar.todolist.service.ITodoListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzf
 * @create 2023/04/30
 * @description 日历模块控制器
 */
@RestController
@RequestMapping("/calendar")
@Api(tags = "日历实体信息服务接口")
public class CalendarController {

    @Autowired
    private IScheduleService scheduleService;

    @Autowired
    private IScheduleOtmUserService scheduleOtmUserService;

    @Autowired
    private ITodoListService todoListService;

    @Autowired
    private CalendarParamMapper calendarParamMapper;

    @Autowired
    private IServerClientService serverClientService;

    private static final String SCHEDULE_ID_SQL = "SELECT schedule_id FROM t_schedule_otm_user WHERE user_id = {}";

    @ApiOperation("获取日历实体信息列表")
    @GetMapping("/list")
    public RestResponse<List<CalendarDto>> getCalendarList() {
        String curUserId = UserUtils.getCurrentUser().getUserid();
        List<CalendarDto> result = new ArrayList<>();
        // 查询日程列表
        List<CalendarDto> scheduleDtos = calendarParamMapper.scheduleList2CalendarList(
                scheduleService.list(Wrappers.<Schedule>lambdaQuery()
                        .inSql(Schedule::getId, StringUtils.format(SCHEDULE_ID_SQL, curUserId))));
        if (CollectionUtils.isNotEmpty(scheduleDtos)) {
            for (CalendarDto dto : scheduleDtos) {
                // 查询日程对应的参与人
                List<ScheduleOtmUser> users = scheduleOtmUserService.list(
                        Wrappers.<ScheduleOtmUser>lambdaQuery()
                                .eq(ScheduleOtmUser::getScheduleId, dto.getId()));
                List<AddressBookDto> userList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(users)) {
                    for (ScheduleOtmUser user : users) {
                        // 查询参与人详细信息
                        UserDto userInfo = serverClientService.getUserDtoById(user.getUserId());
                        if (userInfo != null) {
                            AddressBookDto addressBookDto = new AddressBookDto();
                            addressBookDto.setId(userInfo.getId());
                            addressBookDto.setName(userInfo.getRealName());
                            addressBookDto.setAvatar(userInfo.getPortrait());
                            addressBookDto.setEmail(userInfo.getEmail());
                            addressBookDto.setDescription(userInfo.getDescription());
                            userList.add(addressBookDto);
                        }
                    }
                }
                dto.setParticipants(userList);
            }
            result.addAll(scheduleDtos);
        }
        // 查询待办列表
        List<CalendarDto> todoListDtos = calendarParamMapper.todoList2CalendarList(
                todoListService.list(Wrappers.<TodoList>lambdaQuery()
                        .eq(TodoList::getCreateId, curUserId)));
        if (CollectionUtils.isNotEmpty(todoListDtos)) {
            result.addAll(todoListDtos);
        }
        return RestResponse.ok(result);
    }
}
