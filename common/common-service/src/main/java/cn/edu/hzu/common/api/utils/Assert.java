package cn.edu.hzu.common.api.utils;

import cn.edu.hzu.common.api.CustomResultCode;
import cn.edu.hzu.common.exception.CommonException;
import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author lzf
 * @createTime 2022/09/03
 * @description 断言
 */
public class Assert {

    private Assert() {
    }

    public static void gtZero(CustomResultCode errorCode, Integer num) {
        if (num == null || num <= 0) {
            fail(errorCode);
        }

    }

    public static void geZero(CustomResultCode errorCode, Integer num) {
        if (num == null || num < 0) {
            fail(errorCode);
        }
    }

    public static void gt(CustomResultCode errorCode, Integer num1, Integer num2) {
        if (num1 <= num2) {
            fail(errorCode);
        }
    }

    public static void ge(CustomResultCode errorCode, Integer num1, Integer num2) {
        if (num1 < num2) {
            fail(errorCode);
        }
    }

    public static void eq(CustomResultCode errorCode, Object obj1, Object obj2) {
        if (!obj1.equals(obj2)) {
            fail(errorCode);
        }
    }

    public static void isTrue(CustomResultCode errorCode, boolean condition) {
        if (!condition) {
            fail(errorCode);
        }
    }

    public static void isFalse(CustomResultCode errorCode, boolean condition) {
        if (condition) {
            fail(errorCode);
        }
    }

    public static void isNull(CustomResultCode errorCode, Object... conditions) {
        if (ObjectUtils.allNotNull(conditions)) {
            fail(errorCode);
        }
    }

    public static void notNull(CustomResultCode errorCode, Object... conditions) {
        if (!ObjectUtils.allNotNull(conditions)) {
            fail(errorCode);
        }
    }

    public static void fail(CustomResultCode errorCode) {
        throw new CommonException(errorCode);
    }

    public static void fail(CustomResultCode errorCode, boolean condition) {
        if (condition) {
            fail(errorCode);
        }
    }

    public static void notEmpty(CustomResultCode errorCode, Object[] array) {
        if (ObjectUtil.hasEmpty(array)) {
            fail(errorCode);
        }
    }

    public static void noNullElements(CustomResultCode errorCode, Object[] array) {
        if (array != null && array.length > 0) {
            boolean flag = Stream.of(array).filter(Objects::isNull).count() > 0;
            if (flag) {
                fail(errorCode);
            }
        }
    }

    public static void notEmpty(CustomResultCode errorCode, Collection<?> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            fail(errorCode);
        }

    }

    public static void notEmpty(CustomResultCode errorCode, Map<?, ?> map) {
        if (MapUtils.isEmpty(map)) {
            fail(errorCode);
        }

    }

    public static void isInstanceOf(CustomResultCode errorCode, Class<?> type, Object obj) {
        notNull(errorCode, type);
        if (!type.isInstance(obj)) {
            fail(errorCode);
        }
    }

    public static void isAssignable(CustomResultCode errorCode, Class<?> superType, Class<?> subType) {
        notNull(errorCode, superType);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            fail(errorCode);
        }
    }
}
