package com.openjava.datalake.component;


import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.common.gateway.GatewayUserInfoData;
import com.openjava.datalake.util.AccessIocPaasGatewayUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.exception.APIException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class GateWayUtil {

    private static RedisTemplate redisTemplate;

    public static synchronized RedisTemplate getRestTemplate() {
        if (redisTemplate == null) {
            synchronized (GateWayUtil.class) {
                if (redisTemplate == null) {
                    try {
                        redisTemplate = (RedisTemplate) SpringContextManager.getBean("redisTemplate");
                    } catch (Exception e) {
                    }
                }
            }
        }
        return redisTemplate;
    }

    /**
     * 从网关获取请求身份
     * @param request
     * @return
     */
    public static OaUserVO decodeUserInfo(HttpServletRequest request) throws APIException {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isBlank(authorization)) {
            throw new APIException(HttpStatus.UNAUTHORIZED.value(),
                            "Authorization 不正确");
        }
        String token = authorization;
        GatewayUserInfoData gatewayUserInfoData = (GatewayUserInfoData) MemoryCache.getData(PublicConstant.GAT_WAY_USERINFO_DATA,token);
        if(gatewayUserInfoData == null) {
            System.out.println("---------------去网关获取用户信息-----------------");
            Optional<GatewayUserInfoData> optional = AccessIocPaasGatewayUtils.decodeToken(token);
            if (!optional.isPresent()) {
                throw new APIException(HttpStatus.UNAUTHORIZED.value(),
                        "Authorization 不正确");
            }
            gatewayUserInfoData = optional.get();
            MemoryCache.putData(PublicConstant.GAT_WAY_USERINFO_DATA,token,gatewayUserInfoData);
            System.out.println("-----------网关获取到的用户信息设置到jvm----------------");
        }
        OaUserVO oaUserVO = new OaUserVO();
        if (gatewayUserInfoData != null) {
            String account = gatewayUserInfoData.getAccount();
            String userName = gatewayUserInfoData.getFullName();
            String userId = gatewayUserInfoData.getUserId();
            oaUserVO.setUserAccount(account);
            oaUserVO.setUserName(userName);
            oaUserVO.setUserId(userId);
        } else {
            throw new APIException(HttpStatus.UNAUTHORIZED.value(),
                    "没有权限调用该API，未登录");
        }
        return oaUserVO;
    }
}
