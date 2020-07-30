package com.openjava.datalake.rescata.service;

import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datalake.common.responseBody.DtsDataSourceConnInfoResp;
import org.ljdp.component.exception.APIException;

import java.util.List;

/**
 * @Author JiaHai
 * @Description 汇聚平台调用 业务层接口
 */
public interface DtsService {

    /**
     * 获取数据源的JDBC连接信息
     * @param dataSourceId
     * @param oaUserVO
     * @return
     * @throws APIException
     */
    DtsDataSourceConnInfoResp queryDataByDataSourceConnInfo(String dataSourceId, OaUserVO oaUserVO) throws APIException;

}
