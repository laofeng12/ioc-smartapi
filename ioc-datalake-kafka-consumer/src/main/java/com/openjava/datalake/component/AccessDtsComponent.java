package com.openjava.datalake.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.openjava.admin.component.IocAuthorizationToken;
import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datalake.common.DeptApiParamConstant;
import com.openjava.datalake.common.responseBody.DtsDataSourceConnInfoResp;
import com.openjava.datalake.util.RestTemplateUtils;
import com.openjava.util.CustomException;
import com.openjava.util.CustomResponseErrorHandler;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.openjava.boot.conf.DtsIntegrationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author xjd
 * @Date 2020/3/10 17:12
 * @Version 1.0
 */
@Component
public class AccessDtsComponent {
    private static Logger LOG = LoggerFactory.getLogger(AccessDtsComponent.class);

    @Resource
    private DtsIntegrationConfig dtsIntegrationConfig;
    @Resource
    private IocAuthorizationToken iocAuthorizationToken;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private BasicTextEncryptor basicTextEncryptor;

    private HttpHeaders login(OaUserVO oaUserVO) throws APIException {
        BaseUserInfo baseUserInfo = (BaseUserInfo) SsoContext.getUser();
        HttpHeaders  tokenHeaders = new HttpHeaders();
        String userAgent;
        String token;
        if (baseUserInfo != null) {
            userAgent = baseUserInfo.getUserAgent();
            token = SsoContext.getToken();
            System.out.println(DeptApiParamConstant.AUTHORITY_TOKEN + ":" + token);
            tokenHeaders.set(DeptApiParamConstant.AUTHORITY_TOKEN, token);
            tokenHeaders.set(DeptApiParamConstant.USER_AGENT, userAgent);
        } else {
            tokenHeaders = getIocAuthorizationHeaders(oaUserVO);
//            try {
//                String authorization = iocAuthorizationToken.generateAesToken(oaUserVO);
//                System.out.println(DeptApiParamConstant.AUTHORIZATION + ":" + authorization);
//                tokenHeaders.set(DeptApiParamConstant.AUTHORIZATION, authorization);
//                tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new APIException(APIConstants.CODE_SERVER_ERR, e.getMessage());
//            }
        }

        return tokenHeaders;
    }

    public DtsDataSourceConnInfoResp getDataSourceConnInfo(String dataSourceId, OaUserVO oaUserVO) throws APIException {
        final String catchKey = "dtsDateSourceConnInfo" + dataSourceId;
        DtsDataSourceConnInfoResp dtsDataSourceConnInfoResp;
        String respEncrypJsonString = null;
        respEncrypJsonString = redisTemplate.opsForValue().get(catchKey);
        if (StringUtils.isBlank(respEncrypJsonString)) {
            // 从DTS服务取
            // 带 路径参数 {id}
            String uri = dtsIntegrationConfig.getGetDataSourceConnInfo();
            HttpHeaders httpHeaders = getIocAuthorizationHeaders(oaUserVO);

            Map<String, Object> uriVarilabes = new HashMap<>();
            uriVarilabes.put(DeptApiParamConstant.PATH_PARAM_DTS_DATASOURCE_GET_ID, dataSourceId);
            RestTemplate restTemplate = RestTemplateUtils.getRestTemplate();
            ResponseErrorHandler errorHandler = restTemplate.getErrorHandler();
            restTemplate.setErrorHandler(new CustomResponseErrorHandler());
            try {
                respEncrypJsonString = RestTemplateUtils.getWithUri(uri, httpHeaders, String.class, uriVarilabes);
//            String url = basicTextEncryptor.decrypt(dtsDataSourceConnInfoResp.getUrl());
//            String userName = basicTextEncryptor.decrypt(dtsDataSourceConnInfoResp.getUsername());
//            String password = basicTextEncryptor.decrypt(dtsDataSourceConnInfoResp.getPassword());
//            dtsDataSourceConnInfoResp.setUrl(url)
//                    .setUsername(userName)
//                    .setPassword(password);
            } catch (ResourceAccessException re) {
                String message = "请求超时";
                throw new APIException(HttpStatus.REQUEST_TIMEOUT.value(), String.format("内部错误 调用DTS接口异常:[%s]", message));
            } catch (CustomException ce) {
                ce.printStackTrace();
                String errorBody = ce.getBody();
                if (JSONValidator.from(errorBody).validate()) {
                    DataApiResponse result = JSONObject.parseObject(errorBody, DataApiResponse.class);
                    throw new APIException(result.getCode() == null ? APIConstants.CODE_SERVER_ERR : result.getCode(),
                            String.format("DTS异常：[%s]", result.getMessage()));
                } else {
                    throw new APIException(APIConstants.CODE_SERVER_ERR, errorBody);
                }
            } finally {
                restTemplate.setErrorHandler(errorHandler);
            }
            // 缓存在redis，一小时有效
            redisTemplate.opsForValue().set(catchKey, respEncrypJsonString, Duration.ofHours(1));
        }

        String decrypt = basicTextEncryptor.decrypt(respEncrypJsonString);
        dtsDataSourceConnInfoResp = JSON.parseObject(decrypt, DtsDataSourceConnInfoResp.class);
        if (dtsDataSourceConnInfoResp == null) {
            throw new APIException(APIConstants.CODE_FAILD, "获取DTS数据源链接信息失效null");
        }
        return dtsDataSourceConnInfoResp;
    }

    public HttpHeaders getIocAuthorizationHeaders(OaUserVO oaUserVO) throws APIException {
        HttpHeaders httpHeaders = new HttpHeaders();
        String authorization = null;
        try {
            authorization = iocAuthorizationToken.generateAesToken(oaUserVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR, e.getMessage());
        }
        System.out.println(DeptApiParamConstant.AUTHORIZATION + ":" + authorization);
        httpHeaders.set(DeptApiParamConstant.AUTHORIZATION, authorization);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }

}
