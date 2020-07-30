package com.openjava.datalake.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author xjd
 * @Date 2019/7/8 17:48
 * @Version 1.0
 */
public class RandomNumUtils {

    public static String geSubscribeWorkNum() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date(System.currentTimeMillis());
        String prefixWorkNum = simpleDateFormat.format(now);
        String randomCode = RandomStringUtils.random(3, false, true);
        return prefixWorkNum + randomCode;
    }

    public static String geRandomCode(int size) {

        String vcode = "";
        for (int i = 0; i < size; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        // 生成6位随机
//        String random=(int)((Math.random()*9+1)*100000)+"";
        return vcode;
    }

    public static void main(String[] args) {
        String s = geSubscribeWorkNum();
        System.out.println(s);
    }

}
