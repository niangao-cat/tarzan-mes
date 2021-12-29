package io.tarzan.common.domain.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;


/**
 * chuang.yang 2019.3.4 数字校验工具类
 */
public class NumberHelper {

    private static Pattern isNumericPattern = Pattern.compile("[0-9]*");
    private static Pattern isSixDecimalPattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d{1,6})?$");
    private static Pattern isDouble = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
    /**
     * 正浮点数
     */
    private static Pattern isPositiveNumber = Pattern.compile("^((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*))$");

    /**
     * 判断输入字符串是否为正整数
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }

        return isNumericPattern.matcher(str).matches();
    }

    /**
     * 判断输入字符串小数位是否为6位
     * 
     * @param str
     * @return
     */
    public static boolean isSixDecimal(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }

        return isSixDecimalPattern.matcher(str).matches();
    }

    /**
     * 判断浮点数（double和float）
     *
     * @author chuang.yang
     * @date 2019/5/2
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }

        return isDouble.matcher(str).matches();
    }

    /**
     * 判断整数
     *
     * @param s
     * @return
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    /**
     * @Description 判断是否是正浮点数
     * @param s
     * @return boolean
     * @Date 2020-05-13 10:01
     * @Author han.zhang
     */
    public static boolean isPositiveNumber(String s) {
        if(StringUtils.isEmpty(s)){
            return false;
        }
        return isPositiveNumber.matcher(s).matches();
    }

}
