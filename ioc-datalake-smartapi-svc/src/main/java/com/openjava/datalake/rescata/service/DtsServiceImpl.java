package com.openjava.datalake.rescata.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.fastjson.TypeReference;
import com.openjava.admin.component.IocAuthorizationToken;
import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.common.responseBody.DtsDataSourceConnInfoResp;
import com.openjava.datalake.component.AccessDtsComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.result.DataApiResponse;
import org.openjava.boot.conf.DtsIntegrationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author JiaHai
 * @Description 汇聚平台调用 业务层接口实现类
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DtsServiceImpl implements DtsService {
    /**
     * 返回参数中的一个key值
     */
    public static final String ROWS = "rows";

    /**
     * 返回参数中的一个key值
     */
    public static final String DATA = "data";


    @Resource
    private AccessDtsComponent accessDtsComponent;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Override
    public DtsDataSourceConnInfoResp queryDataByDataSourceConnInfo(String dataSourceId, OaUserVO oaUserVO) throws APIException {
        DtsDataSourceConnInfoResp dataSourceConnInfo = accessDtsComponent.getDataSourceConnInfo(dataSourceId, oaUserVO);
        return dataSourceConnInfo;
    }

}
