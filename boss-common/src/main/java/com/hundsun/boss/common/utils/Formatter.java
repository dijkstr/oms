/*
 * Created on 2004-9-16
 */
package com.hundsun.boss.common.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Formatter {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    public static final String TIME_FORMAT1 = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT2 = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT1 = "yyyy-MM-dd";
    public static final String DATE_FORMAT2 = "yyyy年MM月dd日";
    public static final String DATE_FORMAT3 = "yyyyMMdd";
    public static final String DATE_FORMAT4 = "yyyyMM";
    public static final String DATE_FORMAT5 = "yyyy年MM月";

    public static final String DECIMAL_FORMAT1 = "###0.00";
    public static final String DECIMAL_FORMAT2 = "###0.000";
    public static final String DECIMAL_FORMAT3 = "###0.000000";
    public static final String DECIMAL_FORMAT4 = "###0.00000000";
    public static final String DECIMAL_FORMAT5 = "###0";
    public static final String DECIMAL_FORMAT6 = "#,##0.00";
    public static final String DECIMAL_FORMAT7 = "#.###";
    public static final String DECIMAL_FORMAT8 = "###0.#";
    public static final String DECIMAL_FORMAT9 = "###0.##";
    public static final String DECIMAL_FORMAT10 = "###0.###";
    public static final String DECIMAL_FORMAT11 = "###0%";
    public static final String DECIMAL_FORMAT12 = "###0.0%";
    public static final String DECIMAL_FORMAT13 = "###0.00%";
    public static final String DECIMAL_FORMAT14 = "###0.000%";
    public static final String DECIMAL_FORMAT15 = "###0.########";

    /**
     * 线程安全格式化时间
     * 
     * @param pattern
     * @param date
     * @return
     */
    public static String formatDate(String pattern, Object date) {
        if (date instanceof String) {
            return (String) date;
        }
        return FastDateFormat.getInstance(pattern).format(date);
    }

    /**
     * 格式化时间字符串
     * 
     * @param pattern
     * @param date
     * @return
     * @throws ParseException
     */
    public static java.util.Date parseUtilDate(String pattern, String date) throws ParseException {
        DateFormat parser = new SimpleDateFormat(pattern);
        return parser.parse(date);
    }

    /**
     * 格式化数字
     * 
     * @param pattern
     * @param decimal
     * @return
     */
    public static String formatDecimal(String pattern, Object decimal) {
        if(CommonUtil.isNullorEmpty(decimal)){
            decimal = 0;
        }
        if (decimal instanceof String) {
            return (String) decimal;
        }
        return new DecimalFormat(pattern).format(decimal);
    }

    /**
     * 取两天之间的差值.
     * 
     * @param endDate
     * @param beginDate
     * @return
     */
    public static Integer getDiffDays(Date endDate, Date beginDate) {
        long time = Math.abs(endDate.getTime() - beginDate.getTime());
        return (int) ((time / (24 * 3600 * 1000)) + 1);
    }
}
