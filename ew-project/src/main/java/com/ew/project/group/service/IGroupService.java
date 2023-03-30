package com.ew.project.group.service;

import com.ew.project.group.entity.Group;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.project.group.dto.GroupQueryParam;
import com.ew.project.group.dto.GroupAddParam;
import com.ew.project.group.dto.GroupEditParam;
import com.ew.project.group.dto.GroupDto;
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
public interface IGroupService extends IBaseService<Group> {

    /**
     *
     * 分页查询，返回Dto
     *
     * @param groupQueryParam 查询参数
     * @return GroupDto 查询返回列表实体
     * @since 2023-03-30
     *
     */
    PageResult<GroupDto> pageDto(GroupQueryParam groupQueryParam);

    /**
     *
     * 查询用户自己创建的所有，返回Dto
     *
     * @param groupQueryParam 查询参数
     * @return GroupDto 查询返回列表实体
     * @since 2023-03-30
     *
     */
    PageResult<GroupDto> getList(GroupQueryParam groupQueryParam);

    /**
     *
     * 查询用户自己加入的所有，不包括自己创建的，返回Dto
     *
     * @param groupQueryParam 查询参数
     * @return GroupDto 查询返回列表实体
     * @since 2023-03-30
     *
     */
    PageResult<GroupDto> getJoinedList(GroupQueryParam groupQueryParam);

    /**
     *
     * 新增
     *
     * @param groupAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-03-30
     *
     */
    boolean saveByParam(GroupAddParam groupAddParam);

    /**
     *
     * 根据id查询，转dto
     *
     * @param id 项目组信息id
     * @return GroupDto
     * @since 2023-03-30
     *
     */
    GroupDto getDtoById(String id);

    /**
     *
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-03-30
     *
     */
    boolean saveDtoBatch(List<GroupDto> rows);

    /**
     *
     * 更新
     *
     * @param groupEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-03-30
     *
     */
    boolean updateByParam(GroupEditParam groupEditParam);

    /**
     *
     * 删除
     *
     * @param ids 根据id删除
     * @return true - 操作成功 false -操作失败
     * @since 2023-03-30
     *
     */
    boolean deleteByIds(String[] ids);
}