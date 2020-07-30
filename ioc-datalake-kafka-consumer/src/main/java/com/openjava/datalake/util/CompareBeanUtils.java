package com.openjava.datalake.util;

import org.apache.commons.collections4.CollectionUtils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author xjd
 * @date 2019/08/19 14:02:05
 */
public class CompareBeanUtils {
    /**
     * <p>Title: compareFields</p>  
     * <p>Description: </p>  
     *   比较两个实体属性值
     * @param obj1
     * @param obj2
     * @param ignoreArr 忽略的字段
     * @return  
     */
     public static Map<String, List<Object>> compareFieldsIgnore(Object obj1, Object obj2, String[] ignoreArr) {
            try{  
                Map<String, List<Object>> map = new HashMap<String, List<Object>>();  
                List<String> ignoreList = null;  
                if(ignoreArr != null && ignoreArr.length > 0){  
                    // array转化为list  
                    ignoreList = Arrays.asList(ignoreArr);  
                }  
                if (obj1.getClass() == obj2.getClass()) {// 只有两个对象都是同一类型的才有可比性  
                    Class clazz = obj1.getClass();  
                    // 获取object的属性描述  
                    PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,  
                            Object.class).getPropertyDescriptors();  
                    for (PropertyDescriptor pd : pds) {// 这里就是所有的属性了  
                        String name = pd.getName();// 属性名  
                        if(ignoreList != null && ignoreList.contains(name)){// 如果当前属性选择忽略比较，跳到下一次循环  
                            continue;  
                        }  
                        Method readMethod = pd.getReadMethod();// get方法  
                        // 在obj1上调用get方法等同于获得obj1的属性值  
                        Object o1 = readMethod.invoke(obj1);  
                        // 在obj2上调用get方法等同于获得obj2的属性值  
                        Object o2 = readMethod.invoke(obj2);  
                        if(o1 instanceof Timestamp){  
                            o1 = new Date(((Timestamp) o1).getTime());  
                        }  
                        if(o2 instanceof Timestamp){  
                            o2 = new Date(((Timestamp) o2).getTime());  
                        }  
                        if(o1 == null && o2 == null){  
                            continue;  
                        }else if(o1 == null && o2 != null){  
                            List<Object> list = new ArrayList<Object>();  
                            list.add(o1);  
                            list.add(o2);  
                            map.put(name, list);  
                            continue;  
                        }  
                        if (!o1.equals(o2)) {// 比较这两个值是否相等,不等就可以放入map了  
                            List<Object> list = new ArrayList<Object>();  
                            list.add(o1);  
                            list.add(o2);  
                            map.put(name, list);  
                        }  
                    }  
                }  
                return map;  
            }catch(Exception e){  
                e.printStackTrace();  
                return null;  
            }  
        }

    /**
     * <p>Title: compareFields</p>
     * <p>Description: </p>
     *   比较两个实体属性值
     * @param obj1
     * @param obj2
     * @param compareArr 对比的字段
     * @return
     */
    public static Map<String, List<Object>> compareFields(Object obj1, Object obj2, String[] compareArr) {
        try{
            Map<String, List<Object>> map = new HashMap<String, List<Object>>();
            List<String> compareList = null;
            if(compareArr != null && compareArr.length > 0){
                // array转化为list
                compareList = Arrays.asList(compareArr);
            }
            if (obj1.getClass() == obj2.getClass()) {// 只有两个对象都是同一类型的才有可比性
                Class clazz = obj1.getClass();
                // 获取object的属性描述
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,
                        Object.class).getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {// 这里就是所有的属性了
                    String name = pd.getName();// 属性名
                    if(compareList != null && compareList.contains(name)){// 只对比传入的属性名
                        Method readMethod = pd.getReadMethod();// get方法
                        // 在obj1上调用get方法等同于获得obj1的属性值
                        Object o1 = readMethod.invoke(obj1);
                        // 在obj2上调用get方法等同于获得obj2的属性值
                        Object o2 = readMethod.invoke(obj2);
                        if(o1 instanceof Timestamp){
                            o1 = new Date(((Timestamp) o1).getTime());
                        }
                        if(o2 instanceof Timestamp){
                            o2 = new Date(((Timestamp) o2).getTime());
                        }
                        if(o1 == null && o2 == null){
                            continue;
                        }else if(o1 == null && o2 != null){
                            List<Object> list = new ArrayList<Object>();
                            list.add(o1);
                            list.add(o2);
                            map.put(name, list);
                            continue;
                        }
                        // 到这里 o1 != null 若o2 == null 也是不equals
                        if (!o1.equals(o2)) {// 比较这两个值是否相等,不等就可以放入map了
                            List<Object> list = new ArrayList<Object>();
                            list.add(o1);
                            list.add(o2);
                            map.put(name, list);
                        }
                    }
                }
            }
            return map;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Map<Object, Object> comparaList(List<?> oldPermittedColumnsSource, List<?> newApplyColumnsSource, String keyFieldName, String [] ignoFieldNames) {
        Class<?> oldPermittedClass = null;
        Class<?> newPermittedClass = null;
        if (CollectionUtils.isNotEmpty(oldPermittedColumnsSource)) {
            oldPermittedClass = oldPermittedColumnsSource.get(0).getClass();
        }
        if (CollectionUtils.isNotEmpty(newApplyColumnsSource)) {
            newPermittedClass = newApplyColumnsSource.get(0).getClass();
        }
        if (oldPermittedClass != newPermittedClass) {
            return Collections.emptyMap();
        }

        List oldPermittedColumns = new ArrayList(oldPermittedColumnsSource);
        List newApplyColumns = new ArrayList(newApplyColumnsSource);
        Map<Object, Object> compareNotChangeStructureMap = new HashMap<>();
        Iterator<?> oldIterator = oldPermittedColumns.iterator();
        ListIterator<?> newIterator = newApplyColumns.listIterator();

        try {
            while (newIterator.hasNext()) {
                Object newPermittedColumn = newIterator.next();
                while (oldIterator.hasNext()) {
                    Object oldPermittedColumn = oldIterator.next();
                    Map<String, List<Object>> stringListMap = compareFieldsIgnore(newPermittedColumn, oldPermittedColumn, ignoFieldNames);
                    if(stringListMap.isEmpty()) {
                        Field keyField = oldPermittedClass.getDeclaredField(keyFieldName);
                        keyField.setAccessible(true);
                        Object oldKey = keyField.get(oldPermittedColumn);
                        Object newKey = keyField.get(newPermittedColumn);

                        compareNotChangeStructureMap.put(oldKey, newKey);
                        oldIterator.remove();
                        break;
                    }
                }
                newIterator.remove();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return compareNotChangeStructureMap;
    }
}
