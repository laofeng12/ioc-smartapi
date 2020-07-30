package com.openjava.datalake.util;

import com.openjava.admin.user.vo.OaUserVO;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

/**
 * @Author JiaHai
 * @Description 登录相关工具类
 */
public class LoginUtils {
    /**
     * authority-token
     */
    public static final String AUTHORITY_TOKEN = "authority-token";
    /**
     * user-agent
     */
    public static final String USER_AGENT = "user-agent";

    /**
     * 获取 登录后的 请求头
     *
     * @return
     */
    public static HttpEntity getLoginHttpHeaders() {
        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORITY_TOKEN, SsoContext.getToken());
        if (null != SsoContext.getUser()) {
            httpHeaders.add(USER_AGENT, ((BaseUserInfo) SsoContext.getUser()).getUserAgent());
        }
        return new HttpEntity(httpHeaders);
    }

    /**
     * 校验 是否已登录
     *
     * @throws APIException
     */
    public static void validateLogin() throws APIException {
        OaUserVO oaUserVO = (OaUserVO) SsoContext.getUser();
        if (null == oaUserVO) {
            throw new APIException(APIConstants.ACCOUNT_NO_LOGIN, "请登录");
        }
    }
}