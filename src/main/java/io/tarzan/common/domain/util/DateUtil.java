package io.tarzan.common.domain.util;

import org.apache.commons.lang.StringUtils;
import org.hzero.core.base.BaseConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: weihua.liao
 * @date: 2019/9/9
 * @description:
 */
public class DateUtil {

    private final static String formatDefault = "yyyy-MM-dd HH:mm:ss";
    private final static SimpleDateFormat sdfFormat = new SimpleDateFormat(formatDefault);

    public static LocalDateTime convert(String datetime) {
        if (StringUtils.isEmpty(datetime)) {
            return null;
        }
        return LocalDateTime.parse(datetime,
                DateTimeFormatter.ofPattern(BaseConstants.Pattern.DATETIME));
    }

    public static String date2String(Date date, String format){
        if(StringUtils.isEmpty(format)){
            format = formatDefault;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return
                sdf.format(date);
    }

    public static Date string2Date(String date,String format){
        if(StringUtils.isEmpty(format)){
            format = formatDefault;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date calDate(Date date,int step){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,step);
        return calendar.getTime();
    }



    /**
    * @Description 对传入Date类型时间进行加减小时操作
    * @Param [date, hour]
    * @return java.util.Date
    * @date 2020/1/8 9:18 上午
    * @auther selino
    */
    public static Date hourAddOrSub(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);
        Date startDate = calendar.getTime();
        return startDate;
    }


    /**
    * @Description 对传入字符串类型时间进行加减小时操作
    * @Param [dateStr, hour]
    * @return java.util.Date
    * @date 2020/1/8 9:18 上午
    * @auther selino
    */
    public static Date hourAddOrSub(String dateStr, int hour) {
        try {
            return hourAddOrSub(sdfFormat.parse(dateStr), hour);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
    * @Description 对传入Date类型时间进行加减小时操作
    * @Param [date, hour]
    * @return java.lang.String
    * @date 2020/1/8 9:27 上午
    * @auther selino
    */
    public static String hourAddOrSubStr(Date date, int hour) {
        return sdfFormat.format(hourAddOrSub(date,hour));
    }

    /**
    * @Description 对传入字符串类型时间进行加减小时操作
    * @Param [dateStr, hour]
    * @return java.lang.String
    * @date 2020/1/8 9:27 上午
    * @auther selino
    */
    public static String hourAddOrSubStr(String dateStr, int hour) {
        try {
            return sdfFormat.format(hourAddOrSub(sdfFormat.parse(dateStr), hour));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取时间间隔
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getDistanceTime(Date startDate, Date endDate) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long time1 = startDate.getTime();
        long time2 = endDate.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        return hour + ":" + min + ":" + sec;
    }

}
