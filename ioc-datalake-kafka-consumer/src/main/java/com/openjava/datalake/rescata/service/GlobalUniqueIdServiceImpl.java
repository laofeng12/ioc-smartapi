package com.openjava.datalake.rescata.service;

import com.openjava.datalake.common.restTemplateFactory.HttpsClientRequestFactory;
import com.openjava.datalake.util.JwtTokenUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @Author JiaHai
 * @Description 获取全局唯一ID 业务层接口实现类
 */
@Slf4j
@Service
@ConfigurationProperties(prefix = "datalake.sequence")
@Getter
@Setter
public class GlobalUniqueIdServiceImpl implements GlobalUniqueIdService {
    /**
     * 是否开启安全模式
     */
    private Boolean securityMode;

    /**
     * key
     */
    private String key;

    /**
     * secret
     */
    private String secret;

    /**
     * 全局ID服务URL
     */
    private String globalUniqueId;

    /**
     * 全局ID服务URL（安全模式）
     */
    private String globalUniqueIdOnSecurity;

    /**
     * Authorization
     */
    private static final String AUTHORITY_TOKEN = "Authorization";

    /**
     * 持票人（与token结合）
     */
    private static final String BEARER = "Bearer ";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Long getGlobalUniqueIdById(Long sequenceId) throws APIException {
        // 声明全局唯一ID
        Long globalUniqueId;

        if (this.securityMode != null && this.securityMode) {
            // 安全模式
            log.info("进入 安全模式 - - - - -");
            // 参数非空校验
            this.checkSecurityMode(this.key, this.secret, this.globalUniqueIdOnSecurity);
            // 声明并初始化请求头
            HttpHeaders httpHeaders = new HttpHeaders();
            // 生成token
            String token = JwtTokenUtils.generateToken(this.key, this.secret);
            log.info("token 为： " + token);
            // 添加请求头
            httpHeaders.add(AUTHORITY_TOKEN, BEARER + token);
            // 远程调用
            ResponseEntity<Long> responseEntity;
            try {
                RestTemplate myRestTemplate;
                if (this.globalUniqueIdOnSecurity.matches("^(https://).*")) {
                    // 改用https
                    myRestTemplate = new RestTemplate(new HttpsClientRequestFactory());
                } else {
                    myRestTemplate = restTemplate;
                }
                log.info("调用的URL为： " + (this.globalUniqueIdOnSecurity + sequenceId));
                responseEntity = myRestTemplate.exchange(this.globalUniqueIdOnSecurity + sequenceId, HttpMethod.GET, new HttpEntity(httpHeaders), Long.class);
            } catch (RestClientException e) {
                e.printStackTrace();
                throw new APIException(APIConstants.CODE_FAILD, "获取全局唯一ID失败。。。");
            }
            // 赋值
            globalUniqueId = responseEntity.getBody();
        } else {
            // 非安全模式
            log.info("进入 非安全模式 - - - - -");
            if (StringUtils.isBlank(this.globalUniqueId)) {
                throw new APIException(APIConstants.PARAMS_NOT_Valid, "全局ID服务URL不可为空");
            }
            try {
                // 远程调用 && 赋值
                log.info("调用的URL为： " + (this.globalUniqueId + sequenceId));
                globalUniqueId = restTemplate.getForObject(this.globalUniqueId + sequenceId, Long.class);
            } catch (HttpClientErrorException e) {
                log.error("进入HttpClientErrorException");
                e.getResponseBodyAsString();
                throw new APIException(APIConstants.CODE_FAILD, "获取全局唯一ID失败。。。");
            } catch (RestClientException e) {
                e.printStackTrace();
                throw new APIException(APIConstants.CODE_FAILD, "获取全局唯一ID失败。。。");
            }
        }

        // 返回全局唯一ID
        return globalUniqueId;
    }

    /**
     * 全局ID服务参数非空校验（安全模式）
     *
     * @param key
     * @param secret
     * @param globalUniqueIdOnSecurity
     * @throws APIException
     */
    private void checkSecurityMode(String key, String secret, String globalUniqueIdOnSecurity) throws APIException {
        if (StringUtils.isBlank(key)) {
            throw new APIException(APIConstants.PARAMS_NOT_Valid, "安全模式下，key不可为空");
        }
        if (StringUtils.isBlank(secret)) {
            throw new APIException(APIConstants.PARAMS_NOT_Valid, "安全模式下，secret不可为空");
        }
        if (StringUtils.isBlank(globalUniqueIdOnSecurity)) {
            throw new APIException(APIConstants.PARAMS_NOT_Valid, "安全模式下，需要配置全局ID服务 安全URL");
        }
    }
}
