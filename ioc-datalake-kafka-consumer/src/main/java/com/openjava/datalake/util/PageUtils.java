package com.openjava.datalake.util;

import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;

/**
 * @Author JiaHai
 * @Description 分页参数工具类
 */
public class PageUtils {
    /**
     * 校验分页参数是否合法
     *
     * @param page 页数
     * @param size 每页条数
     * @throws APIException
     */
    public static void checkParam(Integer page, Integer size) throws APIException {
        if (null != page && page < 0) {
            throw new APIException(APIConstants.CODE_PARAM_ERR, "页数 需要为自然数");
        }
        if (null != size && size < 1) {
            throw new APIException(APIConstants.CODE_PARAM_ERR, "每页条数 需要为正整数");
        }
    }
}
