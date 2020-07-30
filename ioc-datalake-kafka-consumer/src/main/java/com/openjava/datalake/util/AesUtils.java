package com.openjava.datalake.util;

import org.ljdp.secure.cipher.AES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author: lsw
 * @Date: 2019/8/30 17:49
 */
public class AesUtils {
    private static final Logger LOG = LoggerFactory.getLogger(AesUtils.class);

    private static final String S_KEY = "1234567890abcdef";
    private static final String IV_STR = "abcdef1234567890";
    private static final AES aes = new AES(S_KEY, IV_STR);

    /**
     * AES加密
     *
     * @param str
     * @return
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static String encrypt(String str) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        LOG.debug("加密前的字符串：" + str);
        //加密后的字符串
        String encryptStr = aes.encryptBase64(str);
        LOG.debug("加密后的字符串：" + encryptStr);

        return encryptStr;
    }

    /**
     * AES解密
     *
     * @param str
     * @return
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static String decrypt(String str) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        LOG.debug("解密前的字符串：" + str);
        //解密后的字符串
        String decryptStr = aes.decryptBase64(str);
        LOG.debug("解密后的字符串：" + decryptStr);

        return decryptStr;
    }

}
