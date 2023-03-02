package com.ew.server.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ew.server.file.entity.FileMeta;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-03-01
 */
public interface FileMetaMapper extends BaseMapper<FileMeta> {
    List<String> getFileNameByIds(@Param("ids") List<String> ids);
}
