package com.hiveelpay.common.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.hiveelpay.common.exceptions.ErrorCode;
import com.hiveelpay.common.exceptions.HiveelPayException;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wilson  wilson@hiveel.com
 * @version V1.0
 * @Description: 日期时间工具类
 * @date 2017-07-05
 * @Copyright: pay.hiveel.com
 */
public class DateUtil {

    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddhhmmssSSS";
    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddhhmmss";

    public static final String FORMAT_MMYY = "MM/YY";
    public static final String FORMAT_MMYYYY = "MM/YYYY";
    public static final String FORMAT_4_EMAIL = "MM/dd/YYYY hh:mm:ss";
    public static final String FORMAT_4_EMAIL_2 = "MM/dd/YYYY hh:mm";
    public static final String FORMAT_4_EMAIL_1 = "MM/dd/YYYY";

    public static final String FORMAT_MMddyyyy = "MMddyyyy";
    public static final String FORMAT_MMyyyy = "MMyyyy";
    public static final String FORMAT_MMddyyyyHHmm = "MMddyyyyHHmm";
    public static final String FORMAT_HHmm = "HHmm";
    public static final String FORMAT_dd = "dd";

    public static boolean isCreditCardExpirationDate(String dateStr) {
        if (Strings.isNullOrEmpty(dateStr)) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_MMYY);
            sdf.parse(dateStr);
            return true;
        } catch (Exception ignored) {
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_MMYYYY);
            sdf.parse(dateStr);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static String getCurrentDate() {
        String formatPattern_Short = "yyyyMMddhhmmss";
        SimpleDateFormat format = new SimpleDateFormat(formatPattern_Short);
        return format.format(new Date());
    }

    public static String getSeqString() {
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss"); // "yyyyMMdd G
        return fm.format(new Date());
    }

    public static String getSeqStringShort() {
        return String.valueOf(new Date().getTime());
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取当前时间，格式为 yyyyMMddHHmmss
     *
     * @return
     */
    public static String getCurrentTimeStr(String format) {
        format = StringUtils.isBlank(format) ? FORMAT_YYYY_MM_DD_HH_MM_SS : format;
        Date now = new Date();
        return date2Str(now, format);
    }

    public static String formatDayNumToDayStr(Integer dayNum) {
        if (dayNum == null) {
            return null;
        }
        if (dayNum >= 1 && dayNum <= 9) {
            return "0" + dayNum;
        }
        return String.valueOf(dayNum);
    }

    public static String date2Str(Date date) {
        return date2Str(date, FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 时间转换成 Date 类型
     *
     * @param date
     * @param format
     * @return
     */
    public static String date2Str(Date date, String format) {
        if (date == null) {
            return "";
        }
        if ((format == null) || format.trim().equals("")) {
            format = FORMAT_YYYY_MM_DD_HH_MM_SS;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取批量付款预约时间
     *
     * @return
     */
    public static String getRevTime() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        String dateString = new SimpleDateFormat(DateUtil.FORMAT_YYYYMMDDHHMMSS).format(cal.getTime());
        System.out.println(dateString);
        return dateString;
    }

    /**
     * 时间比较
     *
     * @param date1
     * @param date2
     * @return DATE1>DATE2返回1，DATE1<DATE2返回-1,等于返回0
     */
    public static int compareDate(String date1, String date2, String format) {
        DateFormat df = new SimpleDateFormat(format);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 把给定的时间减掉给定的分钟数
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date minusDateByMinute(Date date, int minute) {
        Date newDate = new Date(date.getTime() - (minute * 60 * 1000));
        return newDate;
    }

    public static synchronized Date addHours(Date date, int hours) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, hours);
        return c.getTime();
    }

    public static synchronized Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, days);
        return c.getTime();
    }

    public static synchronized Date addMonths(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    /**
     * @param dateStr
     * @param format  default MM/YYYY
     * @return
     */
    public static Date strToDate(String dateStr, String format) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(dateStr), "str to date fmt, parameter of dateStr can't null!");
        if (Strings.isNullOrEmpty(format)) {
            format = FORMAT_MMYYYY;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            throw new HiveelPayException(ErrorCode.DATE_FMT_ERROR);
        }
    }

    public static int getIntervalDays(Date a, Date b) {
        if (a.before(b)) {
            return (int) ((b.getTime() - a.getTime()) / (1000 * 60 * 60 * 24));
        }
        return (int) ((a.getTime() - b.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static void main(String args[]) {
        System.out.println(getIntervalDays(strToDate("08012018", "MMddyyyy"), strToDate("08032018", "MMddyyyy")));
    }
}
