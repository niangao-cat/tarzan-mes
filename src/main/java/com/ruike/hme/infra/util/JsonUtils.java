package com.ruike.hme.infra.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.hzero.core.base.BaseConstants;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通用json转换工具
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com
 * @date 2020/6/30 4:33 下午
 */
@Slf4j
public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);

        //取消默认转换timesstamps(时间戳)形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        //忽略空bean转json错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        //忽略在json字符串中存在，在java类中不存在字段，防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //所有日期都统一为以下样式：yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(BaseConstants.Pattern.DATETIME));
    }

    public static <T> String objToJson(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("obj To json is error", e);
            return null;
        }
    }

    /**
     * 返回格式化好的json串
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String objToJsonPretty(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("obj To json pretty is error", e);
            return null;
        }
    }

    public static <T> T jsonToObject(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T) json : objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.warn("json To obj is error", e);
            return null;
        }
    }

    /**
     * 通过jackson 的javatype 来处理多泛型的转换
     *
     * @param json
     * @param collectionClazz
     * @param elements
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String json, Class<?> collectionClazz, Class<?>... elements) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClazz, elements);

        try {
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            log.warn("json To obj is error", e);
            return null;
        }
    }

    public static <T> Map<String, String> toStringMap(T obj) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        Map<String, String> fieldsMap = new HashMap<>(declaredFields.length);
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                String str;
                if (value == null) {
                    str = "";
                } else if (value instanceof Integer || value instanceof Short || value instanceof Byte || value instanceof Long
                        || value instanceof Double || value instanceof Float || value instanceof Boolean || value instanceof String
                        || value instanceof Character) {
                    str = value.toString();
                } else if (value instanceof BigDecimal) {
                    str = ((BigDecimal) value).toPlainString();
                } else if (value instanceof Date) {
                    str = DateUtil.formatDateTime((Date) value);
                } else {
                    str = "";
                }
                fieldsMap.put(field.getName(), str);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fieldsMap;
    }

    public static <T> Map<String, Object> objToMap(T obj) {
        Map<String, Object> fieldsMap;
        try {
            fieldsMap = objectMapper.readValue(objectMapper.writeValueAsString(obj), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return fieldsMap;
    }

    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        T obj = null;
        try {
            obj = objectMapper.readValue(objectMapper.writeValueAsString(map), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 时间UTC
     *
     * @param obj
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/9/8 21:38
     */
    public static <T> Map<String, String> toStringMap2(T obj) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        Map<String, String> fieldsMap = new HashMap<>(declaredFields.length);
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                String str;
                if (value == null) {
                    str = "";
                } else if (value instanceof Integer || value instanceof Short || value instanceof Byte || value instanceof Long
                        || value instanceof Double || value instanceof Float || value instanceof Boolean || value instanceof String
                        || value instanceof Character) {
                    str = value.toString();
                } else if (value instanceof BigDecimal) {
                    str = ((BigDecimal) value).toPlainString();
                } else if (value instanceof Date) {
                    str = DatePattern.UTC_FORMAT.format((Date) value);
                } else {
                    str = "";
                }
                fieldsMap.put(field.getName(), str);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fieldsMap;
    }

    /**
     * 时间格式转化
     * @param attrList
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, String>> toStringMap3(List<T> attrList, Class<T> clazz) {
        List<Map<String, String>> resultList = new ArrayList<>();
        for (T obj : attrList) {
            Field[] declaredFields = clazz.getDeclaredFields();
            Map<String, String> fieldsMap = new HashMap<>(declaredFields.length);
            for (Field field : declaredFields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    String str;
                    if (value == null) {
                        str = "";
                    } else if (value instanceof Integer || value instanceof Short || value instanceof Byte || value instanceof Long
                            || value instanceof Double || value instanceof Float || value instanceof Boolean || value instanceof String
                            || value instanceof Character) {
                        str = value.toString();
                    } else if (value instanceof BigDecimal) {
                        str = ((BigDecimal) value).toPlainString();
                    } else if (value instanceof Date) {
                        str = DatePattern.UTC_FORMAT.format((Date) value);
                    } else {
                        str = "";
                    }
                    fieldsMap.put(field.getName(), str);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            resultList.add(fieldsMap);
        }
        return resultList;
    }
}
