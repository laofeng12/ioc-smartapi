package com.openjava.datalake.util;

import org.ljdp.common.spring.SpringContextManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  RESTFul标准请求工具
 *  支持泛型类
 *  使用方法 ：
 *          调用前先用org.springframework.core.ParameterizedTypeReference指定泛型类的实际类型
 *          eg.
 *              ParameterizedTypeReference<TestModelType<List<TestModelBody>>> parameterizedTypeReference = new ParameterizedTypeReference<TestModelType<List<TestModelBody>>>() {};
 *              TestModelType<List<TestModelBody>> listTestModelType = restTemplateUtils.get(testUrl, parameterizedTypeReference);
 *     若返回的类只是普通类型不带 参数类型 <T> 可以简化代码
 *              ParameterizedTypeReference<TestModel> typeReference = ParameterizedTypeReference.forType(TestModel.class);
 *              TestModel respBody = restTemplateUtils.get(testUrl, typeReference);
 *
 *  @author xjd
 * @date 20190201
 */
//@Component
public class RestTemplateUtils {

//    @Autowired
    private static RestTemplate restTemplate;

    // 单类测试用 静态不影响，最终还是注入的对象
/*    static {
        restTemplate = new RestTemplate();
    }*/

    public static synchronized RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            synchronized (RestTemplateUtils.class) {
                if (restTemplate == null) {
                    try {
                        restTemplate = SpringContextManager.getBean(RestTemplate.class);
                    } catch (Exception e) {
                    }
                    if (restTemplate == null) {
                        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                        //设置连接超时：ms
                        requestFactory.setConnectTimeout(5*1000);
                        //设置读取超时：ms
                        requestFactory.setReadTimeout(5*1000);
                        restTemplate = new RestTemplate(requestFactory);
                        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
                    }
                }
            }
        }

        return restTemplate;
    }


    public static <T> T getWithUri(String url, ParameterizedTypeReference<T> parameterizedTypeReference, MultiValueMap<String, String> paramsKeyValues){
        return getWithUri(url,null, parameterizedTypeReference, paramsKeyValues);
    }
    public static <T> T getWithUri(String url, HttpHeaders httpHeaders, ParameterizedTypeReference<T> parameterizedTypeReference, MultiValueMap<String, String> paramsKeyValues){
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(paramsKeyValues)
                .build().encode();
//        URI uri = uriComponents.toUri();
        url = uriComponents.toString();
        return get(url, httpHeaders, parameterizedTypeReference);
    }
    public static <T> T getWithUri(String url, HttpHeaders httpHeaders, Class<T> respClass, Map<String, Object> uriVarilabes){
        return get(url, new HttpEntity<>(httpHeaders), null, respClass, uriVarilabes);
    }
    public static <T> T get(String url, ParameterizedTypeReference<T> parameterizedTypeReference){
        return get(url, null, parameterizedTypeReference, null);
    }
    public static <T> T get(String url, ParameterizedTypeReference<T> parameterizedTypeReference, Map<String, ?> uriVarilabes) {
        return get(url, null, parameterizedTypeReference, uriVarilabes);
    }
    public static <T> T get(String url, HttpHeaders httpHeaders, ParameterizedTypeReference<T> parameterizedTypeReference) {
        return get(url, new HttpEntity<>(httpHeaders), null, parameterizedTypeReference, null);
    }
    public static <T> T get(String url, HttpHeaders httpHeaders, ParameterizedTypeReference<T> parameterizedTypeReference, Map<String, ?> uriVarilabes) {
        return get(url, new HttpEntity<>(httpHeaders), null, parameterizedTypeReference, uriVarilabes);
    }
    public static <T> T get(String url, HttpEntity httpEntity, Object body, ParameterizedTypeReference<T> parameterizedTypeReference, Map<String, ?> uriVarilabes) {

        if (httpEntity == null) {
            httpEntity = new HttpEntity(body, setJsonHeader(new HttpHeaders()));
        }
        if (uriVarilabes == null) {
            uriVarilabes = new HashMap<>(0);
        }

        ResponseEntity<T> responseEntity = getRestTemplate().exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                parameterizedTypeReference,
                uriVarilabes
        );

        return responseEntity.getBody();
    }
    public static <T> T get(String url, HttpEntity httpEntity, Object body, Class<T> respClass, Map<String, ?> uriVarilabes) {

        if (httpEntity == null) {
            httpEntity = new HttpEntity(body, setJsonHeader(new HttpHeaders()));
        }
        if (uriVarilabes == null) {
            uriVarilabes = new HashMap<>(0);
        }

        ResponseEntity<T> responseEntity = getRestTemplate().exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                respClass,
                uriVarilabes
        );

        return responseEntity.getBody();
    }


    public static <T> T post(String url, HttpEntity requestEntity, HttpHeaders httpHeaders, ParameterizedTypeReference<T> parameterizedTypeReference, Map<String, ?> uriVariables){

        if (requestEntity == null) {
            requestEntity = new HttpEntity<>(httpHeaders);
        }
        ResponseEntity<T> responseEntity = getRestTemplate().exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                parameterizedTypeReference,
                uriVariables
        );
        return responseEntity.getBody();
    }
    public static <T> T post(String url, HttpEntity requestEntity, HttpHeaders httpHeaders, Class<T> respClass, Map<String, ?> uriVariables){

        if (requestEntity == null) {
            requestEntity = new HttpEntity<>(httpHeaders);
        }
        ResponseEntity<T> responseEntity = getRestTemplate().exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                respClass,
                uriVariables
        );
        return responseEntity.getBody();
    }

    public static <T> T postFormData(String url, HttpHeaders httpHeaders, MultiValueMap<String, ?> reqParams, Class<T> respClass){
        if (httpHeaders == null) {
            httpHeaders = new HttpHeaders();
        }
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity httpEntity = new HttpEntity(reqParams, httpHeaders);

        return post(url, httpEntity, null, respClass, new HashMap<>());
    }
    public static <T> T postFormData(String url, ParameterizedTypeReference<T> parameterizedTypeReference, MultiValueMap<String, ?> params){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity httpEntity = new HttpEntity(params, httpHeaders);

        return post(url, httpEntity, null, parameterizedTypeReference, new HashMap<>());
    }
    public static <T> T postJsonBody(String url, ParameterizedTypeReference<T> parameterizedTypeReference, Object body){
        HttpEntity httpEntity = new HttpEntity(body, setJsonHeader(null));
        return post(url, httpEntity, null, parameterizedTypeReference, new HashMap<>());
    }
    public static <T> T postJsonBody(String url, HttpHeaders httpHeaders, Object requeysBody, Class<T> respClass){
        HttpEntity httpEntity = new HttpEntity(requeysBody, setJsonHeader(httpHeaders));
        return post(url, httpEntity, null, respClass, new HashMap<>());
    }
    public static <T> T postJsonBody(String url, HttpHeaders httpHeaders, ParameterizedTypeReference<T> parameterizedTypeReference, Object body){
        HttpEntity httpEntity = new HttpEntity(body, setJsonHeader(httpHeaders));
        return post(url, httpEntity, null, parameterizedTypeReference, new HashMap<>());
    }

    public static <T> T put(String url, HttpEntity requestEntity, ParameterizedTypeReference<T> parameterizedTypeReference){

        return null;
    }

    /**
     *    设置Json提交/接收请求头
     * @param httpHeaders
     * @return
     */
    public static HttpHeaders setJsonHeader(HttpHeaders httpHeaders){

        if(httpHeaders == null){
            httpHeaders = new HttpHeaders();
        }
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        return httpHeaders;
    }

    public String sayHello3() {

        ResponseErrorHandler errorHandler = getRestTemplate().getErrorHandler();

        UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://HELLO-SERVICE/sayhello?name={name}")
                .build().expand("王五").encode();
        URI uri = uriComponents.toUri();
        ResponseEntity<String> responseEntity = getRestTemplate().getForEntity(uri, String.class);

        return responseEntity.getBody();
    }

    public static void main(String[] args) {
        ParameterizedTypeReference<String> typeReference = new ParameterizedTypeReference<String>() {};
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name", "frank");
        multiValueMap.add("age", "18");
        multiValueMap.add("name", "alice");
        String withUri = getWithUri("http://HELLO-SERVICE/sayhello", typeReference, multiValueMap);
    }

}
