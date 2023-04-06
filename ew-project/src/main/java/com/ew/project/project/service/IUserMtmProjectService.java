package com.ew.project.project.service;

import com.ew.project.project.entity.UserMtmProject;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.project.project.dto.UserMtmProjectQueryParam;
import com.ew.project.project.dto.UserMtmProjectAddParam;
import com.ew.project.project.dto.UserMtmProjectEditParam;
import com.ew.project.project.dto.UserMtmProjectDto;
import cn.edu.hzu.common.api.PageResult;
import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-06
 *
 */
public interface IUserMtmProjectService extends IBaseService<UserMtmProject> {

    /**
     *
     * 分页查询，返回Dto
     *
     * @param userMtmProjectQueryParam 查询参数
     * @return UserMtmProjectDto 查询返回列表实体
     * @since 2023-04-06
     *
     */
    PageResult<UserMtmProjectDto> pageDto(UserMtmProjectQueryParam userMtmProjectQueryParam);

    /**
     *
     * 新增
     *
     * @param userMtmProjectAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-06
     *
     */
    boolean saveByParam(UserMtmProjectAddParam userMtmProjectAddParam);

    /**
     *
     * 根据id查询，转dto
     *
     * @param id 项目信息id
     * @return UserMtmProjectDto
     * @since 2023-04-06
     *
     */
    UserMtmProjectDto getDtoById(String id);

    /**
     *
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-06
     *
     */
    boolean saveDtoBatch(List<UserMtmProjectDto> rows);

    /**
     *
     * 更新
     *
     * @param userMtmProjectEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-06
     *
     */
    boolean updateByParam(UserMtmProjectEditParam userMtmProjectEditParam);

    /**
     * 根据用户id获取用户加入的所有项目标识tab
     */
    List<String> getTabById(String userId);
}