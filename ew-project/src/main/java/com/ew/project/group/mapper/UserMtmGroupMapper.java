package com.ew.project.group.mapper;

import com.ew.project.group.dto.UserMtmGroupDto;
import com.ew.project.group.dto.UserMtmGroupQueryParam;
import com.ew.project.group.entity.UserMtmGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-03-30
 */
public interface UserMtmGroupMapper extends BaseMapper<UserMtmGroup> {

    /**
     * 获取项目组用户信息列表
     * @param queryParam 查询信息
     * @return
     */
    List<UserMtmGroupDto> getMemberList(@Param("query") UserMtmGroupQueryParam queryParam);

    /**
     * 获取项目组用户信息列表总数
     * @param queryParam 查询信息
     * @return 总数
     */
    Integer memberListCount(@Param("query") UserMtmGroupQueryParam queryParam);
}
