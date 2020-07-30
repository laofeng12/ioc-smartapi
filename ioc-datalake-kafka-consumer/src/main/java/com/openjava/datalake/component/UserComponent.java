package com.openjava.datalake.component;

import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datalake.common.PublicConstant;
import org.ljdp.component.exception.APIException;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.stereotype.Component;

/**
 * @author: lsw
 * @Date: 2020/1/9 10:38
 */
@Component
public class UserComponent {

    /** 提示语 */
    private static final String NO_LOGIN_MESSAGE = "获取用户信息失败";

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     * @throws APIException 异常
     */
    public OaUserVO getCurrentLoginUserInfo() throws APIException {
        OaUserVO user = (OaUserVO) SsoContext.getUser();
        if (user == null) {
            throw new APIException(PublicConstant.EXCEPTION_ERROR, NO_LOGIN_MESSAGE);
        }

        return user;
    }
}
