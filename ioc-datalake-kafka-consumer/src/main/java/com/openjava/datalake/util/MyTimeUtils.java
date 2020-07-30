package com.openjava.datalake.util;

import java.math.BigDecimal;

/**
 * @Author xjd
 * @Date 2019/11/20 11:02
 * @Version 1.0
 */
public class MyTimeUtils {

    public static String millsToHours(Long timeMillis, int scale) {

        Double dHours = timeMillis/Double.valueOf(1000)/60/60;
        return BigDecimal.valueOf(dHours).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }


}
