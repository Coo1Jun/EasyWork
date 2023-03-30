package com.ew.project.group.service;

import com.ew.project.group.entity.UserMtmGroup;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.project.group.dto.UserMtmGroupQueryParam;
import com.ew.project.group.dto.UserMtmGroupAddParam;
import com.ew.project.group.dto.UserMtmGroupEditParam;
import com.ew.project.group.dto.UserMtmGroupDto;
import cn.edu.hzu.common.api.PageResult;
import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-03-30
 *
 */
public interface IUserMtmGroupService extends IBaseService<UserMtmGroup> {

    /**
     *
     * 分页查询，返回Dto
     *
     * @param userMtmGroupQueryParam 查询参数
     * @return UserMtmGroupDto 查询返回列表实体
     * @since 2023-03-30
     *
     */
    PageResult<UserMtmGroupDto> pageDto(UserMtmGroupQueryParam userMtmGroupQueryParam);

    /**
     *
     * 新增
     *
     * @param userMtmGroupAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-03-30
     *
     */
    boolean saveByParam(UserMtmGroupAddParam userMtmGroupAddParam);

    /**
     *
     * 根据id查询，转dto
     *
     * @param id 用户与项目组对照信息id
     * @return UserMtmGroupDto
     * @since 2023-03-30
     *
     */
    UserMtmGroupDto getDtoById(String id);

    /**
     *
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-03-30
     *
     */
    boolean saveDtoBatch(List<UserMtmGroupDto> rows);

    /**
     *
     * 更新
     *
     * @param userMtmGroupEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-03-30
     *
     */
    boolean updateByParam(UserMtmGroupEditParam userMtmGroupEditParam);
}