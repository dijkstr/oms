package com.hundsun.boss.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

public class CommonUtils {
    private CommonUtils() {
    };

    /**
     * 得到两位小数double.
     * 
     * @param d 传入的double().
     * @return 返回的数值.
     */
    public static double twoDecimalDouble(double d) {
        BigDecimal tb = new BigDecimal(Double.toString(d));
        return tb.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 得到n位小数double.
     * 
     * @param d 传入的double().
     * @param n 保留小数位数.
     * @return 返回的数值.
     */
    public static double decimalDouble(double d, int n) {
        BigDecimal tb = new BigDecimal(Double.toString(d));
        return tb.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 得到两位小数String.
     * 
     * @param d 传入的double().
     * @return 返回的数值.
     */
    public static String twoDecimalStrDouble(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }


    /**
     * 是否是月的第一天.
     * 
     * @param time format yyyy-MM-dd
     */
    public static boolean judgeFirstDayOfMoth(String time) {
        String str = time.substring(8, 10);
        if ("01".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回日期列表.
     * 
     * @param begin_date 日期时间段开始区间(包含)
     * @param end_date 日期时间段结束区间(包含)
     * @param y 想开始时间相差月份数.
     * @param n 取连续n个月日期，以y所对应月份开始.
     * @return
     * @throws ParseException
     */
    public static List<String> getMothList(String begin_date, String end_date, int y, int n) throws ParseException {
        List<String> monthList = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDateTime = format.parse(begin_date);
        Date endDateTime = format.parse(end_date);

        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDateTime);
        beginCalendar.set(Calendar.DAY_OF_MONTH, 1);
        endCalendar.setTime(endDateTime);
        endCalendar.set(Calendar.DAY_OF_MONTH, 1);

        for (int i = 0; i < n; i++) {
            if (i == 0) {
                beginCalendar.add(Calendar.MONTH, y);
            } else {
                beginCalendar.add(Calendar.MONTH, 1);
            }
            if (beginCalendar.after(endCalendar)) {
                break;
            }
            monthList.add(format.format(beginCalendar.getTime()));
        }
        return monthList;
    }

    /**
     * 返回当前订单季度的第一个月的时间.
     * 
     * @param begin_date 日期时间段开始区间(包含)
     * @param end_date 日期时间段结束区间(包含)
     * @param y 想开始时间相差月份数.
     * @param n 取连续n个月日期，以y所对应月份开始.
     * @return
     * @throws ParseException
     */
    public static String getMothFirst(String begin_date, int y) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDateTime = format.parse(begin_date);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDateTime);
        beginCalendar.set(Calendar.DAY_OF_MONTH, 1);
        beginCalendar.add(Calendar.MONTH, y);
        return format.format(beginCalendar.getTime());
    }

    /**
     * 返回较晚的时间.
     * 
     * @param charge_begin_time
     * @param prod_begin_date_str
     * @return
     * @throws ParseException
     */
    public static String getAfterTime(String charge_begin_time, String prod_begin_date_str) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginCountDate = format.parse(charge_begin_time);
        Date beginProDate = format.parse(prod_begin_date_str);
        if (beginCountDate.before(beginProDate)) {
            return prod_begin_date_str;
        } else {
            return charge_begin_time;
        }
    }

    /**
     * 返回较早的时间.
     * 
     * @param charge_end_time
     * @param prod_end_date_str
     * @return
     * @throws ParseException
     */
    public static String getBeforeTime(String charge_end_time, String prod_end_date_str) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date endCountDate = format.parse(charge_end_time);
        Date endProDate = format.parse(prod_end_date_str);
        if (endCountDate.before(endProDate)) {
            return charge_end_time;
        } else {
            return prod_end_date_str;
        }
    }

    /**
     * 判断对象是否为空
     * 
     * @param obj
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNullorEmpty(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof String && "".equals(obj)) {
            return true;
        } else if (obj instanceof Number && (NumberUtils.compare(((Number) obj).doubleValue(), 0) == 0)) {
            return true;
        } else if (obj instanceof Boolean && !((Boolean) obj)) {
            return true;
        } else if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        } else if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        } else if (obj instanceof Object[] && ((Object[]) obj).length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 当月第一天
     * 
     * @return
     */
    public static String getMonthFirstDay(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(date);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuilder str = new StringBuilder().append(day_first);
        return str.toString();

    }

    /**
     * 当月最后一天
     * 
     * @param date
     * @return
     */
    public static String getMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    /**
     * 获取月份最后日期
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getMaxMonthDate(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

}
