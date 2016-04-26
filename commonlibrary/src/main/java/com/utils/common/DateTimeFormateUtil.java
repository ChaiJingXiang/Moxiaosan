package com.utils.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fengyongqiang on 15/10/16.
 */
public class DateTimeFormateUtil {
    private final static long dayMillisecond = 24 * 3600 * 1000;//一天的毫秒数

    //若是今天显示今天  若是昨天显示昨天  今天  12:08
    // 大于两天显示“日期+时间”          2015-08-20 13:10
    public static String getTimeFormLong(long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(millisecond);
        String time = format.format(date);

        Calendar current = Calendar.getInstance(); //要比较的时间
        current.setTime(date);

        Calendar today = Calendar.getInstance();    //今天的零点
        today.set(Calendar.YEAR, today.get(Calendar.YEAR));
        today.set(Calendar.MONTH, today.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天的零点
        yesterday.set(Calendar.YEAR, yesterday.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, yesterday.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, yesterday.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        if (current.after(today)) {
            return "今天 " + time.split(" ")[1];
        } else if (current.before(today) && current.after(yesterday)) {
            return "昨天 " + time.split(" ")[1];
        } else {
            return time;
        }
    }

    //当天消息“今天 23：00”，其他时间显示“2015-09-01”
    public static String getOnlineTime(long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Calendar today = Calendar.getInstance();    //今天的零点
        today.set(Calendar.YEAR, today.get(Calendar.YEAR));
        today.set(Calendar.MONTH, today.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Date date = new Date(millisecond);
        Calendar current = Calendar.getInstance(); //要比较的时间
        current.setTime(date);
        if (current.after(today)) {
            return "今天 " + getHourMinute(millisecond);
        } else {
            return format.format(date);
        }
    }

    public static String getAcTime(long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String time = format.format(millisecond);

        Date date = new Date();
        String sysTime = format.format(date);
        if (time.equals(sysTime)) {
            return "今天 " + getHourMinute(millisecond);
        } else {
            return time + "   " + getHourMinute(millisecond);
        }
    }

    public static String getPayTime(long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(millisecond);
        return time + "   " + getHourMinute(millisecond);
    }


    //今天和明天，则显示“今天”“明天”；若不是，则显示“周几”。
    // 8月20日 今天     8月29日 周三
    public static String getFutureTime(long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        Date date = new Date(millisecond);
        String time = format.format(date);

        Calendar current = Calendar.getInstance(); //要比较的时间
        current.setTime(date);

        Calendar today = Calendar.getInstance();    //今天的24点
        today.set(Calendar.YEAR, today.get(Calendar.YEAR));
        today.set(Calendar.MONTH, today.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);

        Calendar tomorrow = Calendar.getInstance();    //明天的24点
        tomorrow.set(Calendar.YEAR, tomorrow.get(Calendar.YEAR));
        tomorrow.set(Calendar.MONTH, tomorrow.get(Calendar.MONTH));
        tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) + 1);
        tomorrow.set(Calendar.HOUR_OF_DAY, 23);
        tomorrow.set(Calendar.MINUTE, 59);
        tomorrow.set(Calendar.SECOND, 59);

        if (current.before(today)) {
            return time + " 今天";
        } else if (current.after(today) && current.before(tomorrow)) {
            return time + " 明天";
        } else { //判断是周几
            return time + " " + getWeekDay(millisecond);
        }
    }

    public static String getYear(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String start = sf.format(time);
        return start;
    }

    //判断三个时间 今天  同一天  跨天
    public static String getAnotherDay(long sysTime, long startTime, long endTime) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String sys = sf.format(sysTime);
        String start = sf.format(startTime);
        String end = sf.format(endTime);
        if (sys.equals(start) && sys.equals(end)) {//今天
            return "今天  " + getHourMinute(startTime) + "～" + getHourMinute(endTime);
        } else if (start.equals(end)) {  //同一天
            return start + " " + getHourMinute(startTime) + "～" + getHourMinute(endTime);
        } else { //跨天
            return start + " " + getHourMinute(startTime) + "～" + end + " " + getHourMinute(endTime);
        }
    }

    //判断两个时间  如果跨天就显示 20:00~8月23日 24:00
    // 是同一天就显示            20:00~ 24:00
    public static String getAnotherDay(long startTime, long endTime) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String start = sf.format(startTime);
        String end = sf.format(endTime);
        if (start.equals(end)) {  //同一天
            return start + " " + getHourMinute(startTime) + "～" + getHourMinute(endTime);
        } else { //跨天
            return start + " " + getHourMinute(startTime) + "～" + end + " " + getHourMinute(endTime);
        }
    }

    //单场次跨天：8月28日 20:00 ~8月29日 23：00
    //单场次当天：8月28日 20:00 ~ 23：0
    public static String getShareTime(long startTime, long endTime) {
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日");
        if (getIsSameDay(startTime,endTime)){  //同天
            return sf.format(startTime)+" "+getHourMinute(startTime)+"~"+getHourMinute(endTime);
        }else {
            Date date2 = new Date(startTime);
            Date date3 = new Date(endTime);
            return sf.format(date2) + "  " + getHourMinute(startTime) + "~" + sf.format(date3) + "  " + getHourMinute(endTime);
        }
    }

    //判断两个时间  是不是同一天
    public static boolean getIsSameDay(long startTime, long endTime) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String start = sf.format(startTime);
        String end = sf.format(endTime);
        if (start.equals(end)) {  //同一天
            return true;
        } else { //跨天
            return false;
        }
    }

    //判断两个时间  如果跨天就显示 8月20日 20:00~8月23日 24:00
    // 是同一天就显示           8月20日 周三 20:00~24:00
    public static String getSignedTime(long startTime, long endTime) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM月dd日");
        String start = sf.format(startTime);
        String end = sf.format(endTime);
        if (start.equals(end)) {  //同一天
            Date date = new Date(startTime);
            return sf2.format(date) + " " + getWeekDay(startTime) + " " + getHourMinute(startTime) + "~" + getHourMinute(endTime);
        } else { //跨天
            Date date2 = new Date(startTime);
            Date date3 = new Date(endTime);
            return sf2.format(date2) + " " + getHourMinute(startTime) + "~" + sf2.format(date3) + " " + getHourMinute(endTime);
        }
    }

    //8月28日 ~ 9月23日
    public static String getMonthDay(long startTime, long endTime) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        Date date1 = new Date(startTime);
        Date date2 = new Date(endTime);
        return format.format(date1) + " ~ " + format.format(date2);
    }

    public static String getWeekDay(long millisecond) {
        Date date = new Date(millisecond);
        Calendar current = Calendar.getInstance();
        current.setTime(date);
        String myWeek = null;
        switch (current.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                myWeek = "日";
                break;
            case 2:
                myWeek = "一";
                break;
            case 3:
                myWeek = "二";
                break;
            case 4:
                myWeek = "三";
                break;
            case 5:
                myWeek = "四";
                break;
            case 6:
                myWeek = "五";
                break;
            case 7:
                myWeek = "六";
                break;
            default:
                break;
        }
        return "周" + myWeek;
    }

    //获取几点几分
    // 20:00
    public static String getHourMinute(long millisecond) {
        String hour = "";
        Date date = new Date(millisecond);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        hour = formatter.format(date);
        return hour;
    }

    //获取今天的零点
    public static Long getTodayStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    //获取今天的24点
    public static Long getTodayEndTime() {
        Calendar todayEnd = Calendar.getInstance();    //今天的24点
        todayEnd.set(Calendar.YEAR, todayEnd.get(Calendar.YEAR));
        todayEnd.set(Calendar.MONTH, todayEnd.get(Calendar.MONTH));
        todayEnd.set(Calendar.DAY_OF_MONTH, todayEnd.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);

        return todayEnd.getTime().getTime();
    }

}
