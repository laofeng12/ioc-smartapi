package com.openjava.datalake.util;

import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datalake.common.PublicConstant;
import org.ljdp.plugin.sys.vo.UserVO;
import org.ljdp.secure.sso.SsoContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 初始化create/update/state
 * @author xjd
 */
public class InitDoSave {


    private static Logger logger = LoggerFactory.getLogger(InitDoSave.class);

    public static void InitCreate(Object bean) {
        UserVO user = (UserVO) SsoContext.getUser();
        if (user == null) {
//            throw new APIException(APIConstants.ACCOUNT_NO_LOGIN, "请登录后操作");
            return;
        }
        String userId = user.getUserId();
        String userName = user.getUserName();
        String userAccount = user.getUserAccount();
        Date now = new Date();
        Class<?> clazz = bean.getClass();

        Field[] declaredFields = clazz.getDeclaredFields();
        try {
            for (Field field : declaredFields){
                field.setAccessible(true);
                if("createFullname".equals(field.getName())){
                    field.set(bean, userName);
                }
                if("createId".equals(field.getName())){
                    field.set(bean, userId);
                }
                if("createTime".equals(field.getName())){
                    field.set(bean, now);
                }
                if("createAccount".equals(field.getName())){
                    field.set(bean, userAccount);
                }
                if("state".equals(field.getName())){
                    field.set(bean, PublicConstant.YES);
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void InitModify(Object bean) {
        UserVO user = (UserVO) SsoContext.getUser();
        if (user == null) {
//            throw new APIException(APIConstants.ACCOUNT_NO_LOGIN, "请登录后操作");
            return;
        }
        String userId = user.getUserId();
        String userName = user.getUserName();
        String userAccount = user.getUserAccount();
        Date now = new Date();
        Class<?> clazz = bean.getClass();

        Field[] declaredFields = clazz.getDeclaredFields();
        try {
            for (Field field : declaredFields){
                field.setAccessible(true);
                if("updateFullname".equals(field.getName())){
                    field.set(bean, userName);
                }
                if("updateId".equals(field.getName())){
                    field.set(bean, userId);
                }
                if("updateTime".equals(field.getName())){
                    field.set(bean, now);
                }
                if("updateAccount".equals(field.getName())){
                    field.set(bean, userAccount);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理数据
     * @param entity
     */
    public static void dealModel(Object entity) {
        if (entity == null) {
            return;
        }
        /*if (!(entity instanceof Persistable)) {// BasicApiResponse or Persistable
            return;
        }*/
         // map 大小根据需要初始化的字段来设置2的倍数
        Map<String, Object> createValue = new HashMap<>(8);
        Map<String, Object> updateValue = new HashMap<>(8);

        OaUserVO user = (OaUserVO) SsoContext.getUser();
        Long userId = null;
        String userName = "";
        String userAccount = "";
        Date now = new Date();
        if (user != null) {
            try{
                userId = Long.valueOf(user.getUserId());
            }catch (NumberFormatException e){
                logger.info("userId:[{}]为String， 不对createId，updateId初始化", user.getUserId());
            }
            userName = user.getUserName();
            userAccount = user.getUserAccount();
            if (userId != null) {
                createValue.put("createId", userId);
                updateValue.put("updateId", userId);
            }
            createValue.put("createFullname", userName);
            createValue.put("createAccount", userAccount);
            updateValue.put("updateFullname", userName);
            updateValue.put("updateAccount", userAccount);
        }
//        createValue.put("state", PublicConstant.YES);
        createValue.put("createTime", now);
        updateValue.put("updateTime", now);

        Class<?> clazz = entity.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        Field isNewField;
        Boolean isNew;

        try {
            isNewField = clazz.getDeclaredField("isNew");
            isNewField.setAccessible(true);
            isNew = (Boolean) isNewField.get(entity);

            for (Field field : declaredFields){
                Object o;
                if(isNew == null || isNew){
                    o = createValue.get(field.getName());
                    if (o == null) {
                        o = updateValue.get(field.getName());
                    }
                }else{
                    o = updateValue.get(field.getName());
                }
                if (o != null) {
                    field.setAccessible(true);
                    field.set(entity, o);
                }
            }
        } catch (NoSuchFieldException e) {
            logger.info("entity中不带isNew属性,dealModel方法调用错误");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
