package com.openjava.datalake.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author xjd
 * @Date 2020/1/2 19:09
 * @Version 1.0
 */
public class MyJsonUtils {

    /**
     * 解决JS精度丢失问题
     * 转换对象中的long类型、BigInteger类型、BigDecimal类型。
     * 将数值转成String，避免js最多只能存17位数字的精度丢失问题。
     * @param obj
     * @param <T>
     * @return
     */
    public static  <T> T processJsonAccuracy(T obj) {
        SerializeConfig serializeConfig  = SerializeConfig.getGlobalInstance();
        serializeConfig.put(BigInteger .class, ToStringSerializer.instance);
        serializeConfig.put(BigDecimal .class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
//        serializeConfig.put(oracle.sql.TIMESTAMP.class,  new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
//        serializeConfig.put(java.sql.Timestamp.class,  new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        String s = JSON.toJSONString(obj, serializeConfig, SerializerFeature.PrettyFormat);
        T objString = (T) JSON.parseObject(s, obj.getClass());
        return objString;
    }
}
