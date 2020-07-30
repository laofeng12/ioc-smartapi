package com.openjava.datalake.util.secret;

import java.util.Random;

/**
 * @author  zmk
 * 2020/01/16
 */
public class RandomUtils {
    private static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTER_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SMALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER_CHAR = "0123456789";

    /**
     * 获取定长的随机数，包含大小写、数字
     *
     * @param length 随机数长度
     */
    public static String generateString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (; length > 0; length--) {
            sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 获取定长的随机数，包含大小写字母
     *
     * @param length 随机数长度
     */
    public static String generateMixString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (; length > 0; length--) {
            sb.append(LETTER_CHAR.charAt(random.nextInt(LETTER_CHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 获取定长的随机数，包含小写字母、数字
     *
     * @param length 随机数长度
     */
    public static String generateSmallString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (; length > 0; length--) {
            sb.append(SMALL_CHAR.charAt(random.nextInt(SMALL_CHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 获取定长的随机数，只包含小写字母
     *
     * @param length 随机数长度
     */
    public static String generateLowerString(int length) {
        return generateMixString(length).toLowerCase();
    }

    /**
     * 获取定长的随机数，只包含大写字母
     *
     * @param length 随机数长度
     */
    public static String generateUpperString(int length) {
        return generateMixString(length).toUpperCase();
    }

    /**
     * 获取定长的随机数，只包含数字
     *
     * @param length 随机数长度
     */
    public static String generateNumberString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (; length > 0; length--) {
            sb.append(NUMBER_CHAR.charAt(random.nextInt(NUMBER_CHAR.length())));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String pwd =  RandomUtils.generateString(12);
        String pwd2 =  RandomUtils.generateSmallString(12);
        System.out.println(pwd);
        System.out.println(pwd2);
    }

}
