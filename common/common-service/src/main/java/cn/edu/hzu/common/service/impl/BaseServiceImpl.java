package cn.edu.hzu.common.service.impl;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.param.PageParam;
import cn.edu.hzu.common.service.IBaseService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @className BaseServiceImpl
 * @description: 实现类
 * @author lzf
 * @date 2022/3/18
 * @version V1.0
 **/
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {


    @Override
    public PageResult<T> page(PageParam pageParam, Wrapper<T> wrapper) {
        Page<T> result = this.page(new Page<>(pageParam.getPageNo(),pageParam.getLimit()),wrapper);
        if (null != result){
            return PageResult.<T>builder().records(result.getRecords()).total(result.getTotal()).build();
        }
        return null;
    }

    @Override
    public boolean removeByIds(Long[] ids) {
        return removeByIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public boolean removeByIds(String[] ids) {
        return removeByIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public boolean updateByMap(Map<String, Object> map){
        return false;
    }

    @Override
    public Map<String,Object> getMapById(String id){
        return new HashMap<>();
    }

    protected void exportFullQueryWrapper(Wrapper queryWrapper) {

        // queryWrapper.last(" limit 10000");
    }
}
