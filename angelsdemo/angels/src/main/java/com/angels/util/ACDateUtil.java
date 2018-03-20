package com.angels.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;
import android.text.format.DateFormat;

/**
 * 
* @ClassName: ACDateUtil 
* @Description: 功能：时间
* @author angelsC
* @date 2015-10-15 下午4:20:56 
*
 * 1,将时间戳转为代表"距现在多久之前"的字符串
 * 2,判断是否同一天
 * 3,获取yyyy年MM月dd日显示日期
 */
public class ACDateUtil {
	/**
     * 将时间戳转为代表"距现在多久之前"的字符串
     * 
     * @param timeStr unix时间戳
     * @param fullDate 是否显示年月日
     * @param inChinese 年、月、日 三个字是否用中文显示
     * @return
     */
    public static String getStandardDate(String timeStr, boolean fullDate, boolean inChinese) {
        if (TextUtils.isEmpty(timeStr)) {
            return "";
        }

       
        long t = 0;
        try {
            t = Long.parseLong(timeStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
        return getStandardDate(t, fullDate, inChinese);
    }
    public static String getStandardDate(long t, boolean fullDate, boolean inChinese) {
        if (t==0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        long time = System.currentTimeMillis() - (t * 1000);
        long hour = (long)Math.floor(time / 60 / 60 / 1000.0f);// 小时

        long day = (long)Math.floor(time / 24 / 60 / 60 / 1000.0f);// 天前

        String currentYear = DateFormat.format("yyyy", new Date(System.currentTimeMillis()))
                .toString();
        String year = DateFormat.format("yyyy", new Date(t * 1000)).toString();

        if (day > 0) { // 超过一天
            if (fullDate) { // 显示年月日
                if (inChinese) {
                    sb.append(DateFormat.format("yyyy年MM月dd日", new Date(t * 1000)));
                } else {
                    sb.append(DateFormat.format("yyyy-MM-dd", new Date(t * 1000)));
                }
            } else { // 当前年度不显示年份
                if (currentYear.equals(year)) {
                    if (inChinese) {
                        sb.append(DateFormat.format("MM月dd日", new Date(t * 1000)));
                    } else {
                        sb.append(DateFormat.format("MM-dd", new Date(t * 1000)));
                    }
                } else {
                    if (inChinese) {
                        sb.append(DateFormat.format("yyyy年MM月dd日", new Date(t * 1000)));
                    } else {
                        sb.append(DateFormat.format("yyyy-MM-dd", new Date(t * 1000)));
                    }
                }
            }
        } else if (hour > 0) {
            sb.append(hour + "小时前");
        } else {
            sb.append("刚刚");
        }
        return sb.toString();
    }
    
    /**
     * 判断是否同一天
     * 
     * @param oneTime
     * @param antherTime
     * @return
     */
    public static boolean isSameDay(long oneTime, long antherTime) {
        if (oneTime == antherTime) {
            return true;
        } else if (Math.abs(oneTime - antherTime) > 24 * 60 * 60 * 1000) {
            return false;
        }
        try {
            oneTime = oneTime * (oneTime < 10000000000L ? 1000 : 1);
            antherTime = antherTime * (antherTime < 10000000000L ? 1000 : 1);
            String oneDay = DateFormat.format("dd", new Date(oneTime)).toString();
            String antherDay = DateFormat.format("dd", new Date(antherTime)).toString();
            return oneDay.equals(antherDay);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 获取yyyy年MM月dd日显示日期
     * 
     * @param time 时间戳
     * @param inChinese 是否用中文显示
     * @param hasDay 是否显示日
     * @return
     */
    public static String GetFullDate(long time, boolean inChinese, boolean hasDay) {
        if (time == 0) {
            return time + "";
        }
        long tempTime = time * (time < 10000000000L ? 1000 : 1);
        CharSequence year = DateFormat.format("yyyy", new Date(tempTime));
        CharSequence month = DateFormat.format("MM", new Date(tempTime));
        CharSequence day = DateFormat.format("dd", new Date(tempTime));
        if (inChinese) {
            return year + "年" + month + "月" + (hasDay ? day + "日" : "");
        }
        return year + "-" + month + (hasDay ? "-" + day : "");
    }

    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // long类型转换为String类型
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }
    
}

