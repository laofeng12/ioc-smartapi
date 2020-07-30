package com.openjava.datalake.util;

/**
 * @Author JiaHai
 * @Description 进制转换工具类
 */
public class BinaryConversionUtils {
    /**
     * 十进制转换36进制
     *
     * @param n 十进制数字
     * @return
     */
    public static String intToThirtySixBinary(int n) {
        return intToCustomRadix(n, 36);
    }

    /**
     * 自定义进制转换（输入十进制数字）
     *
     * @param n     十进制数字
     * @param radix 需要转换的进制数
     * @return
     */
    private static String intToCustomRadix(int n, int radix) {
        StringBuilder sb = new StringBuilder();
        String s;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        while (n != 0) {
            sb = sb.append(b[n % radix]);
            n = n / radix;
        }
        s = sb.reverse().toString();
        return s;
    }

    public static void main(String[] args) {
        // 三十六进制 zzzz
        System.out.println(intToThirtySixBinary(1679615));
        System.out.println(intToThirtySixBinary(10));
    }

}
