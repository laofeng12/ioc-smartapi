package com.openjava.datalake.util;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author xjd
 * @Date 2019/12/21 14:21
 * @Version 1.0
 */
public class MD5Utils {

    //盐，用于混交md5
    private static final String slat = "PWD#ioc201912";
    /**
     * 直接用Spring工具
     * 生成md5
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        String base = str + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 不用引包
     * @param dataStr
     * @return
     */
    public static String encrypt(String dataStr) {
        try {
            dataStr = dataStr + slat;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 返回大写的
     * @param data
     * @return
     * @throws Exception
     */
    public static String MD5(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        data = data + slat;
        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] array = md.digest(data.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();

        for (byte item : array) {

            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));

        }

        return sb.toString().toUpperCase();

    }

    public static void main(String[] args) {
        String data = "123456";
        String md5 = getMD5(data);
        String encrypt = encrypt(data);
        String s = null;
        try {
            s = MD5(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(md5);
        System.out.println(encrypt);
        System.out.println(s);
    }
}
