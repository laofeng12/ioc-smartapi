package com.openjava.datalake.util.secret;

import com.commons.utils.SmUtil;
import com.openjava.util.ByteUtils;

import java.util.SortedMap;

/**
 * 国密加密工具,支持sm3、sm4
 */
public class SmUtils {

    //算法
    private static final String SM3 = "sm3";
    private static final String SM4 = "sm4";

    /**
     * sm3加密
     * @param jsonData 参数
     * @param key 秘钥
     * @return
     * @throws Exception
     */
    public static String sm3Encrypt(String jsonData, String key) throws Exception {
        return SmUtil.sm3(jsonData,key);
    }

    /**
     * sm3加密2,针对排序
     * @param parameters 参数
     * @param key 秘钥
     * @return
     * @throws Exception
     */
    public static String sm3SortEncrypt(SortedMap<String, String> parameters, String key) throws Exception {
        String jsonData = getJsonSortByASCII(parameters,key);
        return SmUtil.sm3(jsonData,key);
    }

    /**
     * sm3验证原文和密文是否一致
     * @param jsonData 原文
     * @param sm3HexString  sm3加密有的密文
     * @param key 秘钥
     * @return
     * @throws Exception
     */
    public static boolean sm3verify(String jsonData , String sm3HexString,String key ) throws Exception {
        return SmUtil.verifySm3(jsonData,sm3HexString,key);
    }

    /**
     * sm4加密
     * @param jsondata
     * @param key
     * @return
     * @throws Exception
     */
    public static String sm4Encrypt(String jsondata, String key) throws Exception {
        return SmUtil.sm4(key,jsondata);
    }

    /**
     * sm4加密,针对排序
     * @param parameters
     * @param key
     * @return
     * @throws Exception
     */
    public static String sm4SortEncrypt(SortedMap<String, String> parameters, String key) throws Exception {
        String jsondata = getJsonSortByASCII(parameters,key);
        return SmUtil.sm4(key,jsondata);
    }

    /**
     * 输入秘钥密文和原文，验证是否一致
     * @param jsondata 原文
     * @param sm4HexString 密文
     * @param key 秘钥
     * @return
     * @throws Exception
     */
    public static boolean sm4verify(String jsondata , String sm4HexString, String key) throws Exception {
        return SmUtil.verifySm4(key, sm4HexString, jsondata);
    }

    /**
     * 输入秘钥和密文，得到解密后的数据
     * @param sm4HexString
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptSm4(String sm4HexString, String key) throws Exception {
        return SmUtil.decryptSm4(key, sm4HexString);
    }


    /**
     * 参数名ASCII码从小到大排序（字典序）,返回json
     */
    public static String getJsonSortByASCII(SortedMap<String, String> parameters,String key){
        StringBuffer sbparameters= new StringBuffer();
        StringBuilder sb = new StringBuilder();
        for (String k : parameters.keySet()) {
            Object v = parameters.get(k);
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sbparameters.append(k + "=" + v + "&");
            }
        }
        sbparameters.append("key=" + key);// 最后加密时添加密钥，由于key值放在最后，所以不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
//        System.out.println(sbparameters.toString());
        return sbparameters.toString();
    }

    public static String getKey() throws Exception {
        byte[] bytes = SmUtil.generateKey();
        String generateKey = ByteUtils.toHexString(bytes);
        return generateKey;

    }

    public static void main(String[] args) throws Exception {
        String data = "{\"pushSequence\":\"12345689\",\"beginTimestamp\":\"20191231000000\",\"endTImestamp\":\"20191231000000\",\"column\":[\"字段1\",\"字段2\", \"字段3\"],\"data\":[[\"数据1\",\"数据2\",\"数据3\"],[\"数据1\",\"数据2\",\"数据3\"]],\"audit_info\":{\"terminal_info\":\"192.168.1.1\",\"query_timestamp\":\"670985423406\"}}";
        String key = SmUtils.getKey();
        System.out.println(key);
        String sm3HexString = SmUtils.sm3Encrypt(data,key);
        System.out.println(sm3HexString);
        System.out.println(SmUtils.sm3verify(data,sm3HexString,key));
        String sm4 =  SmUtils.sm4Encrypt(data,key);
        System.out.println(sm4);
        System.out.println(SmUtils.sm4verify(data,sm4,key));
        System.out.println(SmUtils.decryptSm4(sm4,key));
    }
}
