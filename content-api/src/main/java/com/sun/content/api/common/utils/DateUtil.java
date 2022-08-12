package com.sun.content.api.common.utils;

import org.springframework.util.Assert;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @描述：日期工具类
 * @作者: 孙世龙
 * @日期: 2022-04-19
 */
public class DateUtil {

    public static Date localConvertDate(LocalDateTime dateTime) {
        Assert.notNull(dateTime, "localConvertDate is not permission null!");
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = dateTime.atZone(zoneId);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    public static Date getDayBegin() {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return localConvertDate(todayStart);
    }

    public static Date getDayEnd() {
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return localConvertDate(todayEnd);
    }

    // 获取昨天的开始时间
    public static Date getBeginDayOfYesterday() {
        LocalDateTime yestEnd = LocalDateTime.now().minusDays(1);
        return localConvertDate(getDayBeginTime(yestEnd));
    }

    // 获取昨天的结束时间
    public static Date getEndDayOfYesterday() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localConvertDate(getDayEndTime(localDateTime.minusDays(1)));
    }

    private static LocalDateTime getDayEndTime(LocalDateTime d) {
        Assert.notNull(d, "getDayEndTime's param is not permission null!");
        return LocalDateTime.of(d.toLocalDate(), LocalTime.MAX);
    }

    private static LocalDateTime getDayBeginTime(LocalDateTime d) {
        Assert.notNull(d, "getDayBeginTime's param is not permission null!");
        return LocalDateTime.of(d.toLocalDate(), LocalTime.MIN);
    }


    // 获取本周的开始时间
    public static Date getBeginDayOfWeek() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localConvertDate(getDayStartTime(localDateTime.with(DayOfWeek.MONDAY)));

    }


    // 获取上周的开始时间
    public static Date getBeginDayOfLastWeek() {
        LocalDateTime localDateTime = LocalDateTime.now().minusWeeks(1);
        return localConvertDate(getDayStartTime(localDateTime.with(DayOfWeek.MONDAY)));
    }


    // 获取本月的开始时间
    public static Date getBeginDayOfMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginDateTime = now.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
        return localConvertDate(beginDateTime);
    }


    // 获取上月的开始时间
    public static Date getBeginDayOfLastMonth() {
        LocalDateTime now = LocalDateTime.now().minusMonths(1);
        LocalDateTime beginDateTime = now.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
        return localConvertDate(beginDateTime);
    }

    // 获取  当前时间 - 1年
    public static Date getDayOfLastYear() {
        LocalDateTime dateTime = LocalDateTime.now().minusYears(1);
        return localConvertDate(dateTime);
    }

    public static Date getDateOfLastMonth(){
        LocalDateTime now = LocalDateTime.now().minusMonths(1);
        return localConvertDate(now);
    }


    // 获取某个日期的开始时间
    public static LocalDateTime getDayStartTime(LocalDateTime d) {
        Assert.notNull(d, "getDayStartTime's param is not permission null!");
        return LocalDateTime.of(d.toLocalDate(), LocalTime.MIN);
    }


    // 获取今年是哪一年
    public static Integer getNowYear() {
        LocalDateTime now = LocalDateTime.now();
        return now.getYear();
    }

    // 获取本月是哪一月
    public static int getNowMonth() {
        LocalDateTime now = LocalDateTime.now();
        return now.getMonthValue();
    }

    // 两个日期相减得到的天数
    public static int getDiffDays(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }
        long diff = (endDate.getTime() - beginDate.getTime())
                / (1000 * 60 * 60 * 24);
        return new Long(diff).intValue();
    }

    // 两个日期相减得到的毫秒数
    public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }

    // 获取两个日期中的最大日期
    public static Date max(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return beginDate;
        }
        return endDate;
    }

    // 获取两个日期中的最小日期
    public static Date min(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return endDate;
        }
        return beginDate;
    }

    public static LocalDateTime dateConvertLocal(Date date) {
        Assert.notNull(date, "dateConvertLocal date is not permission null!");
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(date.toInstant(), zoneId);
    }

    // 返回某个日期下几天的日期
    public static Date getNextDay(Date date, int i) {
        Assert.notNull(date, "getNextDay param's date is not permission null!");
        LocalDateTime localDateTime = dateConvertLocal(date);
        return localConvertDate(localDateTime.plusDays(i));
    }

    /**
     * 获取现在时间
     *
     */
    public static String getStringDate(Date currentTime) {
        Assert.notNull(currentTime, "getStringDate param's currentTime is not permission null!");
        LocalDateTime localDateTime = dateConvertLocal(currentTime);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        return dtf.format(localDateTime);
    }

    public static Date strToDate(String strDate) {
        Assert.hasText(strDate, "strToDate's param strDate is not permission null!");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localConvertDate(LocalDateTime.parse(strDate, dtf));

    }

    public static String strToDateDay(String strDate) {
        Assert.hasText(strDate, "strToDateDay is not permission null!");
        LocalDate localDate3 = LocalDate.parse(strDate, DateTimeFormatter.ISO_LOCAL_DATE);
        return localDate3.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * 根据月份参数获取当月开始时间
     *
     * @param mon 1-12 数字
     * @return 2022.03.22
     */
    public static Date getStartByMonth(int mon) {
        if (mon == 0) return getBeginDayOfMonth();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginDateTime = now.withMonth(mon).with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
        return localConvertDate(beginDateTime);
    }
}
