package com.openjava.datalake.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xjd
 * @Date 2019/7/11 10:36
 * @Version 1.0
 */
public class MySpringBeanUtils {

    private static String[] getBlankPropertiesToIgnore(Object bean) {
        List<String> blankProperteNameList = new ArrayList<>();
        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                Object value = declaredField.get(bean);
                if (value == null || StringUtils.isBlank(String.valueOf(value))) {
                    blankProperteNameList.add(declaredField.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String[] nullProperteNames = blankProperteNameList.toArray(new String[blankProperteNameList.size()]);
        return nullProperteNames;

    }

    public static String[] getNullPropertiesToIgnore(Object bean){
        List<String> nullProperteNameList = new ArrayList<>();
        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                Object value = declaredField.get(bean);
//                Class<?> type = declaredField.getType();
                if (value == null) {
                    nullProperteNameList.add(declaredField.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String[] nullProperteNames = nullProperteNameList.toArray(new String[nullProperteNameList.size()]);
        return nullProperteNames;
    }
    public static void copyPropertiesNotNull(Object source, Object target){
        String[] nullPropertiesToIgnore = getNullPropertiesToIgnore(source);
        BeanUtils.copyProperties(source, target, nullPropertiesToIgnore);
    }

    public static void copyPropertiesNotBlank(Object source, Object target){
        String[] blankPropertiesToIgnore = getBlankPropertiesToIgnore(source);
        BeanUtils.copyProperties(source, target, blankPropertiesToIgnore);
    }
}
