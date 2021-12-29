package io.tarzan.common.domain.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MtIdHelper {
    private MtIdHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * String类型主键转Long类型
     *
     * @param  kId String类型的主键
     * @return Long类型的主键
     */
    public static Long str2Long(String kId) {
        return StringUtils.isEmpty(kId) ? null : Long.parseLong(kId);
    }

    /**
     * Long类型主键转String类型
     *
     * @param kId Long类型的主键
     * @return String类型的主键
     */
    public static String long2Str(Long kId) {
        return kId == null ? "" : kId + "";
    }

    /**
     * 判断存放关联标主键字段是否不为null
     *
     * @param kId 主键
     * @return boolean
     */
    public static boolean isIdNotNull(String kId) {
        return StringUtils.isNotEmpty(kId);
    }

    /**
     * 判断存放关联标主键字段是否为null
     *
     * @param kId 主键
     * @return
     * @return boolean
     */
    public static boolean isIdNull(String kId) {
        return StringUtils.isEmpty(kId);
    }

    /**
     * 判断存放关联标主键字段是否为null
     *
     * @param kIdList 主键集合
     * @return
     * @return boolean
     */
    public static boolean isIdListNull(List<String> kIdList) {
        if(CollectionUtils.isEmpty(kIdList)){
            return true;
        }
        else{
            if(kIdList.size() == 1 && StringUtils.isEmpty(kIdList.get(0))){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两个long型对象是否相同
     *
     * @param l1
     * @param l2
     * @return
     */
    public static boolean isSame(String l1, String l2) {
        if (null == l1 && null == l2) {
            return true;
        }

        if (null == l1) {
            return false;
        } else if (null == l2) {
            return false;
        } else {
            return l1.equalsIgnoreCase(l2);
        }
    }

    /**
     * 判断两个long型对象是否不相同
     *
     * @param l1
     * @param l2
     * @return
     */
    public static boolean isNotSame(String l1, String l2) {
        return !isSame(l1, l2);
    }
}
