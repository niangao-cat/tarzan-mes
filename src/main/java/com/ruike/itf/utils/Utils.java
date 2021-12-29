package com.ruike.itf.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.choerodon.core.exception.CommonException;
import org.apache.logging.log4j.util.Strings;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 接口公用工具
 */
public class Utils {

    /**
     * <strong>Title : getDeclaredFields</strong><br/>
     * <strong>Description : 检验传入字段是否为空 </strong><br/>
     * <strong>Create on : 2020/12/17 下午1:36</strong><br/>
     *
     * @param t
     * @param fields
     * @return java.util.List<java.lang.String>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    public static <T> List<String> objectFieldIsNull(T t, String[] fields) {
        List<String> returnField = new ArrayList<>();
        try {
            for (Field field : t.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                for (String f : fields) {
                    if (f.equals(fieldName)) {
                        String string = Objects.isNull(field.get(t)) ? "" : field.get(t).toString();
                        if (Strings.isEmpty(string)) {
                            returnField.add(f);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();

        }
        return returnField;
    }

    /**
     * 数据交换,返回List<MAP>
     *
     * @param dataExchange Key 为来源 value为目标
     * @param objects      数据
     * @param type         Z Key 为来源 value为目标,N value为目标 Key为来源
     * @return
     */
    public static <T> List<Map<String, Object>> dataExchange(Hashtable<String, String> dataExchange, List<T> objects, String type) {
        List<Map<String, Object>> newData = new ArrayList<>();
        for (T t : objects) {
            newData.add(dataExchange(dataExchange, t, type));
        }
        return newData;
    }

    /**
     * 数据交换,返回MAP
     *
     * @param dataExchange Key 为来源 value为目标
     * @param t            数据
     * @param type         Z Key 为来源 value为目标,N value为目标 Key为来源
     * @return
     */
    public static <T> Map<String, Object> dataExchange(Hashtable<String, String> dataExchange, T t, String type) {
        Map<String, Object> sourceData = parseMap(t);
        Map<String, Object> targetData = new HashMap<>(dataExchange.size(), 1);
        if ("Z".equals(type)) {
            dataExchange.forEach((k, v) -> {
                targetData.put(v, sourceData.get(k));
            });
        } else if ("N".equals(type)) {
            dataExchange.forEach((k, v) -> {
                targetData.put(k, sourceData.get(v));
            });
        } else {
            throw new CommonException("type必须为Z或N【Z Key 为来源 value为目标,N value为目标 Key为来源】");
        }
        return targetData;
    }

    /**
     * object 转MAP
     *
     * @param t
     * @param <T>
     * @return
     */
    private static <T> Map<String, Object> parseMap(T t) {
        return JSONObject.parseObject(JSON.toJSONString(t));
    }


    /**
     * @param t      要分组的类
     * @param fields 要取值的字段
     * @return 返回值用中划线拼接 -
     */
    public static String objectValue(Object t, String[] fields) {
        StringBuffer key = new StringBuffer();
        try {
            for (Field field : t.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                for (String f : fields) {
                    if (f.equals(fieldName)) {
                        String value = Objects.isNull(field.get(t)) ? "" : field.get(t).toString();
                        key.append(value).append("-");
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new CommonException("Utils.listGroup: " + e.getMessage());
        }

        return key.toString();
    }

    /**
     * 根据MAP取值的值，作为KEY值
     *
     * @param map     其中字段拼装KEY值
     * @param mapKeys
     * @author jiangling.zheng@hand-china.com 2020/8/14 9:05
     */
    public static String getMapKey(Map<String, Object> map, String[] mapKeys) {

        StringBuffer keyStr = new StringBuffer();
        for (String key : mapKeys) {
            keyStr.append(map.get(key));
        }
        return keyStr.toString();
    }

    /**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     *
     * @param str
     * @param strStart
     * @param strEnd
     * @return
     */
    public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static Date getNowDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (true) {
            Date date = new Date();
            String dataStr = format.format(date);
            if (!dataStr.contains("9999")) {
                return date;
            }

        }
    }

    /**
     * 以当前的时间毫秒为批次ID
     */
    public static String getBatchId() {
        String batchId = String.valueOf(Utils.getNowDate().getTime());
        return batchId;
    }

    /**
     * 判断是不是数字
     *
     * @return
     */
    public boolean isNum(String str) {
        String regex = "^[-\\+]?[\\d]*$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }
}
