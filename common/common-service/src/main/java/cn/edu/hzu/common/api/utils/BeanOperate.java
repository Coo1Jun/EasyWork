package cn.edu.hzu.common.api.utils;

import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lzf
 * @createTime 2022/08/08
 */
public class BeanOperate {

    public static <T> T entityTransform(Object source, Class<T> targetClass){
        if (targetClass == null){
//            throw new NullPointerException();
            // TODO
        }
        if (source == null || targetClass == null){
            return null;
        }
        try {
            T target = targetClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, target);
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> entityListTransform(List<Object> source, Class<T> targetClass){
        return null;
    }

    /**
     * 实体类对象转化成bean，key为属性的名称
     * @param object
     * @return
     */
    public static Map beanToMap(Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (object != null) {
            BeanMap beanMap = BeanMap.create(object);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }
}
