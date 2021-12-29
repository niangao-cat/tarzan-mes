package io.tarzan.common.domain.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectFieldsHelper {
    /**
     * 判断该对象是否: 返回ture表示所有属性为null 返回false表示不是所有属性都是null
     *
     * @param obj
     * @return boolean
     */
    public static boolean isAllFieldNull(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fs = clazz.getDeclaredFields();
        boolean flag = true;
        for (Field f : fs) {
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            // 设置属性是可以访问的(私有的也可以)
            f.setAccessible(true);
            // 得到此属性的值
            Object val = null;

            try {
                val = f.get(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ("java.lang.String".equals(f.getType().getName())) {
                if (val != null && StringUtils.isNotEmpty(String.valueOf(val))) {
                    flag = false;
                    break;
                }
            } else if ("java.lang.Long".equals(f.getType().getName())
                            || "java.lang.Double".equals(f.getType().getName())
                            || "java.lang.Float".equals(f.getType().getName())
                            || "java.lang.Integer".equals(f.getType().getName())
                            || "java.util.Date".equals(f.getType().getName())) {
                if (val != null) {
                    flag = false;
                    break;
                }
            } else if ("java.util.List".equals(f.getType().getName())
                            || "java.util.Map".equals(f.getType().getName())) {
                if (CollectionUtils.isNotEmpty((List<?>) val)) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    public static Map<String, String> getAttributeFields(Object object, String attributePrefix) {
        List<Field> fields = Arrays.stream(object.getClass().getDeclaredFields())
                .filter(f -> f.getName().startsWith(attributePrefix)).collect(Collectors.toList());
        Map<String, String> fieldMap = new HashMap<>(fields.size());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(object) instanceof String && !"attributeCategory".equals(field.getName())) {
                    String value = String.valueOf(field.get(object));
                    if (StringUtils.isNotEmpty(value)) {
                        fieldMap.put(field.getName(), value);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
        return fieldMap;
    }

    /**
     * 将传入对象中，String类型的字段，为null值的更新为空字符串
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Object setStringFieldsEmpty(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fs = clazz.getDeclaredFields();
        for (Field f : fs) {
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            // 设置属性是可以访问的(私有的也可以)
            f.setAccessible(true);
            if ("java.lang.String".equals(f.getType().getName())) {
                // 得到此属性的值
                Object val = null;
                try {
                    val = f.get(obj);
                    if (val == null) {
                        f.set(obj, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    public static <T> T getOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
