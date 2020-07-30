package com.openjava.datalake.util;


import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Author:   Finn
 * Date:     2020/5/13 17:57
 */
public class SeqUtils {

    /**
     *   将数字格式化为字符串
     * @param number 数字
     * @param max  最大位
     * @param min  最小位
     * @return
     */
    public static String getFormatString(Long number,int max,int min) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);//是否分组
        nf.setMaximumIntegerDigits(max);//最大整数位数
        nf.setMinimumIntegerDigits(min);//最小整数位数
        return nf.format(number);
    }

    /**
     *  根据时序数获取时序字符串,时序数位固定12位
     * @param seq 时序数
     * @return
     */
    public static String getSeqStrByNumber(Long seq){
        if(seq==null){
            seq=1L;
        }
        String seqStr=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        seqStr+=SeqUtils.getFormatString(seq,12,12);
        return seqStr;
    }

    /**
     * md5加密,32位
     * @param str
     * @return
     */
    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return str;
    }


    /**
     * 防篡改码获取
     * @param list
     * @return
     */
    public static String hashTokimnhj(List<Object> list){
        StringBuffer sb=new StringBuffer();
        for (Object o : list) {
            if(o!=null){
                sb.append(o.toString());
            }
        }
        String hashTokimnhj= md5(sb.toString());
        return hashTokimnhj;
    }

    public static void main(String[] args) {
//        System.out.println(getSeqStrByNumber(2L)+1);
        System.out.println(getFormatString(154L,2,1));
        StringBuilder sb = new StringBuilder();
        String hashTokimnhjStr = SeqUtils.md5(sb.append(DateUtils.getTimestamp()).append("PWD#ioc201912").toString());
        System.out.println(hashTokimnhjStr);
    }

}
