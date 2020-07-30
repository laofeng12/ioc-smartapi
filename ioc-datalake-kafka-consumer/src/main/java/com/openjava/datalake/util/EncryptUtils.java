package com.openjava.datalake.util;

import org.ljdp.secure.cipher.AES;

/**
 * @Author xjd
 * @Date 2019/8/27 9:34
 * @Version 1.0
 */
public class EncryptUtils {

    /**
     * AES加密数据
     * @param ivStr
     * @param sKey
     * @param sourceString
     * @return
     */
    public static String aesEncry(String ivStr, String sKey, String sourceString) {
        AES aes = new AES(sKey, ivStr);
        String dataEnyStr = null;
        try {
            dataEnyStr = aes.encryptBase64(sourceString);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据加密异常");
            return "";
        }

        return dataEnyStr;
    }

    /**
     * AES解密
     * @param ivStr
     * @param sKey
     * @param encryptData
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String ivStr, String sKey, String encryptData) {
        String result = null;

        try {
            AES aes = new AES(sKey, ivStr);
            result = aes.decryptBase64(encryptData);
        } catch (Exception e) {//解密异常
            e.printStackTrace();
        }

        return result;
    }
}
