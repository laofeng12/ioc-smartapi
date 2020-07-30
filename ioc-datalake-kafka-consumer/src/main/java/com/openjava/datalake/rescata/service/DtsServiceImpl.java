package com.openjava.datalake.rescata.service;

import com.openjava.admin.component.IocAuthorizationToken;
import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datalake.common.responseBody.DtsDataSourceConnInfoResp;
import com.openjava.datalake.component.AccessDtsComponent;
import lombok.extern.slf4j.Slf4j;
import org.ljdp.component.exception.APIException;
import org.openjava.boot.conf.DtsIntegrationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * @Author JiaHai
 * @Description 汇聚平台调用 业务层接口实现类
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DtsServiceImpl implements DtsService {

    @Autowired
    private AccessDtsComponent accessDtsComponent;


    @Override
    public DtsDataSourceConnInfoResp queryDataByDataSourceConnInfo(String dataSourceId, OaUserVO oaUserVO) throws APIException {
        DtsDataSourceConnInfoResp dataSourceConnInfo = accessDtsComponent.getDataSourceConnInfo(dataSourceId, oaUserVO);
        return dataSourceConnInfo;
    }

}
