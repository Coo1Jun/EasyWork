package cn.edu.hzu.office.dtotolist;

import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lzf
 * @createTime 2022/09/03
 * @description
 */
public class DTOToList {
    private static boolean autoCastDateFormat = true;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void setAutoCastDateFormat(boolean autoCastDateFormat) {
        DTOToList.autoCastDateFormat = autoCastDateFormat;
    }

    public static void setDateFormat(String format) {
        DTOToList.sdf = new SimpleDateFormat(format);
    }

    /**
     * 获取对象object的指定属性，保存在数组返回
     *
     * @param object     目标对象
     * @param filedNames 指定属性
     * @return 存取若干个属性值的数组
     */
    public static String[] toArray(Object object, String... filedNames) {
        Field[] declaredFields = object.getClass().getDeclaredFields();
        List<String> valueList = new ArrayList<>();
        if (filedNames != null && filedNames.length != 0) {
            for (String filedName : filedNames) {
                addFiledToList(filedName, object, valueList);
            }
        } else {
            for (Field declaredField : declaredFields) {
                String filedName = declaredField.getName();
                addFiledToList(filedName, object, valueList);
            }
        }
        return valueList.toArray(new String[0]);
    }

    /**
     * @param filedName 元素属性名称
     * @param object    目标对象
     * @param list      获取object对象的filedName属性，将其添加到list
     */
    private static void addFiledToList(String filedName, Object object, List<String> list) {
        Method method = null;
        if (filedName == "null" || filedName == null) {
            list.add("");
        } else {
            String getFunctionName = "get" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
            try {
                method = object.getClass().getMethod(getFunctionName);
                Object value = method.invoke(object);
                if (value == null) {
                    list.add(StrUtil.EMPTY);
                } else {
                    if (autoCastDateFormat && value instanceof Date) {
                        list.add(sdf.format(value));
                    } else {
                        list.add(value.toString());
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
    }
}