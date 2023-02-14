package cn.edu.hzu.common.service;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.Map;

/**
 * service父接口
 *
 * @author lzf
 * @param <T>
 */
public interface IBaseService<T> extends IService<T> {

    /**
     * 分页查询，直接返回PageResult
     *
     * @param pageParam
     * @param wrapper
     * @return
     */
    PageResult<T> page(PageParam pageParam, Wrapper<T> wrapper);

    /**
     * 根据id数组删除
     * @param id
     * @return
     */
    boolean removeByIds(Long[] id);

    /**
     * 根据id数组删除
     * @param id
     * @return
     */
    boolean removeByIds(String[] id);

    /**
     *
     * 更新业务表单，入参Map
     *
     * @param map 业务参数
     * @return boolean 操作状态 true - 成功 false -失败
     * @created:  2022-08-09 15:29
     * @throws IOException
     *
     */
    boolean updateByMap(Map<String,Object> map);

    /**
     *
     * 通过id获取业务数据Map
     *
     * @param id 业务id
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @create 2022-08-09 15:29
     *
     */
    Map<String,Object> getMapById(String id);

}
