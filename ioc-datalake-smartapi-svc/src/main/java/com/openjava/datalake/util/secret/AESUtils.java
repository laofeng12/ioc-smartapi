package com.openjava.datalake.util.secret;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datalake.push.vo.ApiSyncReceiveDataVO;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author zmk
 * @date 2020/01/14
 */
public class AESUtils {
//    // 密匙
//    private static final String KEY = "1234567890abcdef";
//    // 偏移量
//    private static final String OFFSET = "1234567890abcdef";
    // 编码
    private static final String ENCODING = "UTF-8";
    //算法
    private static final String ALGORITHM = "AES";
    // 默认的加密算法
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";


    /**
     * 加密
     * @param parameters
     * @return
     * @throws Exception
     * @return String
     * @author zmk
     * @date   2020/01/16
     */
    public static String encrypt(SortedMap<String, String> parameters, String key, String offset) throws Exception {
        String pStr =  AESUtils.getJsonSortByASCII(parameters, key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("ASCII"), ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(offset.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(pStr.getBytes(ENCODING));
        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
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
        System.out.println(sbparameters.toString());
        return sbparameters.toString();
    }
    /**
     * 解密
     * @param data
     * @return
     * @throws Exception
     * @return String
     * @author zmk
     * @date   2020/01/16
     */
    public static String decrypt(String data,String key,String offset) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("ASCII"), ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(offset.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] buffer = new BASE64Decoder().decodeBuffer(data);
        byte[] encrypted = cipher.doFinal(buffer);
        return new String(encrypted, ENCODING);//此处使用BASE64做转码。
    }


    public static void main(String[] args) throws Exception {
        String param = "{\"pushSequence\":\"12345689\",\"beginTimestamp\":\"20191231000000\",\"endTImestamp\":\"20191231000000\",\"column\":[\"字段1\",\"字段2\", \"字段3\"],\"data\":[[\"数据1\",\"数据2\",\"数据3\"],[\"数据1\",\"数据2\",\"数据3\"]],\"audit_info\":{\"terminal_info\":\"192.168.1.1\",\"query_timestamp\":\"670985423406\"}}";
        SortedMap<String, String> parameters = new TreeMap<>();
        ApiSyncReceiveDataVO vo = new ApiSyncReceiveDataVO();
        vo.setPushSequence("12345689");
        vo.setBeginTimestamp("20191231000000");
        vo.setEndTImestamp("20191231000000");
        List<String> column = new ArrayList<>();
        column.add("字段1");
        column.add("字段2");
        column.add("字段3");
        vo.setColumn(column);
        List<String[]> data = new ArrayList<>();
        String[] s1 = {"数据1","数据2","数据3"};
        String[] s2 = {"数据1","数据2","数据3"};
        data.add(s1);
        data.add(s2);
        vo.setData(data);
        vo.setAudit_info("{\"terminal_info\":\"192.168.1.1\",\"query_timestamp\":\"670985423406\"}");
        StringBuffer signParams = new StringBuffer();
        parameters.put("pushSequence",vo.getPushSequence());
        parameters.put("beginTimestamp",vo.getBeginTimestamp());
        parameters.put("endTImestamp",vo.getEndTImestamp());
        parameters.put("column", JSONObject.toJSONString(vo.getColumn()));
        parameters.put("data", JSONObject.toJSONString(vo.getData()));
        parameters.put("audit_info",vo.getAudit_info());
        String encrypt =  AESUtils.encrypt(parameters,"1234567890abcdef","1234567890abcdef");
        System.out.println(encrypt);
        String decrypt = AESUtils.decrypt(encrypt,"1234567890abcdef","1234567890abcdef");
        System.out.println(decrypt);
    }
}
