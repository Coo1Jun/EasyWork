package com.ew.project.netdisk.mapper;

import com.ew.project.netdisk.dto.NetDiskFileDto;
import com.ew.project.netdisk.dto.NetDiskFileQueryParam;
import com.ew.project.netdisk.entity.NetDiskFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-12
 */
public interface NetDiskFileMapper extends BaseMapper<NetDiskFile> {

    /**
     * 根据用户id获取项目类型的文件夹列表
     * @param userId
     * @param queryParam
     * @return
     */
    List<NetDiskFileDto> getProNetDir(@Param("user_id") String userId, @Param("query")NetDiskFileQueryParam queryParam);
    Integer getProNetDirCount(@Param("user_id") String userId, @Param("query")NetDiskFileQueryParam queryParam);
}
