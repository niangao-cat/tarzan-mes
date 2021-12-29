package io.tarzan.common.domain.util;

import org.hzero.core.helper.LanguageHelper;
import org.hzero.mybatis.domian.Language;

import java.util.List;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/23 14:23
 * @Description:
 */
public class MtLanguageHelper {

    /**
     * 获取当前系统语言（若系统没有，返回默认zh_CN)
     *
     * @author chuang.yang
     * @date 2019/12/23
     * @param
     * @return java.lang.String
     */
    public static String language() {
        return LanguageHelper.language();
    }

    /**
     * 获取当前系统所有语言
     *
     * @author chuang.yang
     * @date 2019/12/23
     * @param
     * @return java.lang.List
     */
    public static List<Language> languages() {
        return io.choerodon.mybatis.helper.LanguageHelper.languages();
    }
}
