package com.openjava.datalake.util.secret;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datalake.push.vo.ApiSyncReceiveDataVO;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author zmk
 * @data 2020/01/16
 */
public class SHA256Utils {
    /**
     * HmacSHA256获取签名，据参数字段的ASCII码值进行排序 加密签名,故使用SortMap进行参数排序
     */
    public static String createSignByHmacSHA256(SortedMap<String, String> parameters, String key) throws Exception {
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
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] array = sha256_HMAC.doFinal(sbparameters.toString().getBytes("UTF-8"));
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("加密失败");
        }
    }

    /**
     * HmacSHA256获取签名，据参数字段的ASCII码值进行排序 加密签名,故使用SortMap进行参数排序
     */
    public static String createSignBySHA256(SortedMap<String, String> parameters, String key) throws Exception {
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
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(sbparameters.toString().getBytes("UTF-8"));
            String encodestr = byte2Hex(messageDigest.digest());
            return encodestr.toUpperCase();
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("加密失败");
        }
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();

    }

    public static void main(String[] args) throws Exception{
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
        System.out.println(SHA256Utils.createSignByHmacSHA256(parameters,"192006250b4c09247ec02edce69f6a2d"));
        System.out.println(SHA256Utils.createSignBySHA256(parameters,"192006250b4c09247ec02edce69f6a2d"));
    }
}
