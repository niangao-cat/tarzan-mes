package com.ruike.wms.infra.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static com.ruike.wms.infra.constant.WmsConstant.BLANK;

/**
 * 字符串 工具类
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 17:57
 */
public class StringCommonUtils {

    public static boolean equalsIgnoreBlank(String str, String cmpStr) {
        return Optional.ofNullable(str).orElse(BLANK).equals(Optional.ofNullable(cmpStr).orElse(BLANK));
    }

    public static boolean contains(String str, String... items) {
        if (StringUtils.isBlank(str)) {
            return false;
        }

        if (items.length != 0) {
            for (String item : items) {
                if (str.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }
}
