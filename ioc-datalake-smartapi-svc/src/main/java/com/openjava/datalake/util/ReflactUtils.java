package com.openjava.datalake.util;

import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.insensitives.domain.DlInsensitivesRule;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author xjd
 * @Date 2019/7/3 18:08
 * @Version 1.0
 */
public class ReflactUtils {

    public static String getJavaType( Field field ) {
        Type genericType = field.getGenericType();
        String dataType;
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type rawType = parameterizedType.getRawType();
            String[] split = rawType.getTypeName().split("\\.");
            dataType = split[split.length - 1];
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            dataType = dataType + "<";
            String[] actualTypeArgumentNames = new String[actualTypeArguments.length];
            for (int j = 0; j < actualTypeArguments.length; j++) {
                Type actualTypeArgument = actualTypeArguments[j];
                String[] TypeArgumentSplit = actualTypeArgument.getTypeName().split("\\.");
                actualTypeArgumentNames[j] = TypeArgumentSplit[TypeArgumentSplit.length - 1];
            }
            dataType = dataType + StringUtils.join(actualTypeArgumentNames, ", ");
            dataType = dataType + ">";

        } else {
            String[] split = genericType.getTypeName().split("\\.");
            dataType = split[split.length - 1];
        }
        return dataType;
    }

    public static List<Field> getAllNeedShowFieldList(Class<?> clazz){
        if(null == clazz){
            return null;
        }
        List<Field> fieldList = new LinkedList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            /** 过滤静态属性**/
            if(Modifier.isStatic(field.getModifiers())){
                continue;
            }
            /** 过滤transient 关键字修饰的属性**/
            if(Modifier.isTransient(field.getModifiers())){
                continue;
            }
            // 过滤 hidden 的字段
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            if (annotation != null && annotation.hidden()) {
                continue;
            }
            fieldList.add(field);
        }
        /** 处理父类字段**/
        Class<?> superClass = clazz.getSuperclass();
        if (superClass.equals(Object.class)){
            return fieldList;
        }
        List<Field> superFieldList = getAllNeedShowFieldList(superClass);
        fieldList.addAll(superFieldList);
        return fieldList;
    }

    public static Object invokeInsensitivesMethod(String className, String methodName, Object[] params) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class<?> clazz = Class.forName(className);
        Class[] paramsClass = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            paramsClass[i] = params[i].getClass();
        }
        Method declaredMethod = clazz.getDeclaredMethod(methodName, paramsClass);
        Object returnValue = declaredMethod.invoke(clazz, params);
        return returnValue;
    }

    /**
     * 传参有错就返回一个空的，保证数据安全
     * @param ruleInfo
     * @param columnValue
     * @return
     */
    public static Object insensitiveColumn(DlInsensitivesRule ruleInfo, String columnValue){
        Long ruleType = ruleInfo.getRuleType();
        String ruleClassPath = ruleInfo.getRuleClassPath();
        String ruleMethodName = ruleInfo.getRuleMethodName();
        String[] params;
        try {
            if (PublicConstant.INSENSITIVES_RULE_CHAR_SET.equals(ruleType)) {
                params = new String[]{columnValue};
            } else {
                params = columnValue.split(",");
            }
            return invokeInsensitivesMethod(ruleClassPath, ruleMethodName, params);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {


        String columnValue = "a";
        String ruleParamsValue = "b,c";
        String join = StringUtils.join(columnValue, ",", ruleParamsValue);
        System.out.println(join);
        String className = "com.openjava.dl.util.InsensitivesUtils";
        String methodName = "protectMiddle";
        String parms = "protectIdCard,4,10";
        String[] split = parms.split(",");
        long startIndex = 4;
        long endIndex = 10;
        try {
            Object protectIdCard = invokeInsensitivesMethod(className, methodName, split);
            System.out.println(protectIdCard);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
