package com.openjava.datalake.util;

import com.openjava.datalake.common.DeptApiParamConstant;
import com.openjava.datalake.common.responseBody.*;
import com.openjava.datalake.component.IocPlatformConfig;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.plugin.sys.vo.UserVO;
import org.ljdp.secure.sso.SsoContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * @Author xjd
 * @Date 2019/7/17 19:40
 * @Version 1.0
 */
public class GetDeptInfoUtils {

    private static IocPlatformConfig iocPlatformConfig;
    private static HttpHeaders tokenHeaders;
    private static Logger LOG = LoggerFactory.getLogger(GetDeptInfoUtils.class);


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
                iocPlatformConfig.setOrgtypeGetDepart("http://219.135.182.2:31075/admin/org/sysOrg/search?eq_orgtype={eq_orgtype}&page={page}&size={size}");
                iocPlatformConfig.setOrgRoleGetUsers("http://219.135.182.2:31075/admin/user/sysUser/searchUsersByOrgAndRole?orgid={orgid}&deptcode={deptcode}&roleAlias={roleAlias}");
                iocPlatformConfig.setUserAccount("iocadmin");
                iocPlatformConfig.setUserPwd("iocadmin");
            }*/
        }
        return iocPlatformConfig;
    }



    public static String getDeptRoute(String deptId){
        if (StringUtils.isBlank(deptId)) {
            return null;
        }
        DeptInfo body = null;
        String orgsupid = deptId;
        String deptIdRoute = deptId;
        String adminDepartRootId = getIocPlatformConfig().getAdminDepartRootId();
        do {
            if (!adminDepartRootId.equals(deptId)){
                Optional<DeptInfo> deptInfo = httpGetDeptInfo(orgsupid);
                if (deptInfo.isPresent()) {
                    body = deptInfo.get();
                    orgsupid = body.getOrgsupid();
                    deptIdRoute = orgsupid + "." + deptIdRoute;
                } else {
                    return null;
                }
            }
        } while (!adminDepartRootId.equals(orgsupid) && body != null);
        return deptIdRoute;
    }

    public static void testAccessLogin(){
        BaseUserInfo baseUserInfo = (BaseUserInfo) SsoContext.getUser();
        String userAgent;
        String token;
        if (baseUserInfo != null) {
            userAgent = baseUserInfo.getUserAgent();
            token = SsoContext.getToken();
        } else {
            String userAccount = getIocPlatformConfig().getUserAccount();
            String userPwd = getIocPlatformConfig().getUserPwd();
            String url = getIocPlatformConfig().getAdminUserLogin();
            ParameterizedTypeReference<LoginResp> parameterizedTypeReference = new ParameterizedTypeReference<LoginResp>(){};
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add ("userAccount", userAccount);
            params.add("userPwd", userPwd);
            LoginResp post = RestTemplateUtils.postFormData(url, parameterizedTypeReference, params);
            UserVO user = post.getUser();
            userAgent = user.getUserAgent();
            token = user.getTokenId();
        }

        tokenHeaders = new HttpHeaders();
        tokenHeaders.set(DeptApiParamConstant.AUTHORITY_TOKEN, token);
        tokenHeaders.set(DeptApiParamConstant.USER_AGENT, userAgent);
    }

    private static HttpHeaders getTokenHeaders(){
        return tokenHeaders;
    }
    private static void setTokenHeaders(String token, String userAgent){
        tokenHeaders = new HttpHeaders();
        tokenHeaders.set(DeptApiParamConstant.AUTHORITY_TOKEN, token);
        tokenHeaders.set(DeptApiParamConstant.USER_AGENT, userAgent);
    }

    public static Optional<DeptInfo> httpGetDeptInfo(String deptId){
        if (StringUtils.isBlank(deptId)) {
            return Optional.ofNullable(null);
        }
        String url = getIocPlatformConfig().getAdminDepartGet();
        Integer code = null;
        DeptInfo deptInfo = null;
        ParameterizedTypeReference<DeptInfo> deptInfoParameterizedTypeReference = new ParameterizedTypeReference<DeptInfo>() {};
        Map<String, String> pathParams = new HashMap<>(1);
        pathParams.put(DeptApiParamConstant.PATH_PARAM_ADMIN_DEPART_GET_ID, deptId);

        try {
            // 先登录 获取认证信息到tokenHeaders
            testAccessLogin();
            deptInfo = RestTemplateUtils.get(url, tokenHeaders, deptInfoParameterizedTypeReference, pathParams);
        } catch (Exception e) {
            LOG.error("连接公共服务平台错误，httpGetDeptInfo({})", deptId);
            e.printStackTrace();
            return Optional.ofNullable(deptInfo);
        }
        code = deptInfo.getCode();
        if (!Integer.valueOf(APIConstants.CODE_SUCCESS).equals(code)) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(deptInfo);
    }

    public static Optional<List<OrgVO>> httpGetDepChild(String deptId){
        String url = getIocPlatformConfig().getAdminDepartGetChild() + deptId;
        ParameterizedTypeReference<OrgResp> orgRespParameterizedTypeReference = new ParameterizedTypeReference<OrgResp>() {};
//        Map<String, String> uriValiables = new HashMap<>();
//        uriValiables.put(DeptApiParamConstant.URI_VARIABLES_ADMIN_DEPART_SUBSET_ORGID, deptId);
        testAccessLogin();
//        OrgResp orgResp = RestTemplateUtils.get(url, httpEntity, orgRespParameterizedTypeReference, uriValiables);
        OrgResp orgResp = null;
        try {
            orgResp = RestTemplateUtils.get(url, tokenHeaders, orgRespParameterizedTypeReference);
        } catch (Exception e) {
            LOG.error("连接公共服务平台错误，httpGetDeptInfo({})", deptId);
            e.printStackTrace();
            return Optional.ofNullable(null);
        }
        Integer code = orgResp.getCode();
        if (!Integer.valueOf(APIConstants.CODE_SUCCESS).equals(code)) {
            return Optional.ofNullable(null);
        }
        List<OrgVO> resources = orgResp.getResources();
        return Optional.ofNullable(resources);
    }

    public static Optional<DeptResp> httpGetAllDepartByOrgtype(String orgtype, int page, int size){
        if (orgtype == null) {
            return Optional.ofNullable(null);
        }
        String url = getIocPlatformConfig().getOrgtypeGetDepart();
        testAccessLogin();
        ParameterizedTypeReference<DeptResp> deptInfoParameterizedTypeReference = new ParameterizedTypeReference<DeptResp>() {};
        Map<String, Object> uriVarilabes = new HashMap<>();
        uriVarilabes.put(DeptApiParamConstant.REQUEST_PARAM_DEPART_ORGTYPE, orgtype);
        uriVarilabes.put("page", page);
        uriVarilabes.put("size", size);
        DeptResp deptResp = null;
        try {
            deptResp = RestTemplateUtils.get(url, tokenHeaders, deptInfoParameterizedTypeReference, uriVarilabes);
        } catch (Exception e) {
            LOG.error("连接公共服务平台错误，httpGetAllDepartByOrgtype({})", orgtype);
            e.printStackTrace();
            return Optional.ofNullable(null);
        }
        Integer code = deptResp.getCode();
        if(code != 200) {
            System.out.println(deptResp.getMessage());
            deptResp = null;
        }
        if (!Integer.valueOf(APIConstants.CODE_SUCCESS).equals(code)) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(deptResp);
    }

    public static List<OrgZTreeNodeResp> httpGetAllOrgNode() {
        String url = getIocPlatformConfig().getAdminDepartZtree();
        testAccessLogin();
        ParameterizedTypeReference<List<OrgZTreeNodeResp>> listZtreeNodeParamReference = new ParameterizedTypeReference<List<OrgZTreeNodeResp>>() {};
        List<OrgZTreeNodeResp> orgZTreeNodeResps = null;
        try {
            orgZTreeNodeResps = RestTemplateUtils.get(url, tokenHeaders, listZtreeNodeParamReference);
        } catch (Exception e) {
            LOG.error("连接公共服务平台错误，httpGetAllOrgNode()");
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
        return orgZTreeNodeResps;
    }

    public static List<UserResp> httpGetUsersByOrgAndRole(String orgid, String deptCode, String roleAlias){
        String url = getIocPlatformConfig().getOrgRoleGetUsers();
        testAccessLogin();
        ParameterizedTypeReference<List<UserResp>> deptInfoParameterizedTypeReference = new ParameterizedTypeReference<List<UserResp>>() {};
        Map<String, Object> uriVarilabes = new HashMap<>();
        uriVarilabes.put("orgid", orgid);
        uriVarilabes.put("deptcode", deptCode);
        uriVarilabes.put("roleAlias", roleAlias);
        List<UserResp> users = null;
        try {
            users = RestTemplateUtils.get(url, tokenHeaders, deptInfoParameterizedTypeReference, uriVarilabes);
        } catch (Exception e) {
            LOG.error("连接公共服务平台错误，httpGetUsersByOrgAndRole({},{},{})", orgid, deptCode, roleAlias);
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
        return users;
    }

/*
    public static String getPermission(Long resourceId) {
        testAccessLogin();
        String url = "http://127.0.0.1:8080/datalake/subscribe/dlSubscibeCatalogForms/permission";
        MultiValueMap<String, String> paramsKeyValues = new LinkedMultiValueMap<>();
        paramsKeyValues.add("resourceId", String.valueOf(resourceId));
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(paramsKeyValues)
                .build().encode();
        url = uriComponents.toString();
        RestTemplate restTemplate = RestTemplateUtils.getRestTemplate();
        ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                HttpStatus statusCode = response.getStatusCode();
                if (HttpStatus.TEMPORARY_REDIRECT.equals(statusCode) || HttpStatus.NOT_FOUND.equals(statusCode)){
                    return true;
                }
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                HttpStatus statusCode = response.getStatusCode();
                if (HttpStatus.TEMPORARY_REDIRECT.equals(statusCode)){
                    HttpHeaders headers = response.getHeaders();
                    URI location = headers.getLocation();
                    String s = location.toString();
                    System.out.println(s);
                } else if (HttpStatus.NOT_FOUND.equals(statusCode)) {
                    System.out.println("404 没找到");
                }
            }
        };
        ResponseErrorHandler errorHandler = restTemplate.getErrorHandler();
        restTemplate.setErrorHandler(responseErrorHandler);

//        tokenHeaders = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity(tokenHeaders);
        ResponseEntity<BasicApiResponse> respEntity = RestTemplateUtils.restTemplate.exchange(url, HttpMethod.GET, httpEntity, BasicApiResponse.class);
//        ResponseEntity<String> respEntity = RestTemplateUtils.getRestTemplate().getForEntity(url, String.class);
        HttpStatus statusCode = respEntity.getStatusCode();
        String respMsg = null;
        if (HttpStatus.OK.equals(statusCode)) {
            respMsg = statusCode.getReasonPhrase();
        } else if (HttpStatus.NO_CONTENT.equals(statusCode)){
            HttpHeaders headers = respEntity.getHeaders();
            URI location = headers.getLocation();
            String s = location.toString();
            System.out.println(s);
            respMsg = s;
        }
        // 复原
        restTemplate.setErrorHandler(errorHandler);
        return respMsg;
    }
*/

    public static void main(String[] args) {
//        String deptRoute = getDeptRoute("4028818e41fb0cad0141fb0eb0ac00be");
//        Optional<List<OrgVO>> orgVOS = httpGetDepChild(getIocPlatformConfig().getAdminDepartRootId());
//        orgVOS.ifPresent(System.out::println);
////        System.out.println(orgVOS);
////        System.out.println(deptRoute);
//
//        Optional<DeptInfo> topDept = getTopDept("9b5424c2cb424bd99f58b0176ec816ff");
//        topDept.ifPresent(System.out::println);
//
//        String deptRoute1 = getDeptRoute("0fbec53f54964e579dee9f385bd600de");
//        System.out.println(deptRoute1);
//        Long resourceId = 94L;
//        String permission = getPermission(resourceId);
//        System.out.println(permission);

    }
}
