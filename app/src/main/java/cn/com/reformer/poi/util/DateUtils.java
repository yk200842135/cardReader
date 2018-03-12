package cn.com.reformer.poi.util;

import android.app.AlarmManager;
import android.content.Context;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2016-11-04.
 */
public class DateUtils {
    public static final String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String dateFormatYMD = "yyyy-MM-dd";
    public static final String dateFormatYM = "yyyy-MM";
    public static final String dateFormatYMDHM = "yyyy-MM-dd HH:mm";
    public static final String dateFormatMD = "MM/dd";
    public static final String dateFormatHMS = "HH:mm:ss";
    public static final String dateFormatHM = "HH:mm";
    public static final String AM = "AM";
    public static final String PM = "PM";

    public static String getCurrentDate(String format)
    {
        String curDateTime = null;
        try
        {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return curDateTime;
    }

    public static String getStringByFormat(String strDate, String format)
    {
        String mDateTime = null;
        try
        {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return mDateTime;
    }

    public static String getStringByFormat(long milliseconds, String format)
    {
        String thisDateTime = null;
        try
        {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(Long.valueOf(milliseconds));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    /**
     * 描述：String类型的日期时间转化为Date类型.
     *
     * @param strDate String形式的日期时间
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return Date Date类型日期时间
     */
    public static Date getDateByFormat(String strDate, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = mSimpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 描述：计算两个日期所差的天数.
     *
     * @param milliseconds1 the milliseconds1
     * @param milliseconds2 the milliseconds2
     * @return int 所差的天数
     */
    public static int getOffectDay(long milliseconds1, long milliseconds2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(milliseconds1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(milliseconds2);
        //先判断是否同年
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = d1 - d2;
        }
        return day;
    }

    /**
     * 描述：计算两个日期所差的小时数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的小时数
     */
    public static int getOffectHour(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int h = 0;
        int day = getOffectDay(date1, date2);
        h = h1-h2+day*24;
        return h;
    }

    /**
     * 描述：计算两个日期所差的分钟数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的分钟数
     */
    public static int getOffectMinutes(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int m1 = calendar1.get(Calendar.MINUTE);
        int m2 = calendar2.get(Calendar.MINUTE);
        int h = getOffectHour(date1, date2);
        int m = 0;
        m = m1-m2+h*60;
        return m;
    }

    public static void setDate(long milliseconds){
        try {
            Process process = Runtime.getRuntime().exec("su");
            String datetime = getStringByFormat(milliseconds,"yyyyMMdd.HHmmss");//"20160826.124500"; //测试的设置的时间【时间格式 yyyyMMdd.HHmmss】
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT + 08:00 \n");
            os.writeBytes("/system/bin/date -s "+datetime+"\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        SystemClock.setCurrentTimeMillis(milliseconds);
    }

    public static Date getDateByOffset(Date date, int calendarField, int offset)
    {
        Calendar c = new GregorianCalendar();
        try
        {
            c.setTime(date);
            c.add(calendarField, offset);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return c.getTime();
    }

    public static void setDate(Context context, String strDate){
        long milliseconds = getDateByFormat(strDate,dateFormatYMDHMS).getTime();
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        am.setTime(milliseconds);
    }
}
