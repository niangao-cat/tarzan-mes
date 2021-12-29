package com.ruike.itf.utils;

import org.apache.logging.log4j.util.Strings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 根据传入的字段对比该类中的字段是否有值
 *
 * @author jiangling.zheng@hand-china.com 2020/8/12 14:36
 */
public class GetDeclaredFields<T> {

    public List<String> getDeclaredFields(T t, String[] fields) {
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
}
