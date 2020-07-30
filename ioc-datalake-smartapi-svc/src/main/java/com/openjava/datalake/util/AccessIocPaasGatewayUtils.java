package com.openjava.datalake.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.openjava.datalake.common.DeptApiParamConstant;
import com.openjava.datalake.common.gateway.*;
import com.openjava.datalake.component.IocPaasConfig;
import com.openjava.datalake.component.IocPlatformConfig;
import com.openjava.datalake.push.vo.ApiSyncRespVO;
import com.openjava.datalake.util.secret.JwtTokenUtil;
import com.openjava.util.CustomException;
import com.openjava.util.CustomResponseErrorHandler;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @Author xjd
 * @Date 2019/9/9 15:53
 * @Version 1.0
 */
public class AccessIocPaasGatewayUtils {


    private static IocPaasConfig iocPaasConfig;
//    private static HttpHeaders tokenHeaders;
    private static Logger LOG = LoggerFactory.getLogger(GetDeptInfoUtils.class);
    private static IocPlatformConfig iocPlatformConfig;

    private static IocPlatformConfig getIocPlatformConfig(){
        if (iocPlatformConfig == null) {
            try {
                iocPlatformConfig = SpringContextManager.getBean(IocPlatformConfig.class);
            } catch (Exception e) {
            }
            // 单类测试用 静态不影响，最终还是注入的对象
            /*if (iocPlatformConfig == null) {
                iocPlatformConfig = new IocPlatformConfig();
                iocPlatformConfig.setAdminDepartGet("http://219.135.182.2:31075/admin/org/sysOrg/{id}");
                iocPlatformConfig.setAdminDepartRootId("764c858f-42a0-4a2e-8f84-9a5abec2f772");
                iocPlatformConfig.setAdminUserLogin("http://219.135.182.2:31075/admin/user/sysUser/login");
                iocPlatformConfig.setAdminDepartGetChild("http://219.135.182.2:31075/admin/org/sysOrg/doSubset?orgid=");
                iocPlatformConfig.setAdminDepartTopDept("http://219.135.182.2:31075/admin/org/sysOrg/doTopDepartment?id={orgid}");
                iocPlatformConfig.setUserAccount("iocadmin");
                iocPlatformConfig.setUserPwd("iocadmin");
            }*/
        }
        return iocPlatformConfig;
    }

    private static IocPaasConfig getIocPaasConfig(){
        if (iocPaasConfig == null) {
            try {
                iocPaasConfig = SpringContextManager.getBean(IocPaasConfig.class);
            } catch (Exception e) {
            }
            // 单类测试用 静态不影响，最终还是注入的对象
            if (iocPaasConfig == null) {
                iocPaasConfig = new IocPaasConfig();
                iocPaasConfig.setIocPaas("http://219.135.182.2:31012");
                iocPaasConfig.setIocpaasSyncCustomApi("http://219.135.182.2:31012/gateway/apis/spInstanceApi/syncCustomApi");
                iocPaasConfig.setIocpaasCustomApiCredential("http://219.135.182.2:31012/gateway/apis/spInstanceApi/getCustomApiCredential");
                iocPaasConfig.setIocpaasDecodeToken("http://219.135.182.2:31012/certificate/api/spApiCredential/decodeToken");
                iocPaasConfig.setGatewayIpport("http://219.135.182.2:31012/gateway/apis/spInstanceApi/getKongProxy");

            }
        }
        return iocPaasConfig;
    }

    public static Optional<GatewayCredentialResp> getGatewayCredential(long customApiId, long moduleType) {
        String url = getIocPaasConfig().getIocpaasCustomApiCredential();
        ParameterizedTypeReference<GatewayCredentialResp> typeReference = new ParameterizedTypeReference<GatewayCredentialResp>() {};
        MultiValueMap<String, String> paramsKeyValues = new LinkedMultiValueMap<>();
        paramsKeyValues.add("customApiId", String.valueOf(customApiId));
        paramsKeyValues.add("moduleType", String.valueOf(moduleType));
        HttpHeaders tokenHeaders = login();
        GatewayCredentialResp gatewayCredentialResp = RestTemplateUtils.getWithUri(url, tokenHeaders, typeReference, paramsKeyValues);
        Integer code = gatewayCredentialResp.getCode();
        if (!Integer.valueOf(200).equals(code)) {
            String message = gatewayCredentialResp.getMessage();
            gatewayCredentialResp.setMessage(code + "：" + message);
            gatewayCredentialResp.setCode(HttpStatus.BAD_REQUEST.value());
        }
        return Optional.ofNullable(gatewayCredentialResp);
    }

    /**
     * 根据信息资源编码获取获取凭证
     * @Author zmk
     * @param resourceCode
     * @return
     */
    public static Optional<SpDlResJwtResp> getSpDlResJwt(String resourceCode, String authorization, String userId) {
        String url = getIocPaasConfig().getIocpassSpDlResJwt();
        HttpHeaders tokenHeaders = new HttpHeaders();
        if (StringUtils.isNotBlank(authorization)) {
            tokenHeaders.set(DeptApiParamConstant.AUTHORIZATION, authorization);
            url += "?userId="+userId;
        }else {
            tokenHeaders = login();
        }
        HttpEntity requestEntity = new HttpEntity<>(HttpHeaders.readOnlyHttpHeaders(tokenHeaders));
        ResponseEntity<SpDlResJwtResp> response = RestTemplateUtils.getRestTemplate()
                .exchange(url, HttpMethod.GET, requestEntity, SpDlResJwtResp.class, resourceCode);
        SpDlResJwtResp spDlResJwtResp= response.getBody();
        Integer code = spDlResJwtResp.getCode();
        if (!Integer.valueOf(200).equals(code)) {
            String message = spDlResJwtResp.getMessage();
            spDlResJwtResp.setMessage(code + "：" + message);
            spDlResJwtResp.setCode(HttpStatus.BAD_REQUEST.value());
            LOG.info("url:"+url+"/n message:"+message);
        }
        return Optional.ofNullable(spDlResJwtResp);
    }

    public static ApiSyncRespVO pushByUrl(String url,HttpHeaders headers,String json){
        RestTemplate restTemplate = new RestTemplate();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
        ApiSyncRespVO resp = restTemplate.postForEntity(url,formEntity,ApiSyncRespVO.class).getBody();
        return resp;

    }

    public static BasicApiResponse regGatewayApi(GatewayRegApiReq gatewayRegApiReq) {
        String url = getIocPaasConfig().getIocpaasSyncCustomApi();
        ParameterizedTypeReference<BasicApiResponse> typeReference = new ParameterizedTypeReference<BasicApiResponse>() {};
        HttpHeaders tokenHeaders = login();
        RestTemplate restTemplate = RestTemplateUtils.getRestTemplate();
        ResponseErrorHandler errorHandler = restTemplate.getErrorHandler();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        BasicApiResponse basicApiResponse = null;
        try {
            basicApiResponse = RestTemplateUtils.postJsonBody(url, tokenHeaders, typeReference, gatewayRegApiReq);
        } catch (CustomException ce) {
            ce.printStackTrace();
            String errorBody = ce.getBody();
            if (JSONValidator.from(errorBody).validate()) {
                basicApiResponse = JSONObject.parseObject(errorBody, BasicApiResponse.class);
            } else {
                basicApiResponse = new BasicApiResponse(APIConstants.CODE_SERVER_ERR, errorBody);
            }
            return basicApiResponse;
        }  catch (ResourceAccessException e) {
            e.printStackTrace();
            if (basicApiResponse == null) {
                basicApiResponse = new BasicApiResponse();
            }
            basicApiResponse.setCode(APIConstants.CODE_SERVER_ERR);
            basicApiResponse.setMessage("请求网关接口超时");
        } finally {
            restTemplate.setErrorHandler(errorHandler);
        }
        return basicApiResponse;
    }

    public static String getGatewayIpPort() {
        String url = getIocPaasConfig().getGatewayIpport();
        ParameterizedTypeReference<GatewayIpDataResp> typeReference = new ParameterizedTypeReference<GatewayIpDataResp>() {};
        HttpHeaders tokenHeaders = login();
        GatewayIpDataResp gatewayIpDataResp = RestTemplateUtils.get(url, tokenHeaders, typeReference);
        Integer code = gatewayIpDataResp.getCode();
        if (code == 200) {
            String data = gatewayIpDataResp.getData();
            return data;
        }
        return null;
    }


    public static Optional<GatewayUserInfoData> decodeToken(String token) {
        String url = getIocPaasConfig().getIocpaasDecodeToken();
        ParameterizedTypeReference<GatewayUserInfoResp> typeReference = new ParameterizedTypeReference<GatewayUserInfoResp>() {};
        MultiValueMap<String, String> paramsKeyValues = new LinkedMultiValueMap<>();
        paramsKeyValues.add("token", token);
//        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(paramsKeyValues)
//                .build().encode();
//        url = uriComponents.toString();
        // 不需要登陆
//        login();
        GatewayUserInfoResp gatewayUserInfoResp = RestTemplateUtils.getWithUri(url, typeReference, paramsKeyValues);
        GatewayUserInfoData data = gatewayUserInfoResp.getData();
        return Optional.ofNullable(data);
    }


    public static Optional<GatewayUserInfoResp> deleteGatewayApi(Long customApiId, Long moduleType) {
        String url = getIocPaasConfig().getSyncDeleteApi();
        HttpHeaders tokenHeaders = login();
//        HttpEntity<Long> requestEntity = new HttpEntity<>(customApiId, HttpHeaders.readOnlyHttpHeaders(tokenHeaders));
        HttpEntity requestEntity = new HttpEntity<>(HttpHeaders.readOnlyHttpHeaders(tokenHeaders));
        ResponseEntity<GatewayUserInfoResp> response = RestTemplateUtils.getRestTemplate()
                .exchange(url, HttpMethod.DELETE, requestEntity, GatewayUserInfoResp.class, moduleType, customApiId);
        GatewayUserInfoResp data = response.getBody();
        return Optional.ofNullable(data);
    }

    /**
     * API共享到市场
     * @param gatewayPublishReq
     * @return
     * @throws APIException
     */
    public static BaseResp publishGatewayApi(GatewayPublishReq gatewayPublishReq) throws APIException {
        String url = getIocPaasConfig().getIocpassApiPublish();
        HttpHeaders tokenHeaders = login();
        RestTemplate restTemplate = RestTemplateUtils.getRestTemplate();
        ResponseErrorHandler errorHandler = restTemplate.getErrorHandler();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        BaseResp resp = null;
        MultiValueMap<String, Object> reqParams = new LinkedMultiValueMap<>();
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(gatewayPublishReq);
        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            reqParams.add(entry.getKey(), entry.getValue());
        }
        try {
            resp = RestTemplateUtils.postFormData(url, tokenHeaders, reqParams, BaseResp.class);
        } catch (CustomException e) {
            e.printStackTrace();
            String errorbody = e.getBody();
            DataApiResponse result = JSONObject.parseObject(errorbody, DataApiResponse.class);
            throw new APIException(result.getCode(), result.getMessage());
        } finally {
            restTemplate.setErrorHandler(errorHandler);
        }
        return resp;
    }

    /**
     * API下架
     * @param gatewayUnpublishReq
     * @return
     * @throws APIException
     */
    public static BaseResp unpublishGatewayApi(GatewayUnpublishReq gatewayUnpublishReq) throws APIException {
        String url = getIocPaasConfig().getIocpassApiUnpublish();
        HttpHeaders tokenHeaders = login();
        RestTemplate restTemplate = RestTemplateUtils.getRestTemplate();
        ResponseErrorHandler errorHandler = restTemplate.getErrorHandler();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        BaseResp resp = null;
        MultiValueMap<String, Object> reqParams = new LinkedMultiValueMap<>();
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(gatewayUnpublishReq);
        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            reqParams.add(entry.getKey(), entry.getValue());
        }
        try {
            resp = RestTemplateUtils.postFormData(url, tokenHeaders, reqParams, BaseResp.class);
        } catch (CustomException e) {
            e.printStackTrace();
            String errorbody = e.getBody();
            DataApiResponse result = JSONObject.parseObject(errorbody, DataApiResponse.class);
            throw new APIException(result.getCode(), result.getMessage());
        } finally {
            restTemplate.setErrorHandler(errorHandler);
        }
        return resp;
    }

    public static BasicApiResponse updateGatewayApiDeptInfo(GatewayDeptInfo gatewayDeptInfo) {
        String url = getIocPaasConfig().getGatewaySyncCustomApiDept();
        ParameterizedTypeReference<BasicApiResponse> typeReference = new ParameterizedTypeReference<BasicApiResponse>() {};
        HttpHeaders tokenHeaders = login();
        RestTemplate restTemplate = RestTemplateUtils.getRestTemplate();

        ResponseErrorHandler errorHandler = restTemplate.getErrorHandler();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return errorHandler.hasError(clientHttpResponse);
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
                // 队请求头的处理
                List<String> customHeader = clientHttpResponse.getHeaders().get("x-app-err-id");

                String svcErrorMessageID = "";
                if (customHeader != null) {
                    svcErrorMessageID = customHeader.get(0);
                }
                //对body 的处理 (inputStream)
                String body = convertStreamToString(clientHttpResponse.getBody());

                try {

                    errorHandler.handleError(clientHttpResponse);

                } catch (RestClientException scx) {

                    throw new CustomException(scx.getMessage(), scx, body);
                }
            }
            // inputStream 装换为 string
            private String convertStreamToString(InputStream is) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();

                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return sb.toString();
            }
        });

        BasicApiResponse basicApiResponse = null;
        try {
            basicApiResponse = RestTemplateUtils.postJsonBody(url, tokenHeaders, typeReference, gatewayDeptInfo);
        }  catch (ResourceAccessException re) {
            re.printStackTrace();
            if (basicApiResponse == null) {
                basicApiResponse = new BasicApiResponse();
            }
            basicApiResponse.setCode(APIConstants.CODE_SERVER_ERR);
            basicApiResponse.setMessage("请求网关接口超时");
        } finally {
            RestTemplateUtils.getRestTemplate().setErrorHandler(errorHandler);
        }
        return basicApiResponse;
    }

    public static HttpHeaders login(){
        BaseUserInfo baseUserInfo = (BaseUserInfo) SsoContext.getUser();
        String userAgent;
        String token;
        if (baseUserInfo != null) {
            userAgent = baseUserInfo.getUserAgent();
            token = SsoContext.getToken();
        } else {
//            String userAccount = getIocPlatformConfig().getUserAccount();
//            String userPwd = getIocPlatformConfig().getUserPwd();
//            String url = getIocPlatformConfig().getAdminUserLogin();
//            ParameterizedTypeReference<LoginResp> parameterizedTypeReference = new ParameterizedTypeReference<LoginResp>(){};
//            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//            params.add ("userAccount", userAccount);
//            params.add("userPwd", userPwd);
//            LoginResp post = null;
//            try {
//                post = RestTemplateUtils.postFormData(url, parameterizedTypeReference, params);
//                if (post != null) {
//                    UserVO user = post.getUser();
//                    userAgent = user.getUserAgent();
//                    token = user.getTokenId();
//                } else {
//                    userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36";
//                    token = "wKQ3VOIXKbSQvMhz4N7Y8kfTvxGdv3S4SImPzVDFHTc";
//                }
//            } catch (Exception e) {
//                userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36";
//                token = "wKQ3VOIXKbSQvMhz4N7Y8kfTvxGdv3S4SImPzVDFHTc";
//            }
            // todo 生产环境redis端口申请成功后删除
            throw new AssertionError("请登录后再操作");
        }

        HttpHeaders  tokenHeaders = new HttpHeaders();
        tokenHeaders.set(DeptApiParamConstant.AUTHORITY_TOKEN, token);
        tokenHeaders.set(DeptApiParamConstant.USER_AGENT, userAgent);
        return tokenHeaders;
    }

    public static void main(String[] args) {
        String url = "http://localhost:8080/1/datalake/apisync/resource/DGS_DGSQKK_HJ_0036244";
        String json = "{\"beginTimestamp\":\"1578105333031\",\"column\":[\"xh\",\"xm\",\"rxsj\"],\"data\":[[\"41\",\"0xm姓名\",\"2020-01-03 10:35:33\"]],\"endTImestamp\":\"1578105333031\",\"pushSequence\":\"1001\",\"audit_info\":\"{\\\"item_code\\\":\\\"123\\\",\\\"new\\\":true,\\\"item_id\\\":\\\"123\\\",\\\"terminal_info\\\":\\\"123\\\",\\\"item_sequence\\\":\\\"123\\\"}\",\"sign\":\"2DC7040FA1E1008AD85F6E13D618EAEAC4D748F10BD0C1B4C4976EB7FE43C100\"}";
        String token = JwtTokenUtil.generateToken("ivHcIP6EjyzFljhG","QseuK8qsR6QXZ8x3amTflQ9SgwolRksa");
        HttpHeaders headers = new HttpHeaders();
        //ivHcIP6EjyzFljhG
        //QseuK8qsR6QXZ8x3amTflQ9SgwolRksa
        headers.add("Authorization","Bearer "+token);
        AccessIocPaasGatewayUtils.pushByUrl(url,headers,json);
    }

}
