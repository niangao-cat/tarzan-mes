package com.ruike.hme.infra.util;

import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.domain.AuditDomain;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommonUtils {

    public static final Pattern PATTERN = Pattern.compile("-?[0-9]+.?[0-9]*");
    //邮箱判断正则表达
    public static final Pattern EMAIL = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

    private static final String[] IGNORE_TABLE_FIELDS = new String[]{MtMaterialLot.FIELD_IDENTIFICATION,
            AuditDomain.FIELD_OBJECT_VERSION_NUMBER, AuditDomain.FIELD_LAST_UPDATED_BY, AuditDomain.FIELD_CREATED_BY,
            AuditDomain.FIELD_CREATION_DATE, AuditDomain.FIELD_LAST_UPDATE_DATE};

    /***
     * @Description: 判断是否为数字
     * @author: sanfeng.zhang
     * @date 2020/7/13 20:24
     * @param numStr
     * @return : boolean
     * @version 1.0
     */
    public static boolean isNumeric(String numStr) {
        Matcher isNum = PATTERN.matcher(numStr);
        return isNum.matches();
    }

    public static String dateStrFormat(String dateStr, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {

        }
        return sdf.format(date);
    }

    /**
     * 字符串转日期
     *
     * @param dateStr
     * @param pattern
     * @return java.util.Date
     * @author penglin.sui@hand-china.com 2021/6/23 19:21
     */
    public static Date stringToDate(String dateStr , String pattern){
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            date = simpleDateFormat.parse(dateStr);
        } catch (Exception e) {
            throw new CommonException(e);
        }
        return date;
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @param pattern
     * @return java.lang.String
     * @author penglin.sui@hand-china.com 2021/7/28 16:09
     */
    public static String dateToString(Date date , String pattern) {
        SimpleDateFormat sformat = new SimpleDateFormat(pattern);//日期格式
        String tiem = sformat.format(date);
        return tiem;
    }

    /**
     * 获取某月第一天
     *
     * @param date
     * @param pattern
     * @param during
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2020/10/22 22:24
     */
    public static String monthStartDayTime(Date date, String pattern, Integer during) {
        if (during == null) {
            during = 0;
        }
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, during);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return dateFormat.format(calendar.getTime());
    }

    public static List<String> queryMonthDailyList(Date date) {
        List<String> dailyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 返回指定日历字段可能捅有的最大值
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= maxDay; i++) {
            dailyList.add(String.valueOf(i));
        }
        return dailyList;
    }

    /***
     * @Description: 获取当前时间
     * @author: penglin.sui
     * @date 2021/07/28 16:13
     * @return : Date
     * @version 1.0
     */
    public static Date currentTimeCommonGet(String pattern) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(currentTime.format(formatter), DateTimeFormatter.ofPattern(pattern));
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /***
     * @Description: 获取当前时间
     * @author: penglin.sui
     * @date 2020/11/10 9:26
     * @return : Date
     * @version 1.0
     */
    public static Date currentTimeGet() {
        return currentTimeCommonGet("yyyy-MM-dd HH:mm:ss");
    }

    /***
     * @Description: 计算时间
     * @author: penglin.sui
     * @date 2021/7/28 15:51
     * @return : Date
     * @version 1.0
     */
    public static Date calculateDate(Date date , int internal , int type , String pattern){
        SimpleDateFormat dft = new SimpleDateFormat(pattern);
        Date beginDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, internal);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(calendar.getTime()));
        }catch (Exception ex){

        }
        return endDate;
    }

    /**
     * 拆分数据
     *
     * @param sqlList  源数据
     * @param splitNum 拆分数量
     * @return 拆分数据
     * @author jiangling.zheng@hand-china.com 2020/7/30 17:01
     */
    public static <T> List<List<T>> splitSqlList(List<T> sqlList, int splitNum) {

        List<List<T>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    /**
     * 返回两个日期的天数
     *
     * @param dateOne
     * @param dateTwo
     * @return java.lang.Long
     * @author sanfeng.zhang@hand-china.com 2021/3/9 12:52
     */
    public static Long betweenDays(Date dateOne, Date dateTwo) {
        if (dateOne == null || dateTwo == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOne);
        long timeInMillis1 = calendar.getTimeInMillis();
        calendar.setTime(dateTwo);
        long timeInMillis2 = calendar.getTimeInMillis();
        return (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);
    }

    public static <E, T> void copyPropertiesIgnoreNullAndTableFields(E src, T target) {
        // 对象值转换时屏蔽表字段：ID，创建更新人等信息
        Set<String> hashSet = new HashSet<>();
        hashSet.addAll(Arrays.asList(WmsCommonUtils.getNullPropertyNames(src)));
        hashSet.addAll(Arrays.asList(IGNORE_TABLE_FIELDS));
        String[] result = new String[hashSet.size()];
        BeanUtils.copyProperties(src, target, hashSet.toArray(result));
    }

    //字母转化成对应数字(一位字母)
    public static String changeNum(String str) {
        char charStr = str.charAt(0);
        Integer charNum = Integer.valueOf(charStr);
        Integer result = charNum - 64;
        return result.toString();
    }

    /**
     * 判断是否是字母
     *
     * @param str 传入字符串
     * @return 是字母返回true，否则返回false
     */
    public static boolean isAlpha(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.matches("[a-zA-Z]+");
    }

    public static String numberToUpperLetter(int num) {
        if (num <= 0) {
            return null;
        }
        StringBuilder letter = new StringBuilder();
        num--;
        do {
            if (letter.length() > 0) {
                num--;
            }
            letter.insert(0, ((char) (num % 26 + (int) 'A')));
            num = (num - num % 26) / 26;
        } while (num > 0);

        return letter.toString();

    }

    public static boolean isEmail(String email) {
        Matcher isEmail = EMAIL.matcher(email);
        return isEmail.matches();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1 开始时间
     * @param date2 结束时间
     * @param type  类型
     * @param isContainer 是否包含起止
     * @return
     */
    public static int differentByMillisecond(Date date1,Date date2,int type,boolean isContainer)
    {
        int diff = 0;
        switch(type){
            case Calendar.HOUR_OF_DAY:
                diff = ((int) ((date2.getTime() - date1.getTime()) / (1000*60*60)));
                break;
            case Calendar.DAY_OF_MONTH:
                diff = ((int) ((date2.getTime() - date1.getTime()) / (1000*60*60*24)));
                break;
            default:
                break;
        }
        if(isContainer){
            diff += 1;
        }
        return diff;
    }
}
