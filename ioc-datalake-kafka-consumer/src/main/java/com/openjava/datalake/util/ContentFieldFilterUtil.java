package com.openjava.datalake.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * 字段过滤（前台只显示部分字段）
 * @author lsw
 *
 */
public class ContentFieldFilterUtil {

    public static void doListFilter(List<?> objList, Set<String> needShowFieldSet) {
        try {
            for (Object obj : objList) {
                if (obj == null || needShowFieldSet == null || needShowFieldSet.isEmpty()) {
                    return;
                }

                Class<?> clazz = obj.getClass();
                Field[] declaredFields = clazz.getDeclaredFields();
                for (Field field : declaredFields) {
                    if (needShowFieldSet.contains(field.getName())) {
                        continue;
                    }
                    if ("serialVersionUID".equals(field.getName())) {
                        continue;
                    }
//                System.out.println("----" + field.getName());

                    field.setAccessible(true);
                    if (field.get(obj) == null) {
                        continue;
                    }

                    field.set(obj, null);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 过滤
     * @param obj
     * @param needShowFieldSet
     */
    public static void doFilter(Object obj, Set<String> needShowFieldSet) {
        if (obj == null || needShowFieldSet == null || needShowFieldSet.isEmpty()) {
            return;
        }
        
        try {
            Class<?> clazz = obj.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (needShowFieldSet.contains(field.getName())) {
                    continue;
                }
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
//                System.out.println("----" + field.getName());
                
                field.setAccessible(true);
                if (field.get(obj) == null) {
                    continue;
                }
                
                field.set(obj, null);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * 过滤
     * @param obj
     * @param notNeedShowFieldSet
     */
    public static void doFilterIndeed(Object obj, Set<String> notNeedShowFieldSet) {
        if (obj == null || notNeedShowFieldSet == null || notNeedShowFieldSet.isEmpty()) {
            return;
        }
        
        try {
            Class<?> clazz = obj.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (!notNeedShowFieldSet.contains(field.getName())) {
                    continue;
                }
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
//                System.out.println("----" + field.getName());
                
                field.setAccessible(true);
                if (field.get(obj) == null) {
                    continue;
                }
                
                field.set(obj, null);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
}
