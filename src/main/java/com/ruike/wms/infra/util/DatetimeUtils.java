package com.ruike.wms.infra.util;

import org.apache.commons.lang.time.DateUtils;

import java.time.*;
import java.util.*;

/**
 * 日期时间 工具包
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 15:55
 */
public class DatetimeUtils {

    public static Date getBeginOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        LocalDate beginDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        return LocalDateToDate(beginDate);
    }

    public static Date getEndOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return cal.getTime();
    }

    /**
     * 根据范围返回日期列表
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return java.util.List<java.util.Date>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/19 08:55:57
     */
    public static List<Date> getDateListInRange(Date startDate, Date endDate) {
        List<Date> list = new ArrayList<>();
        if (Objects.isNull(startDate) || Objects.isNull(endDate) || startDate.after(endDate)) {
            return list;
        }
        Date addDate = getBeginOfDate(startDate);
        do {
            list.add(addDate);
            addDate = DateUtils.addDays(addDate, 1);
        } while (addDate.compareTo(endDate) < 0);
        return list;
    }

    /**
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return java.util.List<java.time.LocalDate>
     * @Description 根据起止日期返回日期列表（包括起止日期）
     * @author yuchao.wang
     * @date 2020/8/27 9:35
     */
    public static List<LocalDate> getDateListInRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> list = new ArrayList<>();
        if (Objects.isNull(startDate) || Objects.isNull(endDate) || startDate.isAfter(endDate)) {
            return list;
        }
        LocalDate addDate = startDate;
        while (addDate.isBefore(endDate) || addDate.equals(endDate)) {
            list.add(addDate);
            addDate = addDate.plusDays(1);
        }
        return list;
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    public static LocalTime dateToLocalTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalTime();
    }

    public static Date LocalDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date LocalDateToDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date LocalTimeToDate(LocalTime localTime) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }
}
