package com.openjava.datalake.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.ljdp.util.DateFormater;

import java.text.ParseException;
import java.util.Date;

/**
 * @Author xjd
 * @Date 2020/1/7 10:44
 * @Version 1.0
 */
public class MyDateFormater extends DateFormater {
    // MM/dd/yyyy HH:mm:ss 或 M/d/yyyy HH:mm:ss
    public static final String MATCH_mdyHms = "^((0(\\d)|1[0-2]{1})[/\\s]{1}|[1-9]{1}[/\\s]{1})(([0-2]{1}(\\d)|3[01]{1}){1}[/\\s]{1}||[1-9]{1}[/\\s]{1})((\\d){4}) (([01]{1}(\\d)|2[0-3]{1})(:([0-5]{1}(\\d))){2})$";
    // yyyy-MM-dd HH:mm:ss.SSS
    public static final String MATCH_yMdHmsS = "^((\\d){4})[-\\S]{1}(0(\\d)|1[0-2]{1})[-\\S]{1}([0-2]{1}(\\d)|3[01]{1}){1} (([01]{1}(\\d)|2[0-3]{1})(:([0-5]{1}(\\d))){2})\\.(\\d){1,3}$";
    // yyyyMMdd
    public static final String MATCH_yyyyMMdd = "^((\\d){4})(0(\\d)|1[0-2]{1})([0-2]{1}(\\d)|3[01]{1}){1}$";

    public static final String PATTERN_mdyHms = "MM/dd/yyyy HH:mm:ss";
    public static final String PATTERN_yMdHmsS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_yyyyMMdd = "yyyyMMdd";

    public static FastDateFormat DATETIME_mdyHms = FastDateFormat.getInstance(PATTERN_mdyHms);
    public static FastDateFormat DATETIME_yMdHmsS = FastDateFormat.getInstance(PATTERN_yMdHmsS);
    public static FastDateFormat DATETIME_yyyyMMdd = FastDateFormat.getInstance(PATTERN_yyyyMMdd);

    public static Date praseDate(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        Date date = null;
        try {
            if (dateString.matches(MATCH_mdyHms)) {
                date = DATETIME_mdyHms.parse(dateString);
            } else if (dateString.matches(MATCH_yMdHmsS)) {
                date = DATETIME_yMdHmsS.parse(dateString);
            } else if (dateString.matches(MATCH_yyyyMMdd)) {
                date = DATETIME_yyyyMMdd.parse(dateString);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            date = DateFormater.praseDate(dateString);
        }
        return date;
    }

    public static void main(String[] args) {
        Date date = praseDate("2019-01-01 23:24:25.123");
        Date date2 = praseDate("1/1/2019 23:24:25");
        Date date3 = praseDate("2019-01-01 23:24:25");
        System.out.println(date);
        System.out.println(date2);
        System.out.println(date3);
    }
}
