package io.tarzan.common.domain.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import io.choerodon.mybatis.service.BaseServiceImpl;

public class MtFieldsHelper {
    private MtFieldsHelper() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    /**
     * 获取属性值，若为空返回默认值
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static <T> T getOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * 判断两个对象是否相似
     *
     * @param o1
     * @param o2
     * @return
     */
    public static <T> boolean isSame(T o1, T o2) {
        if (o1 == null || o2 == null) {
            return false;
        } else {
            return o1.equals(o2);
        }
    }

    public static boolean isAllFieldNull(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fs = clazz.getDeclaredFields();
        boolean flag = true;
        Field[] var4 = fs;
        int var5 = fs.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Field f = var4[var6];
            if (!Modifier.isStatic(f.getModifiers())) {
                ReflectionUtils.makeAccessible(f);
                Object val = null;

                try {
                    val = f.get(obj);
                } catch (Exception var10) {
                    logger.debug(var10.getMessage());
                }

                if (val != null) {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }

    /**
     * 判断该对象指定字段是否为null，如果是返回false，如果不是返回true,如果字段不存在，也返回false
     *
     * @param obj
     * @return boolean
     */
    public static boolean isSpecifyFieldNotEmpty(Object obj, List<String> fieldName) {
        Class<?> clazz = obj.getClass();
        List<Field> fields = new ArrayList<>();
        for (String ever : fieldName) {
            try {
                Field one = clazz.getDeclaredField(ever);
                fields.add(one);
            } catch (NoSuchFieldException e) {
                logger.debug(e.getMessage());
            }
        }
        boolean flag = true;
        if (CollectionUtils.isNotEmpty(fields)) {
            for (Field field : fields) {
                ReflectionUtils.makeAccessible(field);
                // 得到此属性的值
                Object val = null;

                try {
                    val = field.get(obj);
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                }

                if (field.getType().isAssignableFrom(java.lang.String.class)) {
                    if (val == null || StringUtils.isEmpty(String.valueOf(val))) {
                        flag = false;
                    }
                } else if (field.getType().isAssignableFrom(java.util.List.class)) {
                    if (CollectionUtils.isEmpty((List<?>) val)) {
                        flag = false;
                    }
                } else if (field.getType().isAssignableFrom(java.util.Map.class)) {
                    if (MapUtils.isEmpty((Map<?, ?>) val)) {
                        flag = false;
                    }
                } else {
                    // 包括非主键Long,java.lang.Double,java.lang.Float,java.lang.Integer,java.util.Date
                    if (val == null) {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }
}
