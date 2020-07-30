package com.openjava.datalake.util;

import com.openjava.datalake.common.PublicConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.ljdp.component.exception.APIException;

import java.util.Collection;

/**
 * 参数校验工具
 *
 * @author: lsw
 * @Date: 2019/8/19 9:43
 */
public class ValidateUtil {
    /**
     * 校验真
     *
     * @param expression 表达式
     * @param message    错误提示信息
     */
    public static void isTrue(boolean expression, String message) throws APIException {
        try {
            //校验是否为真
            Validate.isTrue(expression, message);
        } catch (IllegalArgumentException e) {
            //抛出异常
            throw new APIException(PublicConstant.EXCEPTION_ERROR, message);
        }
    }

    /**
     * 校验不为null
     *
     * @param object  对象
     * @param message 错误提示信息
     * @param <T>     泛型
     * @return        泛型
     */
    public static <T> T notNull(T object, String message) throws APIException {
        try {
            //校验是否为null
            return Validate.notNull(object, message);
        } catch (NullPointerException e) {
            //抛出异常
            throw new APIException(PublicConstant.EXCEPTION_ERROR, message);
        }
    }

    /**
     * 校验集合不为空
     *
     * @param collection 集合
     * @param message    错误提示信息
     * @param <T>        泛型
     * @return           集合
     */
    public static <T extends Collection<?>> T notEmpty(T collection, String message) throws APIException {
        try {
            //校验是否为空
            return Validate.notEmpty(collection, message);
        } catch (NullPointerException | IllegalArgumentException e) {
            //抛出异常
            throw new APIException(PublicConstant.EXCEPTION_ERROR, message);
        }
    }

    /**
     * 校验字符串不能为null且不能为""
     *
     * @param str     待检测字符串
     * @param message 提示信息
     */
    public static void strNotBlank(String str, String message) throws APIException {
        //校验是否为空字符串
        if (StringUtils.isBlank(str)) {
            //抛出异常
            throw new APIException(PublicConstant.EXCEPTION_ERROR, message);
        }
    }
}
